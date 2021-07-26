package com.danebrown.netty.server;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by danebrown on 2021/7/14
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Log4j2
public class ServerSelectorMultiThreadMain {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        int mode = 2;
        System.out.println("请输入监听模式[1-3]，空或者其他值将采用最原始的模式:");
        if (scan.hasNextLine()) {
            mode = Optional.ofNullable(scan.nextInt()).orElse(-1);
        }

        ExecutorService bossexecutorService = Executors.newCachedThreadPool();

        ExecutorService workerexecutorService = Executors.newCachedThreadPool();
        ThreadSelectorGroup boss = new ThreadSelectorGroup(2,
                bossexecutorService, mode,"boss");
        ThreadSelectorGroup worker = new ThreadSelectorGroup(3,
                workerexecutorService, mode,"worker");
        boss.setGroup(worker);

        boss.bind(9999);
        //        boss.bind(8888);
        //        boss.bind(7777);
        //        boss.bind(6666);
    }


    public static class ThreadSelectorGroup {

        private ThreadSelectorGroup worker = this;
        /**
         * 1:采用wakeup模式
         * 2:采用blockingQuene模式
         */
        private int mode;
        private ThreadSelector[] selectors;
        private ServerSocketChannel server;
        private AtomicInteger currentIdx = new AtomicInteger(0);
        private AtomicInteger workerCurrentIdx = new AtomicInteger(0);
        private String name;

        /**
         * 创建的线程数
         *
         * @param num
         * @param executorService
         */
        public ThreadSelectorGroup(int num, ExecutorService executorService,
                                   int mode,String name) {
            selectors = new ThreadSelector[num];
            this.mode = mode;
            this.name = name;
            for (int i = 0; i < num; i++) {
                selectors[i] = new ThreadSelector(this);
                //                new Thread(selectors[i]).start();
                executorService.submit(selectors[i]);
            }

        }

        public void setGroup(ThreadSelectorGroup group) {
            this.worker = group;
        }

        /**
         * 绑定一个端口号
         *
         * @param port
         */
        public void bind(int port) {
            try {
                server = ServerSocketChannel.open();
                server.configureBlocking(false);
                server.bind(new InetSocketAddress((port)));
                log.info("groupName:{};bind {}",this.name,
                        server.getLocalAddress());
                nextSelector(server);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 随机选择一个selector，负责后续的操作
         * 由于要兼容serverSocketChannel和SocketChannel，因此使用他们的父类
         *
         * @param channel
         */
        private void nextSelector(Channel channel) throws ClosedChannelException {
            ThreadSelector next = next(channel);
            if (mode == 2) {
                if(channel instanceof ServerSocketChannel){

                    next.lbq.add(channel);
                    //改变使用worker组,因为使用了分组，因此如果是服务器的话，需要设定他的worker组
                    next.setGroup(worker);
                    /**
                     * 相当于把channel放到队列里面，把信息传递给ThreadSelector线程了，然后再wakeup。
                     * 这样就解决了mode1 情况中register放前面也不行，放后面也不行的尴尬境地
                     */
                    next.selector.wakeup();
                    log.info("bind->lbqadd->selector.wakeup() " +
                                    "keysize:{}是让之前阻塞的地方解除阻塞 groupName:{}",
                            next.selector.keys().size(),
                            next.group.name
                    );
                }
                else if(channel instanceof SocketChannel){
                    //客户端，选出来的next已经是work组中的了，因此无需再次设定group
                    next.lbq.add(channel);
                    next.selector.wakeup();
                    log.info("client bind->lbqadd->selector.wakeup() " +
                            "keysize:{}是让之前阻塞的地方解除阻塞; groupName:{}",
                            next.selector.keys().size(),
                            next.group.name
                    );

                }

            } else if (mode == 1) {
                //下面是重点
                if (channel instanceof ServerSocketChannel) {
                    //表明是服务端的，要绑定selector进行register

                    ServerSocketChannel server = (ServerSocketChannel) channel;
                    //
                    /**
                     * 注意，此时register，时机可能晚了，
                     * int num = selector.select();可能已经阻塞了，因此要使用weakup，取消阻塞
                     * 这里是基本原理，但是这种方式有问题，因为涉及到线程先后执行的问题。
                     * 千万不要使用这种方式
                     */

                    //                log.info("bind->reg->OP_ACCEPT begin keysize :{}",
                    //                        next.selector.keys().size());
                    server.register(next.selector, SelectionKey.OP_ACCEPT);
                    log.info("bind->reg->OP_ACCEPT end keysize :{}", next.selector.keys().size());
                    //这句wakeup放在reg前面也不对，放在后面也不对，因为放前面没来得及级处理就阻塞了。放后面可能过时了。
                    next.selector.wakeup();
                    log.info("bind->reg->selector.wakeup() keysize:{}是让之前阻塞的地方解除阻塞", next.selector.keys().size());
                } else if (channel instanceof SocketChannel) {
                    //表明是客户端的，要进行accept或者read
                }
            }
        }

        private ThreadSelector next(Channel channel) {
            if(channel instanceof ServerSocketChannel){
                //从boss中选择selector
                int xid = this.currentIdx.incrementAndGet() % selectors.length;
                log.info("xid is :[{}],groupName:{}", xid, this.name);
                return selectors[xid];
            }
            else{
                //从worker中选择selector
                int xid =
                        this.workerCurrentIdx.incrementAndGet() % worker.selectors.length;
                log.info("worker xid is :[{}],groupName:{}", xid,worker.name);
                return worker.selectors[xid];
            }
        }

    }

    public static class ThreadSelector extends ThreadLocal<LinkedBlockingQueue<Channel>> implements Runnable {

        private Selector selector = null;

        private LinkedBlockingQueue<Channel> lbq = get();

        @Override
        protected LinkedBlockingQueue<Channel> initialValue() {
            return new LinkedBlockingQueue<>();
        }

        public void setGroup(ThreadSelectorGroup group) {
            this.group = group;
        }

        private ThreadSelectorGroup group;

        public ThreadSelector(ThreadSelectorGroup group) {
            try {
                this.group = group;
                //                log.info("Selector.open before");
                selector = Selector.open();
                //                log.info("Selector.opened :{}",selector);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            //如果一个线程一直在死循环，我们称之为loop
//            Thread.currentThread().setName(this.group.name);
            while (true) {
                try {
                    /**
                     * 1
                     * 这里会阻塞
                     * 但是selector有个weakup方法，这个方法会打断阻塞，此时num可能小于0
                     * 在serverSocketSelector做register的时候，可能在这之后执行的，那么可能会导致
                     * 永远无法注册上
                     */
                    log.info("Selector.select before;keysize:{};groupName:{}",
                            selector.keys().size(),
                            this.group.name);
                    int num = selector.select();
                    log.info("Selector.selected :num:{};keysize:{};groupName:{}",
                            num,
                            selector.keys().size(),
                            this.group.name
                    );

                    if (num > 0) {
                        /**
                         * 2
                         * 做selectkeys
                         */
                        Set<SelectionKey> selectionKeys = selector.selectedKeys();
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();
                        while (iterator.hasNext()) {
                            /**
                             * 这里之后是线性处理
                             */
                            SelectionKey key = iterator.next();
                            /**
                             * remove是为了避免重复消费
                             */
                            iterator.remove();
                            if (key.isAcceptable()) {
                                acceptHandler(key);
                            } else if (key.isReadable()) {
                                readHandler(key);
                            } else if (key.isWritable()) {
                                writeHandler(key);
                            } else if (key.isValid()) {

                            } else if (key.isConnectable()) {

                            }
                        }
                    } else {
                        /**
                         * 3
                         * 处理一些task
                         * 例如处理处理服务端的register或者客户端的listen
                         */
                        if (!lbq.isEmpty()) {
                            try {
                                Channel channel = lbq.take();
                                if (channel instanceof ServerSocketChannel) {
                                    ServerSocketChannel server = (ServerSocketChannel) channel;
                                    server.register(selector, SelectionKey.OP_ACCEPT);
                                    log.info("server register ,groupName:{}",
                                            this.group.name);
                                } else if (channel instanceof SocketChannel) {
                                    SocketChannel client = (SocketChannel) channel;
                                    //注册一个8192字节的bytebuffer到客户端读取的缓存中
                                    client.register(selector, SelectionKey.OP_READ, ByteBuffer.allocateDirect(8192));
                                    log.info("client register ,groupName:{}",
                                            this.group.name);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * write处理
         *
         * @param key
         */
        private void writeHandler(SelectionKey key) {

        }

        /**
         * read处理
         *
         * @param key
         */
        private void readHandler(SelectionKey key) {
            ByteBuffer buffer = (ByteBuffer) key.attachment();
            SocketChannel client = (SocketChannel) key.channel();
            try {
                client.configureBlocking(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            buffer.clear();
            while (true) {
                try {
                    int num = client.read(buffer);
                    if (num > 0) {
                        log.info("readHandler->num:{}; groupName:{}", num,
                                this.group.name);
                        buffer.flip();
                        while (buffer.hasRemaining()) {
                            client.write(buffer);
                        }
                        buffer.clear();
                    } else if (num == 0) {
                        break;
                    } else {
                        /**
                         * 客户端断开连接了
                         */
                        log.info("client {} is close; groupName:{}",
                                client.getRemoteAddress(),this.group.name);
                        //这里要注意，一定要cancel，因为连接已经断开了，不需要在关注了
                        key.cancel();
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * accept处理
         *
         * @param key
         */
        private void acceptHandler(SelectionKey key) {
            //拿到server的socket channel
            ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            try {
                //accpet建立连接
                SocketChannel client = channel.accept();
                //设置为unblocking
                client.configureBlocking(false);
                //选择一个selector 并进行注册
                log.info("acceptHandler->remote:{},local:{};groupName:{}",
                        client.getRemoteAddress(),
                        client.getLocalAddress(),
                        this.group.name
                );
                /**
                 * 这里相当于把当前这个channel，又扔回到group中进行重新选择了selector了
                 */
                this.group.nextSelector(client);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

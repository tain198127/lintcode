package com.danebrown.netty.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by danebrown on 2021/7/14
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
public class ServerSelectorMultiThreadMain {
    public static void main(String[] args) {

        ThreadSelectorGroup boss = new ThreadSelectorGroup(2);
        ThreadSelectorGroup worker = new ThreadSelectorGroup(3);
        boss.setGroup(worker);
        boss.bind(9999);
        boss.bind(8888);
        boss.bind(7777);
        boss.bind(6666);
    }


    public static class ThreadSelector extends ThreadLocal<LinkedBlockingQueue<Channel>> implements Runnable {

        LinkedBlockingQueue<Channel> lbq = get();
        private ThreadSelectorGroup group;
        private Selector selector;

        public ThreadSelector(ThreadSelectorGroup group) {
            this.group = group;
            try {
                selector = Selector.open();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected LinkedBlockingQueue<Channel> initialValue() {
            return new LinkedBlockingQueue<>();
        }

        public ThreadSelectorGroup getGroup() {
            return group;
        }

        public void setGroup(ThreadSelectorGroup group) {
            this.group = group;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    int num = selector.select();
                    if (num > 0) {
                        Set<SelectionKey> selectionKeys = selector.selectedKeys();
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();
                        while (iterator.hasNext()) {
                            SelectionKey next = iterator.next();
                            iterator.remove();
                            if (next.isAcceptable()) {
                                //做accpet
                            } else if (next.isReadable()) {
                                //做读
                            } else if (next.isWritable()) {
                                //做写
                            }
                        }
                    }
                    if (!lbq.isEmpty()) {

                        Channel c = lbq.take();
                        if(c instanceof ServerSocketChannel){
                            //可能是append
                            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) c;
                            serverSocketChannel.register(selector,
                                    SelectionKey.OP_ACCEPT);
                        }
                        else if(c instanceof SocketChannel){
                            SocketChannel client = (SocketChannel) c;
                            ByteBuffer buffer =
                                    ByteBuffer.allocateDirect(4096);
                            client.register(selector,SelectionKey.OP_READ,buffer);
                        }
                        //可能是client
                    }

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class ThreadSelectorGroup {
        private ThreadSelector[] sts;
        private ServerSocketChannel channel;
        private AtomicInteger xid = new AtomicInteger(0);
        //用来hold子group的
        private ThreadSelectorGroup groupHolder = this;

        /**
         * 启动几个线程组
         *
         * @param threadNum
         */
        public ThreadSelectorGroup(int threadNum) {

            sts = new ThreadSelector[threadNum];
            for (int i = 0; i < threadNum; i++) {
                sts[i] = new ThreadSelector(this);
                new Thread(sts[i]).start();
            }


        }

        /**
         * 绑定一个端口
         *
         * @param port
         */
        public void bind(int port) {
            try {

                channel = ServerSocketChannel.open();
                channel.configureBlocking(false);
                channel.bind(new InetSocketAddress(port));
                regNextSelector(channel);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 获取
         *
         * @return
         */
        public void regNextSelector(Channel channel) {

            int idx = xid.getAndIncrement() % sts.length;
            ThreadSelector selector = sts[idx];
            if (channel instanceof ServerSocketChannel) {
                //负责accpet等
                selector.setGroup(this.groupHolder);
                selector.selector.wakeup();
            } else if (channel instanceof SocketChannel) {
                //负责读写
                selector.selector.wakeup();
            }


        }


        /**
         * 如果是boos的话可以用来绑定work
         *
         * @param threadSelectorGroup
         */
        public void setGroup(ThreadSelectorGroup threadSelectorGroup) {
            this.groupHolder = threadSelectorGroup;
        }


    }
}

package com.danebrown.netty.server;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 纯手工写netty
 * Created by danebrown on 2021/7/4
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Log4j2
public class ServerSelectorMain {
    ExecutorService service = Executors.newCachedThreadPool();
    private ServerSocketChannel serverSocketChannel = null;
    private Selector selector = null;
    private int port = 9998;

    private long count;

    public static String toJson(Object obj) {

        return JSON.toJSONString(obj, SerializerFeature.SkipTransientField);
    }

    public static void main(String[] args) throws IOException {
        ServerSelectorMain serverSelectorMain = new ServerSelectorMain();
        serverSelectorMain.initServer();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine() && scanner.nextLine().equalsIgnoreCase("fin")) {
            System.out.println("bye");
            break;
        }
    }

    public void initServer() throws IOException {
        System.out.println("请输入任意字符进行下一步");
        //创建一个连接
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            String cmd = scanner.nextLine();
            //socket (PF_INT,SOCKET_STREAM,IPPORTO_IP) = 4创建一个SOCKET FD4
            serverSocketChannel = ServerSocketChannel.open();
            log.warn("ServerSocketChannel.open()\n{}", toJson(serverSocketChannel));
        }
        System.out.println("请输入任意字符进行下一步");
        if (scanner.hasNextLine()) {
            String cmd = scanner.nextLine();
            //设置为非阻塞
            //其中4是上一步创建的那个FD的号，就是FD4
            //fcntl(4,F_SETFL,O_RWWRIO_NOBLOCK)=0,调用系统命令，设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            log.warn("serverSocketChannel.configureBlocking(false)\n{}", toJson(serverSocketChannel));
        }
        System.out.println("请输入任意字符进行下一步");
        //绑定端口
        if (scanner.hasNextLine()) {
            String cmd = scanner.nextLine();
            //这里有两个系统调用
            //bind(4,sa_family=AF_INET,sin_port=9998)，将这个SOCKET和9998这个端口绑定
            //listen(4,50)监听4这个端口
            serverSocketChannel.bind(new InetSocketAddress(port));
            log.warn("serverSocketChannel.bind:[{}],channel:\n{}", port, toJson(serverSocketChannel));
        }
        System.out.println("请输入任意字符进行下一步");
        //开启一个socket
        if (scanner.hasNextLine()) {
            String cmd = scanner.nextLine();
            //在EPOLL模式下，会创建一个FD5,专门用来接收事件
            // epoll_create，这个epoll_create就是用来接收网络接入事件的红黑树的
            // 也就是epfd==epoll的文件描述符,FD5就是那个EPFD
            //在其他模式下，不会创建FD
            selector = Selector.open();
            log.warn("Selector.open()\n{}", toJson(selector));
        }
        System.out.println("请输入任意字符进行下一步");
        if (scanner.hasNextLine()) {
            String cmd = scanner.nextLine();
            //EPOLL模式下，会调用epoll_ctl(fd5,EPOLL_CTL_ADD,fd4,EPOLLIN)
            //表示在FD5（EPFD的事件红黑树）中添加一个FD4（ServerSocketChannel.open()创建的那个FD4）
            //并且关注它的EPOLLIN事件
            //serverSocketChannel.register=会调用epoll_ctl(fd5,EPOLL_CTL_ADD,fd4,EPOLLIN)


            //意思是说，把FD5，也就是selector.open创建的那个，加入到FD4的接入等待队列中，并且关注的是EPOLLIN事件
            //可以开始接收事件了
            //select pool模式下 jvm里面会创建一个数组，把FD5塞到这个JVM数组中，充当红黑树事件处理
            SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            log.warn("serverSocketChannel.register:\n{} ", toJson(selector));
        }
        System.out.println("请输入任意字符进行下一步");
        if (scanner.hasNextLine()) {
            String cmd = scanner.nextLine();
            System.out.println("listen");
            listener(selector, 1);
        }

    }

    public void listener(Selector selector, int queueSize) {
        while (true) {
            try {
                //这里系统调用的是epoll_wait(FD5,{EPOLLIN或者是其他状态，这个就是后面一大串if判断的标志})
                // ，相当于等待FD5有连接进来
                while (selector.select() > 0) {
                    //鉴别有哪些事件，拿出来的是一堆状态
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey serverSelectionKey = iterator.next();
                        iterator.remove();
                        //判断进来的FD是EPOLLIN
                        if (serverSelectionKey.isAcceptable()) {
                            log.warn("serverSelectionKey.isAcceptable:\n{}", toJson(serverSelectionKey));

                            // 了，并且注册类型设置为READ。
                            //收到一个是要求accept类型的FD
                            /**
                             * 在多线程下面，这个新的SOCKET要注册到哪个selector上呢？
                             */
                            accept(serverSelectionKey);
                        } else if (serverSelectionKey.isConnectable()) {
                            log.warn("serverSelectionKey.isConnectable:{}", toJson(serverSelectionKey));
                            connect(serverSelectionKey);
                        } else if (serverSelectionKey.isReadable()) {
                            serverSelectionKey.cancel();
                            log.warn("serverSelectionKey.isReadable:{}", serverSelectionKey);
                            read(serverSelectionKey);
                        } else if (serverSelectionKey.isValid()) {
                            log.warn("serverSelectionKey.isValid:{}", serverSelectionKey);
                            valid(serverSelectionKey);
                        } else if (serverSelectionKey.isWritable()) {
                            serverSelectionKey.cancel();
                            log.warn("serverSelectionKey.isWritable:{}", serverSelectionKey);
                            write(serverSelectionKey);
                        }
                    }
                }
            } catch (IOException ex) {
                log.error("selector.select 出错", ex);
            }
        }

    }

    /**
     * accept
     *
     * @param serverSelectionKey
     */
    public void accept(SelectionKey serverSelectionKey) {
        ServerSocketChannel ssc = (ServerSocketChannel) serverSelectionKey.channel();
        try {
            //注意FD5是个EPFD，是个接收事件的红黑树的FD，从他里面读到一个EPOLLIN的请求，也就是FD7
            //调用的是系统命令ACCEPT(),得到一个客户端到服务端的SOCKET连接，也就是从FD5得到一个FD7
            SocketChannel client = ssc.accept();
            //这里调用了一次 epoll_ctl(FD7,F_SETFL,O_RDWR|O_NOBLOCKING)
            // 相当于设定FD7这个文件描述符为非阻塞
            client.configureBlocking(false);
            //开辟一个8192大小的堆外内存
            ByteBuffer buffer = ByteBuffer.allocate(8192);
            /*
            select，poll：jvm里开辟一个数组 fd7 放进去
            epoll：  epoll_ctl(fd5,EPOLL_CTL_ADD,fd7,EPOLLIN
            表示在FD5(EPFD)里面再加上一个FD7,并且关注的是EPOLLIN这个事件
            //这里读取到accept以后，又注册了一个FD例如FD7，然后把FD7扔回了selector
            此时epoll里面，应该有FD4和FD7，这里体现了多路复用器的精髓
            但是这个只是单线程的方式
             */
            client.register(selector, SelectionKey.OP_READ, buffer);
            System.out.println("-------------------------------------------");
            System.out.println("新客户端：" + client.getRemoteAddress());
            System.out.println("-------------------------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect(SelectionKey serverSelectionKey) {

    }

    public void read(SelectionKey serverSelectionKey) {
        //拿到数据
        ByteBuffer buffer = (ByteBuffer) serverSelectionKey.attachment();
        //拿到客户端的那个socket
        SocketChannel client = (SocketChannel) serverSelectionKey.channel();

        buffer.clear();
        while (true){
            try {
                int num = client.read(buffer);
                if (num >0 ){
                    //读取
                    //将buffer翻转一下，表示开始读取
                    buffer.flip();
                    while (buffer.hasRemaining()){
                        client.write(buffer);
                    }
                    buffer.clear();
                }
                else if(num == 0){
                    //没有读到东西
                    break;
                }
                else if(num < 0){
                    //连接断开
                    System.err.println("连接断开了"+client.getRemoteAddress());
                    serverSelectionKey.cancel();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void valid(SelectionKey serverSelectionKey) {

    }

    public void write(SelectionKey serverSelectionKey) {

    }
}

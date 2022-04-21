package com.danebrown.netty.customs.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.CompleteFuture;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by danebrown on 2022/2/10
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Log4j2
public class CustomServer {
    /**
     * 配置一些参数
     */
    public static void addProperties(){
        System.setProperty("java.net.preferIPv4Stack","true");
    }

    /**
     * 增加优雅停机
     * @param supplier
     */
    public static void shutdownHook(Supplier<CompletableFuture> supplier){
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            log.warn("系统停机了，清理netty资源池");
            CompletableFuture completableFuture = supplier.get();
            try {
                completableFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("清理netty资源池失败",e);
            }
            log.warn("清理netty资源池完成，继续执行后续停机任务");
        }));
    }
    public static void main(String[] args) {
        addProperties();
        ServerBootstrap b = new ServerBootstrap();
        EventLoopGroup boss = new NioEventLoopGroup(1);

        EventLoopGroup child = new NioEventLoopGroup();

        shutdownHook(()->{
            /**
             * 调用链：NioEventLoopGroup-->NioEventLoop-->shutdownGracefully
             * -->采用自旋方式，修改线程状态-->clossAll（把所有注册在selector上的channel都关闭）
             */
            Future f = boss.shutdownGracefully();
            Future c = child.shutdownGracefully();
            /**
             * 优雅停机：
             * 没flush的尽快flush
             * 没完成读写的、继续读写
             * 尽快释放nio资源
             * 清理定时任务
             */
            CompletableFuture bossFuture = CompletableFuture.completedFuture(f);
            CompletableFuture childFuture =
                    CompletableFuture.completedFuture(c);
            CompletableFuture completeFuture =
                    CompletableFuture.allOf(bossFuture,childFuture);
            return completeFuture;
        });
        b.group(boss,child);
        b.channel(NioServerSocketChannel.class);
        b.option(ChannelOption.SO_BACKLOG,100);
        b.handler(new LoggingHandler(LogLevel.INFO));
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast(new LoggingHandler(LogLevel.INFO));
            }
        });
        try {
            ChannelFuture f = b.bind(18080).sync();
            f.channel().closeFuture().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    log.info(future.channel().closeFuture()+"链路关闭");
                }
            });
            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            boss.shutdownGracefully();
            child.shutdownGracefully();
            log.error("异常：",e);
        }
    }
}

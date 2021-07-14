package com.danebrown.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;

import javax.annotation.Resource;

/**
 * Created by danebrown on 2021/6/30
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
//@SpringBootApplication
@Log4j2
public class NettyServer implements CommandLineRunner, ApplicationListener<ContextClosedEvent> {
    private static ServerBootstrap serverBootstrap;
    @Resource(name = "boss")
    EventLoopGroup boss = new NioEventLoopGroup(1);
    @Resource(name = "worker")
    EventLoopGroup workers = new NioEventLoopGroup();

    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder(NettyServer.class).web(WebApplicationType.NONE).run(args);

    }

    @Bean(name = "boss")
    public EventLoopGroup genBoss() {
        return new NioEventLoopGroup(1);
    }

    @Bean(name = "worker")
    public EventLoopGroup genWorkers() {
        return new NioEventLoopGroup();
    }

    @Override
    public void run(String... args) {

        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, workers).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.DEBUG)).childHandler(new ChannelInitializer() {
            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                log.error("exceptionCaught:ChannelHandlerContext:{};", ctx, cause);
                super.exceptionCaught(ctx, cause);
            }

            @Override
            public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                log.error("handlerAdded:{}", ctx);
                super.handlerAdded(ctx);
            }

            @Override
            public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
                log.info("handlerRemoved:{}", ctx);
                super.handlerRemoved(ctx);
            }

            @Override
            protected void initChannel(Channel channel) throws Exception {
                log.info("initChannel:{}", channel);
                ChannelPipeline p = channel.pipeline();

                p.addLast(new TestConsoleHandler());
            }
        });

        ChannelFuture ch = null;
        try {
            ch = serverBootstrap.bind(9999).sync();
            log.info("bind");
            ch.channel().closeFuture().sync();
            log.info("closeFuture");
        } catch (InterruptedException e) {
            e.printStackTrace();
            boss.shutdownGracefully();
            workers.shutdownGracefully();
        }

    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        if (workers != null && !workers.isShutdown()) {
            workers.shutdownGracefully();
        }
        if (boss != null && !boss.isShutdown()) {
            boss.shutdownGracefully();
        }
    }
}

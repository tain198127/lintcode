package com.danebrown.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.FastThreadLocalThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Scanner;

/**
 * Created by danebrown on 2021/7/1
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@SpringBootApplication
//@Configuration
@Component
public class NettyClient implements CommandLineRunner {

    @Autowired
    public TestClientHandler testClientHandler;

    @Bean
    public TestClientHandler getClientHandler(){
        return new TestClientHandler();
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(NettyClient.class).web(WebApplicationType.NONE).run(args);

    }

    @Override
    public void run(String... args) throws Exception {
        FastThreadLocalThread.sleep(3000);
        Scanner scan = new Scanner(System.in);
        System.out.println("请输入地址:");
        String host = null, port = null;
        if (scan.hasNextLine()) {
            host = Optional.ofNullable(scan.next()).orElse("127.0.0.1");
        }
        System.out.println("请输入端口");
        if (scan.hasNextLine()) {
            port = Optional.ofNullable(scan.next()).orElse("9999");
        }

        EventLoopGroup worker = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(worker);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(testClientHandler);

            }
        });
        ChannelFuture f = b.connect(host, Integer.parseInt(port)).sync();
        f.channel().closeFuture().sync();

    }

}

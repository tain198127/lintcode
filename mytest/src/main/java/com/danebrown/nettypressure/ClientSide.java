package com.danebrown.nettypressure;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by danebrown on 2020/4/15
 * mail: tain198127@163.com
 */
public class ClientSide {
    public static String host = "127.0.0.1";
    public static int port = 7878;
    public static class ClientBizHandler extends SimpleChannelInboundHandler<String>{

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
            System.out.println("Server say : " + msg);
        }
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("Client active ");
            super.channelActive(ctx);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("Client close ");
            super.channelInactive(ctx);
        }
    }
    public static class ClientInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            ChannelPipeline pipeline = socketChannel.pipeline();
            pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192,
                    Delimiters.lineDelimiter()));
            pipeline.addLast("decoder", new StringDecoder());
            pipeline.addLast("encoder", new StringEncoder());

            // 客户端的逻辑
            pipeline.addLast("handler", new ClientBizHandler());
        }
    }
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).handler(new ClientInitializer());
            Channel ch = b.connect(host,port).sync().channel();
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for(;;){
                String line = in.readLine();
                if(line == null){
                    continue;
                }
                ch.writeAndFlush(line+"\r\n");
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        finally {
            group.shutdownGracefully();
        }
    }
}

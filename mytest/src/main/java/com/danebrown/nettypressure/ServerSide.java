package com.danebrown.nettypressure;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetAddress;

/**
 * Created by danebrown on 2020/4/15
 * mail: tain198127@163.com
 */
public class ServerSide {
    /**
     * 真正的服务处理者
     */
    public static class BizHandler extends SimpleChannelInboundHandler<String>{

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
            System.out.println(ctx.channel().remoteAddress() + " Say : " + msg);
            ctx.writeAndFlush("Received your message !\n");

        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("RamoteAddress : " + ctx.channel().remoteAddress()
                    + " active !");

            ctx.writeAndFlush("Welcome to "
                    + InetAddress.getLocalHost().getHostName() + " service!\n");

            super.channelActive(ctx);
        }
    }

    /**
     * 初始化器
     */
    public static class ServerInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            ChannelPipeline pipeline = socketChannel.pipeline();
            pipeline.addLast("framer",new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
            pipeline.addLast("decoder", new StringDecoder());
            pipeline.addLast("encoder",new StringEncoder());
            pipeline.addLast("handler",new BizHandler());
        }
    }
    public static int portNumber= 7878;
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ServerInitializer());
            ChannelFuture channelFuture = b.bind(portNumber).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}

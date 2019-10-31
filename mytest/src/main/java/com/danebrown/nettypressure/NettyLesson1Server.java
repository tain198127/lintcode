package com.danebrown.nettypressure;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * 核心 reactor
 */
public class NettyLesson1Server {
    private final int port;
    public NettyLesson1Server(int port){
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        if(args.length != 1){
           System.err.println("arg length error");
           return;
        }
        int port = Integer.parseInt(args[0]);
        NettyLesson1Server channel = new NettyLesson1Server(port);
        channel.start();
    }

    /**
     * 启动
     */
    public void start() throws InterruptedException {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(nioEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyChannel());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            System.out.println(channelFuture.channel().localAddress());
            channelFuture.channel().closeFuture().sync();


        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            nioEventLoopGroup.shutdownGracefully().sync();
        }
    }
    public static class NettyChannel extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf bb = (ByteBuf)msg;
            System.out.println(bb.toString(CharsetUtil.UTF_8));
            super.channelRead(ctx, msg);
            ctx.write(bb);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
//            super.channelReadComplete(ctx);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            super.exceptionCaught(ctx, cause);
            ctx.close();
        }
    }
}


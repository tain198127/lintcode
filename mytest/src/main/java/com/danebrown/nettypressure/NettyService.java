package com.danebrown.nettypressure;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;

public class NettyService  {
    public static final Logger logger= LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    private void init(){

        NioEventLoopGroup boss=new NioEventLoopGroup();
        NioEventLoopGroup work=new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap=new ServerBootstrap();
            bootstrap.group(boss,work);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new NioWebSocketChannelInitializer());
            Channel channel = bootstrap.bind(8081).sync().channel();
            System.out.println("webSocket服务器启动成功："+channel);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("运行出错："+e);
        }finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
            System.out.println("websocket服务器已关闭");
        }
    }
    public static void main(String[] args){
        NettyService ns = new NettyService();
        ns.init();
    }

    public class NioWebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) {
            ch.pipeline().addLast("logging",new LoggingHandler("DEBUG"));//设置log监听器，并且日志级别为debug，方便观察运行流程
            ch.pipeline().addLast("http-codec",new HttpServerCodec());//设置解码器
            ch.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));//聚合器，使用websocket会用到
            ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());//用于大数据的分区传输
            ch.pipeline().addLast("handler",new NioWebSocketHandler());//自定义的业务handler
        }
    }
    public static class NioWebSocketHandler extends SimpleChannelInboundHandler<Object> {



        private WebSocketServerHandshaker handshaker;

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
            System.out.println("收到消息："+msg);
            if (msg instanceof FullHttpRequest){
                //以http请求形式接入，但是走的是websocket
                handleHttpRequest(ctx, (FullHttpRequest) msg);
            }else if (msg instanceof WebSocketFrame){
                //处理websocket客户端的消息
                handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
            }
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            //添加连接
            System.out.println("客户端加入连接："+ctx.channel());
            ChannelSupervise.addChannel(ctx.channel());
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            //断开连接
            System.out.println("客户端断开连接："+ctx.channel());
            ChannelSupervise.removeChannel(ctx.channel());
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {
            ctx.flush();
        }
        private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame){
            // 判断是否关闭链路的指令
            if (frame instanceof CloseWebSocketFrame) {
                handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
                return;
            }
            // 判断是否ping消息
            if (frame instanceof PingWebSocketFrame) {
                ctx.channel().write(
                        new PongWebSocketFrame(frame.content().retain()));
                return;
            }
            // 本例程仅支持文本消息，不支持二进制消息
            if (!(frame instanceof TextWebSocketFrame)) {
                throw new UnsupportedOperationException(String.format(
                        "%s frame types not supported", frame.getClass().getName()));
            }
            // 返回应答消息
            String request = ((TextWebSocketFrame) frame).text();
            TextWebSocketFrame tws = new TextWebSocketFrame(new Date().toString()
                    + ctx.channel().id() + "：" + request);
            // 群发
            ChannelSupervise.send2All(tws);
            // 返回【谁发的发给谁】
            // ctx.channel().writeAndFlush(tws);
        }
        /**
         * 唯一的一次http请求，用于创建websocket
         * */
        private void handleHttpRequest(ChannelHandlerContext ctx,
                                       FullHttpRequest req) {
            //要求Upgrade为websocket，过滤掉get/Post
            if (!req.decoderResult().isSuccess()
                    || (!"websocket".equals(req.headers().get("Upgrade")))) {
                //若不是websocket方式，则创建BAD_REQUEST的req，返回给客户端
                sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
                return;
            }
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                    "ws://localhost:8081/websocket", null, false);
            handshaker = wsFactory.newHandshaker(req);
            if (handshaker == null) {
                WebSocketServerHandshakerFactory
                        .sendUnsupportedVersionResponse(ctx.channel());
            } else {
                handshaker.handshake(ctx.channel(), req);
            }
        }
        /**
         * 拒绝不合法的请求，并返回错误信息
         * */
        private static void sendHttpResponse(ChannelHandlerContext ctx,
                                             FullHttpRequest req, DefaultFullHttpResponse res) {
            // 返回应答给客户端
            if (res.status().code() != 200) {
                ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(),
                        CharsetUtil.UTF_8);
                res.content().writeBytes(buf);
                buf.release();
            }
            ChannelFuture f = ctx.channel().writeAndFlush(res);
            // 如果是非Keep-Alive，关闭连接
            if (!isKeepAlive(req) || res.status().code() != 200) {
                f.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    public static class ChannelSupervise {
        private   static ChannelGroup GlobalGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        private  static ConcurrentMap<String, ChannelId> ChannelMap=new ConcurrentHashMap<>();
        private   static void addChannel(Channel channel){
            GlobalGroup.add(channel);
            ChannelMap.put(channel.id().asShortText(),channel.id());
        }
        private static void removeChannel(Channel channel){
            GlobalGroup.remove(channel);
            ChannelMap.remove(channel.id().asShortText());
        }
        public static  Channel findChannel(String id){
            return GlobalGroup.find(ChannelMap.get(id));
        }
        private static void send2All(TextWebSocketFrame tws){
            GlobalGroup.writeAndFlush(tws);
        }
    }
}

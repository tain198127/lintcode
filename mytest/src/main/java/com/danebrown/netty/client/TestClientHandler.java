package com.danebrown.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.support.ExecutorServiceAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Scanner;

/**
 * Created by danebrown on 2021/7/1
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Log4j2
public class TestClientHandler extends ChannelInboundHandlerAdapter {
    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    /**
     * 通道激活时
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        threadPoolTaskExecutor.execute(()->{
            System.out.println("请输入要对服务端说的话:");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()){

                String msg = scanner.nextLine();
                ByteBuf encoded = ctx.alloc().buffer(4 * msg.length());
                encoded.writeBytes(msg.getBytes());
                ctx.write(encoded);
                ctx.flush();
            }
        });



    }

    /**
     * 读
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("SimpleClientHandler.channelRead");
        ByteBuf result = (ByteBuf) msg;
        byte[] result1 = new byte[result.readableBytes()];
        result.readBytes(result1);
        System.out.println("Server said:" + new String(result1));
        result.release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当出现异常就关闭连接
        log.error("TestClientHandler-->exceptionCaught:{}",cause);

        ctx.close();
    }
}

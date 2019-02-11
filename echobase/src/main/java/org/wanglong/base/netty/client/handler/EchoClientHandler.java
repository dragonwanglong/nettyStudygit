package org.wanglong.base.netty.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * 需要进行数据的读取操作，服务器端处理完成的数据信息会读取
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    private static int REPET = 500;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < REPET; i++) {
            System.out.println("********");
            byte[] byet = ("[" + i + "]" + "hello world").getBytes(CharsetUtil.UTF_8);
            ByteBuf sendBuf = Unpooled.buffer(byet.length);
            sendBuf.writeBytes(byet);
            ctx.writeAndFlush(sendBuf);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //只要服务器端发送完成信息之后，都会进行此方法进行内容的输出操作

        try {

            ByteBuf message = (ByteBuf) msg;
            String readMessage = message.toString(CharsetUtil.UTF_8);
            System.out.println(readMessage);
           /* if ("exit".equalsIgnoreCase(readMessage)) {
                //结束操作
                System.out.println("bye bye  结束本次传输");
            } else {*/
                /*System.out.println(readMessage);
                String string = InputUtil.getString("请输入要发送的消息");
                byte[] bytes = string.getBytes();
                ByteBuf sendBuf = Unpooled.buffer(bytes.length);
                sendBuf.writeBytes(bytes);//保存在缓存之中
                ctx.writeAndFlush(sendBuf);//将数据强制发送过去*/
            // }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            //资源释放
            ReferenceCountUtil.release(msg);
        }
    }


    //异常捕获
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

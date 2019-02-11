package org.wanglong.base.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

//处理echo的操作方式
//ChannelInboundHandler据输入的处理Adapter是针对数
//Netty是一种基于Nio开发框架的封装，和Aio欸有任何关系 Nio基于缓存操作
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当客户端成功连接成功之后，回进行该方法的调用，明确可以给客户端发送一些信息，
        byte[] bytes = "[服务器激活信息],连接通道已经创建，服务器开始激活交互".getBytes();
        //netty是基于缓存的操作，所以netty也提供了一些缓存类(封装了NIO中的BUFFER)
        //这是netty自己定义的缓存类
        ByteBuf buffer = Unpooled.buffer(bytes.length);
        buffer.writeBytes(bytes);//往缓存区中写入数据
        ctx.writeAndFlush(buffer);//强制性发送所有数据并刷新

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            //表示连接成功进行一些数据读取操作,对与读取操作完成后也可以直接响应
            //对于客户端发送来的信息 由于没有制定的数据类型  所以统一用object进行接收
            // ByteBuf buf = (ByteBuf) msg;
            //将字节缓冲区的内容转为字符串并进行编码指定
            String inData = msg.toString().trim();
            System.err.println(inData);
            //回应数据
            String echoData = "[服务器]" + inData + System.getProperty("line.separator");
            //进行连接断开，结束当前交互  exit 是来自客户端的内容  表示客户端的结束
            /*if ("exit".equalsIgnoreCase(inData)) {
                echoData = "exit";
            }*/
            //将回应内容转化为byte字节数组
            //byte[] echoDataBytes = echoData.getBytes();
            //开辟缓冲区
            //ByteBuf echoBuffer = Unpooled.buffer(echoDataBytes.length);
            //将数据写入缓存中
            //echoBuffer.writeBytes(echoDataBytes);
            ctx.writeAndFlush(echoData);
        } finally {
            //如果发生异常 失败  清除缓存
            ReferenceCountUtil.release(msg);
        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

package org.wanglong.base.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.wanglong.base.netty.client.handler.EchoClientHandler;
import org.wanglong.info.HostInfo;


public class EchoClient {
    public void run() throws Exception {
        //如果客户端不同，那么也可以使用多线程模式来处理
        //在netty中考虑到代码的统一性
        EventLoopGroup group = new NioEventLoopGroup();//创建一个线程池
        try {
            //创建客户端处理程序
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)//允许接收返回大块数据
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoClientHandler());//追加处理器 并且以后的开发也是追加处理器
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(HostInfo.HOST_NAME, HostInfo.PORT);
            channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("连接已经建立 可以连接");
                    }
                }
            });
            channelFuture.channel().closeFuture().sync();//等待关闭连接
        } finally {
            group.shutdownGracefully();//关闭线程池
        }
    }
}

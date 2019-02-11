package org.wanglong.base.netty.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.wanglong.base.netty.server.handler.EchoServerHandler;

//实现基础线程池与网络连接的配置项
public class EchoServer {

    //普通方法，作为服务器端的一个启动处理
    public void run(int port) throws Exception {
        //启动线程池 来提升服务器性能，利用定长的线程池可以确保核心线程的数量
        //在netty里面  线程的实现分为两类   主线程池（接收客户端连接）  工作线程池（处理客户端连接）
        EventLoopGroup mainExecutors = new NioEventLoopGroup(5);//创建接收线程池
        NioEventLoopGroup workExecutors = new NioEventLoopGroup(10);//创建工作线程池  处理客户端连接
        System.out.println("服务器启动成功" + port);
        //接下来就要进行服务器的启动
        //创建一个服务器端的程序类，进行NIO启动，同时可以设置channel
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();//服务器端
            //设置要使用的线程池 以及channel类型 Nio类型
            serverBootstrap.group(mainExecutors, workExecutors).channel(NioServerSocketChannel.class);
            //接收到连接之后要进行处理  所以这里定义子处理器
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {

                    socketChannel.pipeline().addLast(new EchoServerHandler());//追加处理器 并且以后的开发也是追加处理器

                }
            });
     /*   服务器端TCP内核模块维护有2个队列，我们称之为A，B吧
    客户端向服务端connect的时候，发送带有SYN标志的包（第一次握手）
    服务端收到客户端发来的SYN时，向客户端发送SYN ACK 确认(第二次握手)
     此时TCP内核模块把客户端连接加入到A队列中，然后服务器收到客户端发来的ACK时（第三次握手）
    TCP没和模块把客户端连接从A队列移到B队列，连接完成，应用程序的accept会返回
    也就是说accept从B队列中取出完成三次握手的连接
 
    A队列和B队列的长度之和是backlog,当A，B队列的长之和大于backlog时，新连接将会被TCP内核拒绝
 
    所以，如果backlog过小，可能会出现accept速度跟不上，A.B 队列满了，导致新客户端无法连接，
 
    要注意的是，backlog对程序支持的连接数并无影响，backlog影响的只是还没有被accept 取出的连接*/
            //下面就是利用一系列常量进行tcp协议的配置
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);//设置每个连接块大小
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);//设置为长连接模式
            //进行同步等待
            //ChannelFuture表示异步回调的处理操作
            ChannelFuture sync = serverBootstrap.bind(port).sync();
            sync.channel().closeFuture().sync();//等待socket关闭
        } finally {
            //关闭worker线程池
            workExecutors.shutdownGracefully();
        }
    }

}

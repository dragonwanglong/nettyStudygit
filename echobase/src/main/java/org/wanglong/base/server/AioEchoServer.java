package org.wanglong.base.server;

import org.wanglong.info.HostInfo;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;


class MyCompletionHandler implements CompletionHandler<AsynchronousServerSocketChannel, AioEchoThread> {


    @Override
    public void completed(AsynchronousServerSocketChannel channel, AioEchoThread aioEchoThread) {
        AsynchronousServerSocketChannel serverSocketChannel = aioEchoThread.getServerSocketChannel();
        //  serverSocketChannel.accept(aioEchoThread,this);//接接收连接
        ByteBuffer buffer = ByteBuffer.allocate(100);
    }

    @Override
    public void failed(Throwable exc, AioEchoThread attachment) {

    }
}

//设置一个单独的服务器处理线程
class AioEchoThread implements Runnable {
    //服务器通道
    private AsynchronousServerSocketChannel serverSocketChannel = null;

    private CountDownLatch countDownLatch;

    public AioEchoThread() throws Exception {
        this.countDownLatch = new CountDownLatch(1);//等待线程数量为1
        //打开服务器的通道
        this.serverSocketChannel = AsynchronousServerSocketChannel.open();
        this.serverSocketChannel.bind(new InetSocketAddress(HostInfo.PORT));
        System.out.println("服务器启动成功 监听" + HostInfo.PORT + "端口");
    }

    public AsynchronousServerSocketChannel getServerSocketChannel() {
        return serverSocketChannel;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }


    @Override
    public void run() {
        // this.serverSocketChannel.accept(this,new MyCompletionHandler());
    }
}

public class AioEchoServer {
}

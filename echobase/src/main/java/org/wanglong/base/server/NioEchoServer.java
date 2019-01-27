package org.wanglong.base.server;

import org.wanglong.info.HostInfo;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Nio服务端
 */
public class NioEchoServer {
    private static class EchoClientHandler implements Runnable {
        private SocketChannel clientChannel;//真正的客户端通道
        private boolean flag = true;//循环处理标记

        public EchoClientHandler(SocketChannel clientChannel) {
            this.clientChannel = clientChannel;
            //严格意义上来讲，当已经成功地连接上了服务器，并且需要处理之前，需要发送一些消息给客户端
        }

        @Override
        public void run() {
            //这里有一个缓冲区的概念，buffer有position  capacity() limit
            //buffer第一个缺点提供了一个过于底层的操作实现，并没有直接进行字符串的转换能力
            ByteBuffer buffer = ByteBuffer.allocate(50);//50个缓冲区
            try {
                while (this.flag) {
                    buffer.clear();
                    int read = this.clientChannel.read(buffer);
                    String recvMsg = new String(buffer.array(), 0, read).trim();
                    String writeMsg = "[Echo]" + recvMsg + "\n";//回应数据信息
                    if ("exit".equalsIgnoreCase(recvMsg)) {
                        writeMsg = "[Exit] 结束了";
                        this.flag = false;
                    }
                    //数据输入按缓存完成 而数据输出进行同样的缓存操作
                    buffer.clear();
                    buffer.put(writeMsg.getBytes());//发送内容
                    buffer.flip();//重置缓冲区
                    this.clientChannel.write(buffer);
                }
                this.clientChannel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        //Nio实现考虑到性能的问题，需要设置一个线程池，采用固定大小的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        //Nio的处理是通过channel控制的，所以要有一个Selector负责管理所有的channel
        ServerSocketChannel channel = ServerSocketChannel.open();
        //需要为其设置一个非阻塞的状态
        channel.configureBlocking(false);
        //服务器上需要提供一个绑定的网络端口
        channel.bind(new InetSocketAddress(HostInfo.PORT));
        //5 设置一个selector 作为一个选择器  目的管理所有的channel
        Selector selector = Selector.open();
        //把channel注册到selector当中并指定操作项为连接
        channel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.printf("服务器已经启动成功 监听端口为:" + HostInfo.PORT);
        //Nio采用的是轮询模式,每当有客户端发生连接的时候，就需要启动一个线程（被线程池管理）

        int keySelect = 0;//接受轮询状态
        while ((keySelect = selector.select()) > 0) {  //实现轮询处理
            Set<SelectionKey> keys = selector.selectedKeys();
            //获取全部的key
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                //获取每一个key
                SelectionKey next = iterator.next();
                if (next.isAcceptable()) {
                    //如果为连接模式
                    SocketChannel accept = channel.accept();
                    if (accept != null) {
                        executorService.submit(new EchoClientHandler(accept));
                    }
                }
                iterator.remove();
            }
        }
        executorService.shutdown();
        channel.close();
    }
}

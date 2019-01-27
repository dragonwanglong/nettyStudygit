package org.wanglong.base.client;

import org.wanglong.info.HostInfo;
import org.wanglong.utils.InputUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static java.nio.channels.SocketChannel.open;

//Nio客户端
public class NioEchoClient {


    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = open();//打开客户端连接通道
        socketChannel.connect(new InetSocketAddress(HostInfo.HOST_NAME, HostInfo.PORT));
        ByteBuffer allocate = ByteBuffer.allocate(50);
        boolean flag = true;
        while (flag) {
            allocate.clear();
            java.lang.String string = InputUtil.getString("请输入内容");
            allocate.put((string).getBytes());
            allocate.flip();
            socketChannel.write(allocate);
            allocate.clear();
            int readCount = socketChannel.read(allocate);
            allocate.flip();
            System.err.println(new java.lang.String(allocate.array(), 0, readCount));
            if ("exit".equalsIgnoreCase(string)) {
                flag = false;
            }
        }
        socketChannel.close();
    }
}

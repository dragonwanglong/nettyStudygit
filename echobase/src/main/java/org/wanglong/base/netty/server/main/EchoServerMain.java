package org.wanglong.base.netty.server.main;


import org.wanglong.base.netty.server.EchoServer;
import org.wanglong.info.HostInfo;

//server的启动主类
public class EchoServerMain {
    public static void main(String[] args) throws Exception {
        new EchoServer().run(HostInfo.PORT);
    }
}

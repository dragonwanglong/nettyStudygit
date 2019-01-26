package org.wanglong.base.server;

import org.wanglong.info.HostInfo;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioEchoServer {

    //处理线程
    static class EchoClientHandler implements Runnable {
        //每一个客户端都需要启动一个任务来执行
        private Socket client;
        private Scanner scanner;
        private PrintStream out;
        private boolean flag = true;//结束标记

        public EchoClientHandler(Socket client) {
            this.client = client;
            try {
                this.scanner = new Scanner(this.client.getInputStream());
                this.scanner.useDelimiter("\n");
                this.out = new PrintStream(this.client.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            while (this.flag) {
                if (this.scanner.hasNext()) {
                    String str = this.scanner.next().trim();
                    if ("exit".equalsIgnoreCase(str)) {
                        System.out.println("结束了");
                        this.flag = false;
                    } else {
                        this.out.println("[echo]:" + str);
                        System.out.println(flag);
                    }
                }
            }
            this.scanner.close();
            this.out.close();
            try {
                this.client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            boolean flag = true;
            ServerSocket serverSocket = new ServerSocket(HostInfo.PORT);
            System.out.println("服务端启动 监听" + HostInfo.PORT + "端口");
            ExecutorService service = Executors.newFixedThreadPool(5);
            while (flag) {
                Socket socket = serverSocket.accept();
                service.submit(new EchoClientHandler(socket));
            }
            service.shutdown();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

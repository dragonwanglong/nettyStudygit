package org.wanglong.base.client;

import org.wanglong.info.HostInfo;
import org.wanglong.utils.InputUtil;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class BioEchoClient {

    public static void main(String[] args) throws Exception {

        //定义绑定主机信息
        Socket client = new Socket(HostInfo.HOST_NAME, HostInfo.PORT);
        Scanner scan = new Scanner(client.getInputStream());
        scan.useDelimiter("\n");
        PrintStream out = new PrintStream(client.getOutputStream());
        boolean flag = true;

        while (flag) {
            String inputString = InputUtil.getString("请输入信息:");
            out.println(inputString);//把信息发送到服务器上
            if (scan.hasNext()) {
                //接受服务器的返回
                String trim = scan.next().trim();
                System.out.println(trim);
                if ("exit".equalsIgnoreCase(trim)) {
                    flag = false;
                }
            }

        }
        scan.close();
        out.close();
        client.close();
    }

}

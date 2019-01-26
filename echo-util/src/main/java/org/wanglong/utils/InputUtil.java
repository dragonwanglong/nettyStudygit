package org.wanglong.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputUtil {

    private static final BufferedReader KEYBOARD_INPUT = new BufferedReader(new InputStreamReader(System.in));

    public InputUtil() {
    }

    public static String getString(String prompt) throws IOException {
        boolean flag = true;//数据接收标记
        String str = null;
        while (flag) {
            System.out.println(prompt);
            str = KEYBOARD_INPUT.readLine();
            if (str == null || "".equals(str)) {
                System.out.println("数据输入有误，内容不允许为空!");
            } else {
                flag = false;
            }
        }
        return str;
    }
}

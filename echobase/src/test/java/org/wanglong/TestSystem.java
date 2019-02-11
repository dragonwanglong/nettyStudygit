package org.wanglong;

import java.util.Properties;

public class TestSystem {
    public static void main(String[] args) {
        Properties properties = System.getProperties();
        properties.list(System.out);
    }
}

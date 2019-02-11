package org.wanglong.base.netty.msgpack;


import org.wanglong.base.vo.Member;

import java.util.ArrayList;
import java.util.List;

public class MessagePackDemoA {

    public static void main(String[] args) {
        List<Member> allMember = new ArrayList<Member>();
        for (int i = 0; i < 10; i++) {
            Member member = new Member();
            member.setMid("mid");
            member.setSalary(1.2);
            member.setName("wanglong");
            member.setAge(123);
        }

    }
}

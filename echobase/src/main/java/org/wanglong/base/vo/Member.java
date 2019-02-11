package org.wanglong.base.vo;

import java.io.Serializable;

public class Member implements Serializable {


    private String mid;
    private String name;
    private int age;
    private Double salary;


    public Member() {
    }

    public Member(String mid, String name, int age, Double salary) {
        this.mid = mid;
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Member{" +
                "mid='" + mid + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", salary=" + salary +
                '}';
    }
}

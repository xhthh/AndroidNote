package com.xht.androidnote.module.kotlin.bean;

public class User {
    public User(String city, int name, int count, int restaurant, int ka, int wholesale, int industry, int other) {
        this.city = city;
        this.name = name;
        this.count = count;
        this.restaurant = restaurant;
        this.ka = ka;
        this.wholesale = wholesale;
        this.industry = industry;
        this.other = other;
    }

    //    name：版块名称，count：目标值，restaurant：餐饮数量，ka：KA数量，wholesale：流通批发数量，industry：工业加工数量，other：其它数量
    private String city;
    private int name;
    private int count;
    private int restaurant;
    private int ka;
    private int wholesale;
    private int industry;
    private int other;
}

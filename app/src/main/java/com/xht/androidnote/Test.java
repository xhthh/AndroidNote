package com.xht.androidnote;

/**
 * Created by xht on 2020/9/4
 */
public class Test {

    public static void main(String[] args) {
        // 具体调用
        Bean bean = new BeanSon();
        bean.commonHas();
    }

    public static int i_static = 123;

    static {
        i_static = 0;
        System.out.println(i_static); // 如果变量在后面定义出现非法前向引用报错
    }

}

// BeanSon
class BeanSon extends Bean {
    static {
        System.out.println("BeanSon static load");
    }

    BeanSon() {
        System.out.println("BeanSon load");
    }

    /*@Override
    void commonHas() {
        System.out.println("commonHas BeanSon");
    }*/

    void doSomething() {
        System.out.println("doSomething");
    }
}

// Bean
class Bean {
    static {
        System.out.println("Bean static load");
    }

    Bean() {
        System.out.println("Bean load");
    }

    void commonHas() {
        System.out.println("commonHas Bean");
    }
}


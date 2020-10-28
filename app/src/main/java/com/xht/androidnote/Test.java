package com.xht.androidnote;

import java.security.PrivateKey;
import java.security.PrivilegedActionException;

/**
 * Created by xht on 2020/9/4
 */
public class Test {

    public static void main(String[] args) {
        // 具体调用
        Bean bean = new BeanSon();
        bean.commonHas();
        //        bean.doSomething();//左编译，右运行，此处不能访问到子类独有的方法

        //        System.out.println(Sub.B);


    }

    public static int test(final Bean bean) {
//        n = 4;
        bean.num = 3;
        return 0;
    }

    class Inner {
//        private static int a = 0;
    }


    static class A {
        public static synchronized void test() {

        }
    }

    //    public static int i_static = 123;
    //
    //    static {
    //        i_static = 0;
    //        System.out.println(i_static); // 如果变量在后面定义出现非法前向引用报错
    //    }


    static class Parent {
        public static int A = 1;

        static {
            A = 2;
        }
    }

    static class Sub extends Parent {
        public static int B = A;
    }

}

interface TestInterface {
    static void get(){//jdk 1.8后可以有静态方法，和默认实现方法
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
    public int num = 0;
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


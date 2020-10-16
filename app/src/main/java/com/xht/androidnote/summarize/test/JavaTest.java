package com.xht.androidnote.summarize.test;

public class JavaTest {

    public static void main(String[] args) {

        //泛型类
        Container<String, String> stringContainer = new Container<>("name", "xht");
        Container<String, Integer> integerContainer = new Container<>("age", 18);

        //泛型方法
        out(stringContainer.getValue());
        out(integerContainer.getValue());
        System.out.println(stringContainer.getKey() + " ：" + stringContainer.getValue());
        System.out.println(integerContainer.getKey() + " ：" + integerContainer.getValue());

        B stature = new B("stature", 172);

        new Person<String>(233);

    }

    /**
     * 泛型方法
     *
     * @param t
     * @param <T>
     */
    public static <T> void out(T t) {
        System.out.println(t);
    }


    static class A extends Container {
        public A(Object key, Object value) {
            super(key, value);
        }
    }

    static class B extends Container<String, Integer> {
        public B(String key, Integer value) {
            super(key, value);
        }
    }


    public static class Person<E> {
        public <T> Person(T t) {
            System.out.println(t);
        }

    }

}

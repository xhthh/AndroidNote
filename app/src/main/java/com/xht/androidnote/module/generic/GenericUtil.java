package com.xht.androidnote.module.generic;

public class GenericUtil {
    /**
     * 作用阈
     * 其实这个声明类似于泛型声明，但是这个方法前面的声明只能作用在方法中。
     * @param p1
     * @param p2
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> boolean compare(Pair<K, V> p1, Pair<K, V> p2) {
        return p1.getKey().equals(p2.getKey()) &&
                p1.getValue().equals(p2.getValue());
    }
}

class Pair<K, V> {

    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}

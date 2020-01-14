package com.xht.androidnote.module.dsa.manhuasuanfa;

import java.util.HashMap;

/**
 * Created by xht on 2020/1/13.
 */
public class LruCache {


    private Node head;
    private Node end;
    //缓存存储上限
    private int limit;

    private HashMap<String, Node> hashMap;

    public LruCache(int limit) {
        this.limit = limit;
        hashMap = new HashMap<>();
    }

    public String get(String key) {
        Node node = hashMap.get(key);

        if (node == null) {
            return null;
        }

        refreshNode(node);

        return node.value;
    }

    public void put(String key, String value) {
        Node node = hashMap.get(key);

        if (node == null) {
            if (hashMap.size() >= limit) {
                String oldKey = removeNoe(head);
                hashMap.remove(oldKey);
            }

            node = new Node(key, value);
            addNode(node);
            hashMap.put(key, node);
        } else {
            node.value = value;
            refreshNode(node);
        }
    }

    /**
     * 刷新被访问的节点位置
     *
     * @param node 被访问的节点
     */
    private void refreshNode(Node node) {
        //如果被访问的节点是尾节点，则无需移动节点
        if (node == end) {
            return;
        }

        //移除节点
        removeNoe(node);
        //插入节点
        addNode(node);
    }

    /**
     * 尾部插入节点
     *
     * @param node 要插入的节点
     */
    private void addNode(Node node) {
        if (end != null) {
            end.next = node;
            node.pre = end;
            node.next = null;
        }

        end = node;
        if (head == null) {
            head = node;
        }
    }

    /**
     * 删除节点
     *
     * @param node 要删除的节点
     * @return
     */
    private String removeNoe(Node node) {
        //移除唯一的节点
        if (node == head && node == end) {
            head = null;
            end = null;
        } else if (node == head) {
            head = node.next;
            head.pre = null;
        } else if (node == end) {
            end = node.pre;
            end.next = null;
        } else {
            Node pre = node.pre;
            Node next = node.next;

            pre.next = next;
            next.pre = pre;
        }

        return node.key;
    }

    class Node {
        public Node(String key, String value) {
            this.key = key;
            this.value = value;
        }

        private Node pre;
        private Node next;
        private String key;
        private String value;
    }

    public static void main(String[] args) {
        LruCache lruCache = new LruCache(5);

        lruCache.put("001", " 用户1信息");
        lruCache.put("002", " 用户1信息");
        lruCache.put("003", " 用户1信息");
        lruCache.put("004", " 用户1信息");
        lruCache.put("005", " 用户1信息");
        lruCache.get("002");
        lruCache.put("004", " 用户2信息更新");
        lruCache.put("006", " 用户6信息");
        System.out.println(lruCache.get("001"));
        System.out.println(lruCache.get("006"));
    }

}

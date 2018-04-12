package com.danebrown.lintcode.lrucache;

import java.util.HashMap;

public class LRUCache {

    class CacheNode {
        int key;
        int value;
        CacheNode pre, next;

        public CacheNode(int key, int value) {
            this.key = key;
            this.value = value;
        }

        public CacheNode getPre() {
            return pre;
        }

        public void setPre(CacheNode pre) {
            this.pre = pre;
        }

        public CacheNode getNext() {
            return next;
        }

        public void setNext(CacheNode next) {
            this.next = next;
        }


        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }


    }

    private HashMap<Integer, CacheNode> innermp;

    public int getCapacity() {
        return capacity;
    }

    int capacity = 0;
    CacheNode head = null, end = null;

    /*
    * @param capacity: An integer
    */
    public LRUCache(int capacity) {
        // do intialization if necessary
        innermp = new HashMap<>(capacity);

        this.capacity = capacity;
    }

    private void remove(CacheNode node) {
        if (node.getPre() != null) {
            node.pre.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.pre = node.pre;
        } else {
            end = node.pre;
        }
    }


    private void setHead(CacheNode n) {
        n.next = head;
        n.pre = null;
        if (head != null) {
            head.pre = n;
        }
        head = n;
        if (end == null) {
            end = head;
        }
    }

    /*
     * @param key: An integer
     * @return: An integer
     */
    public int get(int key) {
        // write your code here

        if (innermp.containsKey(key)) {
            CacheNode n = innermp.get(key);
            remove(n);
            setHead(n);
            return n.getValue();
        }
        return -1;
    }

    /*
     * @param key: An integer
     * @param value: An integer
     * @return: nothing
     */
    public void set(int key, int value) {
        // write your code here
        if (innermp.containsKey(key)) {
            CacheNode old = innermp.get(key);
            old.value = value;
            remove(old);
            setHead(old);
        } else {
            CacheNode newNode = new CacheNode(key, value);
            if (innermp.size() >= capacity) {
                innermp.remove(end.key);
                remove(end);
                setHead(newNode);
            } else {
                setHead(newNode);
            }
            innermp.put(key, newNode);
        }

    }

}
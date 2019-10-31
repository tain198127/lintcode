/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.danebrown.lintcode.tree.redblacktree;

import java.util.Comparator;
import java.util.TreeMap;
import java.util.concurrent.UnderstandConcurrentHashMap;

/**
 * Created by DaneBrown on 27/04/2018.
 * Email:tain198127@163.com
 * All Right Reserved
 */
public final class RedBlackTree<K extends Comparator, V> {
    private ThreadLocal<K, V> chm;
    private TreeMap<K,V> tm;
    private volatile RBNode<K, V> root = null;
    //修改了多少次
    private volatile int modCount=0;
    //总共有多大
    private volatile int size=0;
    /**
     * 插入一个值
     *
     * @param key
     * @param value
     */
    public final void insert(K key, V value) {
        if (root == null) {
            synchronized (this) {
                root = new RBNode<>(null, key, value);
                //父节点都是黑的
                root.setColor(NodeColor.BLACK);
                this.size=1;
                this.modCount++;
            }
        } else {
            RBNode<K, V> node = new RBNode<>(key, value);
            //除了parent以外，所有新生成的节点都是红色的
            node.setColor(NodeColor.RED);
            RBNode<K, V> tmp = root;
            //开始插入
            while (true) {
                //值比tmp小
                if (node.getKey().compare(node.getKey(), tmp.getKey()) < 0) {
                    if (tmp.getLeft() == null) {
                        tmp.setLeft(node);
                        node.setParent(tmp);
                        break;
                    } else {
                        //否则左递归
                        tmp = tmp.getLeft();
                    }
                }
                else if (node.getKey().compare(node.getKey(), tmp.getKey()) >= 0){
                    //其key值大于等于右边的
                    if(tmp.getRight()==null){
                        tmp.setRight(node);
                        node.setParent(tmp);
                        break;
                    }
                    else{
                        tmp = tmp.getRight();
                    }
                }
            }//end whild
            //重置平衡
            reBalance(node);
        }// end else

    }

    /**
     * 重新平衡
     * @param node
     */
    private void reBalance(RBNode<K,V> node){

    }

    private void rotateLeft() {
    }

    private void rotateRight() {
    }

    /**
     * 根据Key值删除某个节点
     *
     * @param key
     */
    public void delete(K key) {

    }

    /**
     * 根据key值查找对应的v值
     *
     * @param key
     * @return
     */
    public V find(K key) {
        return null;
    }

}

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

import javax.xml.soap.Node;

/**
 * Created by DaneBrown on 27/04/2018.
 * Email:tain198127@163.com
 * All Right Reserved
 */
final class RBNode<K,V> {
    private RBNode left,right,parent;
    private NodeColor color;
    private K key;
    private V value;
    //当前节点孩子的数量
    private int childCount;
    //左孩子数量
    private int leftChildCount;
    //右孩子数量
    private int rightChildCount;
    //红孩子数量
    private int redChildCount;
    //黑孩子数量
    private int blackChildCount;
    /**
     * 要想获取左边红孩子的数量，可以遍历其左孩子，再计算左侧孩子中红孩子的数量，在计算左孩子是不是红色的；左黑，右红，右黑同该算法一致
     */


    public RBNode(RBNode parent, K key, V value){
        this.parent=parent;
        this.key=key;
        this.value=value;

    }
    public RBNode(K key,V value){
        this(null,key,value);
    }
    private RBNode(){}
    /**
     * 判断是否是最底层节点
     * @return
     */
    public boolean isLeaf(){
        return left==null && right ==null;
    }
    /**
     * 判断是否为root
     * @return
     */
    public boolean isRoot(){
        return parent == null;
    }
    /**
     * 左孩子
     * @return
     */
    public RBNode getLeft() {
        return left;
    }

    /**
     * 设置左孩子
     * @param left
     */
    public void setLeft(RBNode left) {
        this.left = left;
    }

    /**
     * 右孩子
     * @return
     */
    public RBNode getRight() {
        return right;
    }

    /**
     * 设置右孩子
     * @param right
     */
    public void setRight(RBNode right) {
        this.right = right;
    }

    /**
     * 获取父节点,如果是root节点的话，则返回空
     * @return
     */
    public RBNode getParent() {
        return parent;
    }

    /**
     * 设置父节点
     * @param parent
     */
    public void setParent(RBNode parent) {
        this.parent = parent;
    }

    /**
     * 获取颜色
     * @return
     */
    public NodeColor getColor() {
        return color;
    }

    /**
     * 设置颜色
     * @param color
     */
    public void setColor(NodeColor color) {
        this.color = color;
    }

    /**
     * 获取KEY值
     * @return
     */
    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    /**
     * 获取VALUE
     * @return
     */
    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    /**
     * 拿到所有孩子数量
     * @return
     */
    public int getChildCount() {
        return childCount;
    }

    /**
     * 获取左孩子数量
     * @return
     */
    public int getLeftChildCount() {
        return leftChildCount;
    }



    /**
     * 获取右孩子数量
     * @return
     */
    public int getRightChildCount() {
        return rightChildCount;
    }


    /**
     * 获取红孩子数量
     * @return
     */
    public int getRedChildCount() {
        return redChildCount;
    }


    /**
     * 获取黑孩子数量
     * @return
     */
    public int getBlackChildCount() {
        return blackChildCount;
    }

}

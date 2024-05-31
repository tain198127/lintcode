package com.danebrown.tree;

import java.util.ArrayList;
import java.util.List;

public class BPlusTree {
    private Node root;
    
    // 构造函数
    public BPlusTree() {
        root = null;
    }
    
    // 查找操作
    public String search(String key) {
        return search(root, key);
    }
    
    private String search(Node node, String key) {
        if (node == null) {
            return null;
        }
        
        if (node.isLeaf) {
            LeafNode leafNode = (LeafNode) node;
            return leafNode.search(key);
        } else {
            InternalNode internalNode = (InternalNode) node;
            if (key.compareTo(internalNode.keys.get(0)) < 0) {
                return search(internalNode.children.get(0), key);
            }
            for (int i = 1; i < internalNode.keys.size(); i++) {
                if (key.compareTo(internalNode.keys.get(i)) < 0) {
                    return search(internalNode.children.get(i), key);
                }
            }
            return search(internalNode.children.get(internalNode.keys.size()), key);
        }
    }
    
    // 插入操作（省略）
    
    // 删除操作（省略）
    
    // 节点抽象类
    private abstract class Node {
        boolean isLeaf;
    }
    
    // 叶子节点
    private class LeafNode extends Node {
        List<String> keys;
        List<String> values;
        
        public String search(String key) {
            int index = keys.indexOf(key);
            if (index != -1) {
                return values.get(index);
            } else {
                return null;
            }
        }
    }
    
    // 内部节点
    private class InternalNode extends Node {
        List<String> keys;
        List<Node> children;
    }
}

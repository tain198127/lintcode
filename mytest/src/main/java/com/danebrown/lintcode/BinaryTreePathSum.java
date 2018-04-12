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
package com.danebrown.lintcode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dane on 2017/8/7.
 */

/**
 * source:http://www.lintcode.com/zh-cn/problem/binary-tree-path-sum/
 */
public class BinaryTreePathSum {

    public class TreeNode {
        public int val;
        public TreeNode left, right;

        public TreeNode(int val) {
            this.val = val;
            this.left = this.right = null;
        }
    }

    private static List<List<Integer>> result = new ArrayList<>();

    /**
     * @param root   the root of binary tree
     * @param target an integer
     * @return all valid paths
     */
    public List<List<Integer>> binaryTreePathSum(TreeNode root, int target) {
        // Write your code here
        if (root == null)
            return new ArrayList<>();

        List<Integer> nodes = new ArrayList<>();

        //findPath(root,0,nodes,target);
        //return subPathSum(root,target);


        List<List<Integer>> rst = new ArrayList<List<Integer>>();
        if (root == null) {
            return rst;
        }
        dfs(rst, new ArrayList<Integer>(), root, 0, target);

        return rst;

        // return result;
    }

    public void dfs(List<List<Integer>> rst, ArrayList<Integer> list, TreeNode node, int add, int sum) {
        list.add(node.val);
        if (node.left == null && node.right == null) {
            if (add + node.val == sum) {
                rst.add(new ArrayList<Integer>(list));
            }
            return;
        }
        if (node.left != null) {
            dfs(rst, list, node.left, add + node.val, sum);
            list.remove(list.size() - 1);
        }
        if (node.right != null) {
            dfs(rst, list, node.right, add + node.val, sum);
            list.remove(list.size() - 1);
        }
    }

    private void findPath(TreeNode node, int size, List<Integer> nodes, int target) {
        if (node == null)
            return;
        nodes.add(size++, node.val);

        if (node.left == null && node.right == null) {

            int mark = 0;

            for (int i = 0; i < size; i++) {
                mark += nodes.get(i);
                //System.out.print(nodes.get(i)+",");
                if (mark == target) {
                    List<Integer> tmpRst = new ArrayList<>();
                    List<Integer> subList = nodes.subList(0, i + 1);

                    for (int j = 0; j < subList.size(); j++) {
                        tmpRst.add(subList.get(j));
                    }

                    result.add(tmpRst);


                    //System.out.println("find it");
                    break;
                } else if (mark > target) {
                    break;
                }

            }
            //System.out.println();

        } else {
            findPath(node.left, size, nodes, target);
            findPath(node.right, size, nodes, target);
        }
    }

}

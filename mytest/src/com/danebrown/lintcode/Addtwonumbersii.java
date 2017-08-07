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

/**
 * Created by dane on 2017/8/7.
 */

import java.util.Stack;

/**
 * source: http://www.lintcode.com/zh-cn/problem/add-two-numbers-ii/
 */

public class Addtwonumbersii {

    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
            next = null;
        }
    }

    /*
     * @param l1: The first list.
     * @param l2: The second list.
     * @return: the sum list of l1 and l2.
     */

    public ListNode addLists2(ListNode l1, ListNode l2) {
        // write your code here
        Stack<Integer> st1 = new Stack<Integer>();
        Stack<Integer> st2 = new Stack<Integer>();
        Stack<Integer> result = new Stack<Integer>();

        int append = 0;//进位
        while (l1 != null) {
            st1.push(l1.val);
            //System.out.println(l1.val);
            l1 = l1.next;
        }
        while (l2 != null) {
            st2.push(l2.val);
            //System.out.println(l2.val);
            l2 = l2.next;
        }

        while (!(st1.isEmpty() && st2.isEmpty() && append == 0)) {
            int num1 = 0;
            int num2 = 0;
            if (!st1.isEmpty()) {
                num1 = st1.pop();
            }
            if (!st2.isEmpty()) {
                num2 = st2.pop();
            }
            int rst = num1 + num2 + append;

            append = 0;
            if (rst >= 10) {
                append = 1;//进位
                rst = rst - 10;
            }
            //System.out.println(rst);
            result.push(rst);

        }
        ListNode ln = new ListNode(result.pop());
        ListNode copy = ln;
        while (!result.isEmpty()) {
            copy.next = new ListNode(result.pop());
            copy = copy.next;
        }
        return ln;


    }
}

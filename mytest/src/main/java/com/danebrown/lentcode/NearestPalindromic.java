package com.danebrown.lentcode;

import java.util.Collections;

/**
 * @author DaneBrown
 * email:tain198127@163.com
 * https://leetcode-cn.com/problems/find-the-closest-palindrome/
 * 给定一个整数 n ，你需要找到与它最近的回文数（不包括自身）。
 *
 * “最近的”定义为两个整数差的绝对值最小。
 *
 * 示例 1:
 *
 * 输入: "123"
 * 输出: "121"
 * 注意:
 *
 * n 是由字符串表示的正整数，其长度不超过18。
 * 如果有多个结果，返回最小的那个。
 **/
public class NearestPalindromic {
    public String nearestPalindromic(String n) {
        boolean isFin = false;
        if(n.length()/2 == 0){
            isFin = true;
        }
        String head = n.substring(0, n.length()/2);

        return "";
    }

    public static void main(String[] args) {
        NearestPalindromic nearestPalindromic = new NearestPalindromic();
        nearestPalindromic.nearestPalindromic("100");
    }
}

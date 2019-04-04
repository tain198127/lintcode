package com.danebrown.lentcode;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author DaneBrown
 * email:tain198127@163.com
 * https://leetcode-cn.com/problems/find-the-closest-palindrome/
 * 给定一个整数 n ，你需要找到与它最近的回文数（不包括自身）。
 * <p>
 * “最近的”定义为两个整数差的绝对值最小。
 * <p>
 * 示例 1:
 * <p>
 * 输入: "123"
 * 输出: "121"
 * 注意:
 * <p>
 * n 是由字符串表示的正整数，其长度不超过18。
 * 如果有多个结果，返回最小的那个。
 **/
public class NearestPalindromic {
    Gson gson = new Gson();

    public static void main(String[] args) {
        NearestPalindromic nearestPalindromic = new NearestPalindromic();
        long startTime=System.currentTimeMillis();
        String v= nearestPalindromic.nearestPalindromic("807045053224792883");
        long endTime=System.currentTimeMillis();
        System.out.println(v+"["+(endTime-startTime)+"ms]");
    }

    public String nearestPalindromic(String n) {

        long rstValue = findAllPalindromic(n);

        return String.valueOf(rstValue);
    }

    /**
     * 同时找上下的回文字，看谁先找到
     *
     * @param n
     * @return
     */
    private long findAllPalindromic(String n) {

        Long v = Long.valueOf(n);
        for(long i = 1; i < v; i++){
            boolean isDownPal = isPalindromic(v-i);
            if(isDownPal){
                return v-i;
            }
            boolean isUpPal = isPalindromic(v+i);
            if(isUpPal){
                return v+i;
            }

        }
        return 0;

    }

    /**
     * 判断是否为回文字
     * @param l
     * @return
     */
    private boolean isPalindromic(long l){
        long tmp = l;
        long tst = 0;
        while(tmp>0){
            tst = tst*10+tmp%10;
            tmp = tmp/10;
        }
        return (tst==l);
    }
}

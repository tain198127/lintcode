package com.danebrown.lentcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author DaneBrown
 * email:tain198127@163.com
 * 返回 A 的最短的非空连续子数组的长度，该子数组的和至少为 K 。
 *
 * 如果没有和至少为 K 的非空子数组，返回 -1 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：A = [1], K = 1
 * 输出：1
 * 示例 2：
 *
 * 输入：A = [1,2], K = 4
 * 输出：-1
 * 示例 3：
 *
 * 输入：A = [2,-1,2], K = 3
 * 输出：3
 *
 *
 * 提示：
 *
 * 1 <= A.length <= 50000
 * -10 ^ 5 <= A[i] <= 10 ^ 5
 * 1 <= K <= 10 ^ 9
 * https://leetcode-cn.com/problems/shortest-subarray-with-sum-at-least-k/
 **/
public class ShortestSubarray {
    List<Integer> current =new ArrayList<>();
    List<Integer> son =new ArrayList<>();
    List<Integer> grandson = new ArrayList<>();
    int level = 1;
    public int shortestSubarray_myversion(int[] A, int K) {
        current = new ArrayList<>(A.length);
        //初始化
        for(int i = 0; i < A.length;i++){
            if(A[i] >= K) {
                return level;
            }
            current.add(A[i]);
        }
        while (current.size()>1){
            level++;
            grandson = son;
            son = current;
            current = new ArrayList<>((son.size()/2)+1);
            for(int i =0; i < son.size()-1;i++){
                int v = son.get(i)+son.get(i+1)- (i+1>grandson.size()?0:grandson.get(i+1));
                if(v >= K){
                    return level;
                }
                current.add(v);
            }

        }

        return -1;
    }

    /**
     * 性能最高，要看明白
     * @param A
     * @param K
     * @return
     */
    public int shortestSubarray(final int[] A, int K) {
        int len = A.length;
        int sum = 0, begin = 0, res = -1;
        for (int i = 0; i < len; i++) {
            if (A[i] >= K) return 1;
            sum += A[i];
            if (sum < 1) {
                sum = 0;
                begin = i + 1;
                continue;
            }
            for (int j = i - 1; A[j + 1] < 0; j--) {
                A[j] = A[j + 1] + A[j];
                A[j + 1] = 0;
            }
            if (sum >= K) {
                while (sum - A[begin] >= K || A[begin] <= 0)
                    sum -= A[begin++];
                int length = i - begin + 1;
                if (res < 0 || res > length)
                    res = length;
            }
        }
        return res;
    }

    /**
     * [48,99,37,4,-31]
     * 140
     * @param args
     */
    public static void main(String[] args) {
        ShortestSubarray shortestSubarray = new ShortestSubarray();
        int[] A = {48,99,37,4,-31};
        int K = 140;
        int rst = shortestSubarray.shortestSubarray_myversion(A,K);
        System.out.println(rst);
    }

}

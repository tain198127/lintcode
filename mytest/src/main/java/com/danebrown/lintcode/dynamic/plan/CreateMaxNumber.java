package com.danebrown.lintcode.dynamic.plan;

import java.util.Arrays;
import java.util.Stack;

/**
 * @author DaneBrown
 * email:tain198127@163.com
 * @see <a href="https://www.lintcode.com/problem/create-maximum-number/description">create-maximum-number</a>
 * 给出两个长度分别是m和n的数组来表示两个大整数，数组的每个元素都是数字0-9。从这两个数组当中选出k个数字来创建一个最大数，
 * 其中k满足k <= m + n。选出来的数字在创建的最大数里面的位置必须和在原数组内的相对位置一致。返回k个数的数组。你应该尽可能的去优化算法的时间复杂度和空间复杂度。
 * <p>
 * 样例
 * 给出 nums1 = [3, 4, 6, 5], nums2 = [9, 1, 2, 5, 8, 3], k = 5
 * 返回 [9, 8, 6, 5, 3]
 * <p>
 * 给出 nums1 = [6, 7], nums2 = [6, 0, 4], k = 5
 * 返回 [6, 7, 6, 0, 4]
 * <p>
 * 给出 nums1 = [3, 9], nums2 = [8, 9], k = 3
 * 返回 [9, 8, 9]
 **/
public class CreateMaxNumber {
    public static void main(String[] args) {
        CreateMaxNumber createMaxNumber = new CreateMaxNumber();
        int[] num1 = new int[]{3, 4, 6, 5};
        int[] num2 = new int[]{9, 1, 2, 5, 8, 3};
        int k = 5;
        int[] rst = createMaxNumber.maxNumber(num1, num2, k);
        Arrays.stream(rst).forEach(item -> System.out.println(item));
    }

    /**
     * 找到可选范围内最大的数
     * @return
     */

    /**
     * @param nums1 : an integer array of length m with digits 0-9
     * @param nums2 : an integer array of length n with digits 0-9
     * @param k     : an integer and k <= m + n
     * @return: an integer array
     */
    public int[] maxNumber(int[] nums1, int[] nums2, int k) {
        Stack<Integer> stack = new Stack<>();
        int num1Idx = 0;
        int num2Idx = 0;
        int num = k;

        while (num1Idx < nums1.length || num2Idx < nums2.length) {

            int numToPut = Integer.MIN_VALUE;

            if(num1Idx < nums1.length && num2Idx < nums2.length){
                if (nums1[num1Idx] > nums2[num2Idx]) {
                    numToPut = nums1[num1Idx];
                    num1Idx++;
                } else {
                    numToPut = nums2[num2Idx];
                    num2Idx++;
                }
            }
            else{
                if(num1Idx>= nums1.length && num2Idx< nums2.length){
                    numToPut = nums2[num2Idx];
                    num2Idx++;
                }
                if(num2Idx >= nums2.length && num1Idx<nums1.length){
                    numToPut = nums1[num1Idx];
                    num1Idx++;
                }
            }

            if (!stack.isEmpty() && numToPut > stack.peek()) {
                stack.pop();
            }
            if(stack.size()<k) {
                stack.push(numToPut);
            }
            num--;
        }
        return stack.stream().mapToInt(Integer::intValue).toArray();

    }

    /**
     * 查找数组中前N个数中最大的数
     *
     * @param nums     数组
     * @param N        前N个
     * @param startIdx 开始地址
     * @return idx
     */
    private int findMaxNum(int[] nums, int startIdx, int N) {
        int times = 0;
        int maxIdx = startIdx;
        int maxNum = nums[startIdx];
        for (int i = startIdx; i < nums.length && times < N; i++, times++) {

            if (nums[i] > maxNum) {
                maxIdx = i;
                maxNum = nums[i];
            }
        }
        return maxIdx;
    }
}

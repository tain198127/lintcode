package com.danebrown.lentcode;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author DaneBrown
 * email:tain198127@163.com
 *
 * lentcode https://leetcode.com/problems/median-of-two-sorted-arrays/description/
 *
 * There are two sorted arrays nums1 and nums2 of size m and n respectively.
 *
 * Find the median of the two sorted arrays. The overall run time complexity should be O(log (m+n)).
 *
 * You may assume nums1 and nums2 cannot be both empty.
 * Example 1:
 *
 * nums1 = [1, 3]
 * nums2 = [2]
 *
 * The median is 2.0
 * Example 2:
 *
 * nums1 = [1, 2]
 * nums2 = [3, 4]
 *
 * The median is (2 + 3)/2 = 2.5
 **/
public class MedianofTwoSortedArrays {
    public static void main(String[] args) {
        MedianofTwoSortedArrays medianofTwoSortedArrays = new MedianofTwoSortedArrays();
        double d = medianofTwoSortedArrays.findMedianSortedArrays(new int[]{1,3},new int[]{2});
        System.out.println(d);
    }
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        double result = 0;
        List<Integer> list1 = new LinkedList<>();
        for (int i: nums1
             ) {
            list1.add(i);
        }
        List<Integer> list2 = new LinkedList<>();
        for (int i:nums2
             ) {
            list2.add(i);
        }
        list1.addAll(list2);
        Collections.sort(list1);
        int l = list1.size();
        if(l%2 == 0){
            int pos1 =  l/2-1;
            int pos2 = l/2;
            int pos1Value= list1.get(pos1);
            int pos2Value = list1.get(pos2);
            result = (double)(pos1Value+pos2Value)/2d;

        }
        else{
            int pos = (int)Math.ceil(l/2);
            result = list1.get(pos);

        }
        return result;
    }

}

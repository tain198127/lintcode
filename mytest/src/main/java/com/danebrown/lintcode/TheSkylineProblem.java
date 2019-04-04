package com.danebrown.lintcode;

import java.util.List;

/**
 * @author DaneBrown
 * email:tain198127@163.com
 * 水平面上有 N 座大楼，每座大楼都是矩阵的形状，可以用一个三元组表示 (start, end, height)，分别代表其在x轴上的起点，终点和高度。大楼之间从远处看可能会重叠，求出 N 座大楼的外轮廓线。
 *
 * 外轮廓线的表示方法为若干三元组，每个三元组包含三个数字 (start, end, height)，代表这段轮廓的起始位置，终止位置和高度。
 * 样例
 * 给出三座大楼：
 *
 * [
 *   [1, 3, 3],
 *   [2, 4, 4],
 *   [5, 6, 1]
 * ]
 * 外轮廓线为：
 *
 * [
 *   [1, 2, 3],
 *   [2, 4, 4],
 *   [5, 6, 1]
 * ]
 **/
public class TheSkylineProblem {
    /**
     * @param buildings: A list of lists of integers
     * @return: Find the outline of those buildings
     * int[] 分别表示start,end,height
     */
    public List<List<Integer>> buildingOutline(int[][] buildings) throws Exception {
        // write your code here
        throw new Exception("尚未实现");
    }

    public static void main(String[] args) throws Exception {
        TheSkylineProblem theSkylineProblem = new TheSkylineProblem();
        theSkylineProblem.buildingOutline(new int[][]{{1,3,3},{2,4,4},{5,6,1}});
    }
}

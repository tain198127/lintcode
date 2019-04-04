package com.danebrown.lentcode;

/**
 * @author DaneBrown
 * email:tain198127@163.com
 * https://leetcode-cn.com/articles/climbing-stairs/
 * <p>
 * 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
 * <p>
 * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
 * <p>
 * 注意：给定 n 是一个正整数。
 * <p>
 * 示例 1：
 * <p>
 * 输入： 2
 * 输出： 2
 * 解释： 有两种方法可以爬到楼顶。
 * 1.  1 阶 + 1 阶
 * 2.  2 阶
 * 示例 2：
 * <p>
 * 输入： 3
 * 输出： 3
 * 解释： 有三种方法可以爬到楼顶。
 * 1.  1 阶 + 1 阶 + 1 阶
 * 2.  1 阶 + 2 阶
 * 3.  2 阶 + 1 阶
 **/
public class ClimbingStairs {
    int[] mem = null;
    int i = 0;

    public static void main(String[] args) {
        ClimbingStairs climbingStairs = new ClimbingStairs();
        int ways = climbingStairs.climbStairsByMem(44);
        System.out.println(ways);
    }

    /**
     * 使用斐波那契数列递归方式，最笨的方法
     *
     * @param n
     * @return
     */
    public int climbStairs(int n) {
        if (n == 1) {
            return 1;
        } else if (n == 2) {
            return 2;
        } else {
            int preSteps = climbStairs(n - 1);
            int prepreSteps = climbStairs(n - 2);
            return prepreSteps + preSteps;
        }
    }

    /**
     * 动态规划的方式
     *
     * @param n
     * @return
     */
    public int climbStairsByDP(int n) {
        int[] steps = new int[n + 1];
        if (n == 1) {
            return 1;
        }
        steps[1] = 1;
        steps[2] = 2;
        for (int i = 3; i <= n; i++) {
            steps[i] = steps[i - 1] + steps[i - 2];
        }
        return steps[n];
    }

    /**
     * 记忆化递归
     *
     * @param n
     * @return
     */
    public int climbStairsByMem(int n) {
        //当前步数
        int currentStep = 0;
        //前一步
        int preStep = 0;
        //前两步
        int preTwoStep = 0;
        if (n == 1) {
            return 1;
        } else if (n == 2) {
            return 2;
        }
        preTwoStep = 1;
        preStep = 2;
        for (int i = 3; i <= n; i++) {
            currentStep = preStep + preTwoStep;
            preTwoStep = preStep;
            preStep = currentStep;
        }
        return currentStep;
    }

}

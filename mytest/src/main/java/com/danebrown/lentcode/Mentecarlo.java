package com.danebrown.lentcode;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author DaneBrown
 * email:tain198127@163.com
 **/
public class Mentecarlo {
    /**
     * 原理：
     * n表示要生成的点的个数。
     * 园的面积=πR^2
     * 正方形的面积=(2R)^2
     * 园和正方形的面积的比例；=4π
     * 所以 π=4*园和正方形的面积的比例
     * 无数个点，可以组成面积。用n的数量，表示面积
     * @param n 点的个数
     * @return
     */
    static double MontePI(int n) {
        double PI;
        double x, y;
        int i, sum;
        sum = 0;
        for (i = 1; i < n; i++) {
            x = ThreadLocalRandom.current().nextDouble(-1,1);
            y = ThreadLocalRandom.current().nextDouble(-1,1);
            if ((x * x + y * y) <= 1) {
                sum++;

            }

        }
        PI = 4.0 * sum / n;
        return PI;

    }

    public static void main(String[] args) {
        int n;
        double PI;
        System.out.println("蒙特卡洛概率算法计算圆周率:");
        Scanner input = new Scanner(System.in);
        System.out.println("输入点的数量：");
        n = input.nextInt();
        PI = MontePI(n);
        System.out.println("PI="+PI);

    }
}

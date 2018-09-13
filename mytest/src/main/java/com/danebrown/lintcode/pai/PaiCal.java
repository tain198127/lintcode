package com.danebrown.lintcode.pai;

import java.util.Scanner;

public class PaiCal {
    public static void main(String[] args) {
        double y = 1.0;
        Scanner read = new Scanner(System.in);
        System.out.println("请输入割圆次数：");
        int n = read.nextInt();

        cut(n);


    }

    public static void cut(int n) {
        double y = 1.0;
        for (int i = 0; i < n; i++) {
            double π = 3 * Math.pow(2, i) * y;
            System.out.println("第" + i + "次切割,为正" + (6 + 6 * i) + "边形，圆周率π≈" + π);
            y = Math.sqrt(2 - Math.sqrt(4 - y * y));
        }
    }
}

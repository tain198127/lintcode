package com.danebrown.lentcode;

import java.util.Scanner;

/**
 * @author DaneBrown
 * email:tain198127@163.com
 **/
public class DFSDataTower {
    public static void main(String[] args) {
        System.out.print("输入数组的层数: ");
        Scanner scan=new Scanner(System.in);
        int n=scan.nextInt();//定义数塔层数n;
        int d[][]=new int[n][n];
        System.out.print("输入数组元素：");
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<n;j++)
            {
                if(i>=j)
                    d[i][j]=scan.nextInt();
            }
        }
        int result = dataTower(d);
    }
    public static int dataTower(int tower[][])
    {
        int heigh = tower.length;//数塔高度
        int len = tower[heigh - 1].length;//数塔底部宽度
        int [][] resultTower = new int[heigh][len];//结果数塔，存放路径数值和
        int [][] path = new int[heigh][len];//计算结果数塔生成路径

        //初始化结果数塔
        for(int i = 0; i < len; i++)
        {
            resultTower[heigh - 1][i] = tower[heigh - 1][i];
        }
        for(int i = heigh - 2; i >= 0; i--)
        {
            for(int j = 0; j <= i; j++)
            {
                if(resultTower[i + 1][j] > resultTower[i + 1][j + 1])
                {
                    resultTower[i][j] = tower[i][j] + resultTower[i + 1][j];
                    path[i][j] = j;
                }else
                {
                    resultTower[i][j] = tower[i][j] + resultTower[i + 1][j + 1];
                    path[i][j] = j + 1;
                }
            }
        }

        //打印路径
        System.out.println("最大数值和为" + resultTower[0][0] + "\n");
        System.out.print("路径为：" + tower[0][0]);
        int j = path[0][0];
        for(int i = 1; i <= heigh - 1; i++){
            System.out.print("->" + tower[i][j]);
            j = path[i][j];
        }
        System.out.println();

        return resultTower[0][0];
    }

}

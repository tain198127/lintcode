package com.danebrown.lentcode;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author DaneBrown
 * email:tain198127@163.com
 **/
public class DynamicProgramingMinCoinCombin {
    //cent
    /**
     * 原unit。
     * 如果是分，可以是1，2，5。
     * 如果是毛，可以是1，2，5分
     * 如果是园，可以是1，2，5元
     * 如果是10元，可以是10，20，50元
     * 如果是百元，不用动态规划
     */
    private static final int unit[] = new int[]{1,2,5};

    public static void main(String[] args) {
        DynamicProgramingMinCoinCombin dynamicProgramingMinCoinCombin = new DynamicProgramingMinCoinCombin();
        int times = dynamicProgramingMinCoinCombin.countMinCoin(8,null);
        System.out.println(times);
    }

    /**
     * 计算最小组合
     * @param money 目标金额，单位是分
     * @param outCoinSquence 输出，硬币的顺序
     * @return 次数
     */
    public int countMinCoin(int money, ArrayList<Integer> outCoinSquence){
        //记录金额
        int dp[] = new int[money+1];
        //假定目标金额，全部都用1(分/角/元/拾元)来计算
        Arrays.fill(dp,1);
        dp[0] = 0;//第一位记做0；
        //每一位做循环
        for(int i = 1; i<=money;i++){
            //循环扫描元unit中的每个单位
            for(int j = 0; j <unit.length;j++){
                if(i>= unit[j] || dp[i-unit[j]]+1 < dp[i]){
                    dp[i] = dp[i-unit[j]+1];
                }
            }
        }
        Arrays.stream(dp).forEach(item->System.out.println(item));
        return dp[money];

    }
}

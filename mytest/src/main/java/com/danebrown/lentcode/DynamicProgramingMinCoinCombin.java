package com.danebrown.lentcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
//    private static final int unit[] = new int[]{0,1,2,5,10,20,50,100,200,500,1000,2000,5000,100000};
    private static final int unit[] = new int[]{0,1,2,5};

    public static void main(String[] args) {
        DynamicProgramingMinCoinCombin dynamicProgramingMinCoinCombin = new DynamicProgramingMinCoinCombin();
//        int times = dynamicProgramingMinCoinCombin.countMinCoin(8,null);

        List<Integer> output = new ArrayList<>();
        int times = dynamicProgramingMinCoinCombin.countMinCoin_My(10,output);
        System.out.println(times);
    }


    /**
     * 计算最小组合
     * @param money 目标金额
     * @param outCoinSquence 输出，硬币的顺序
     * @return 次数
     */
    public int countMinCoin_My(int money, List<Integer> outCoinSquence){
        int wegiht_unit[] = Arrays.stream(unit).skip(2).toArray();
        //
        /**
         * 状态转移变量
         * 例如：
         * 1->1  BASE LV=1
         * 2->1  BASE LV=2
         * 3—>2+1->2 LV=2
         * 4->2+2->2 LV=2
         * 5->1  BASE LV=5
         * 6->5+1->2 LV=5
         * 7->5+2——>2 转移 LV=5
         * 8->7+1|5+2+1->3 LV=5
         * 9->5+2+2->3 转移 LV=5
         * 10->5+5->2 变化
         */
        Map<Integer,Integer> status = new HashMap<>();

        //金额、次数
        List<Integer> list = new ArrayList<>(money);
        int coin=0;
//        Collections.fill(list,0);
        //如果没找到，就按最多次数算。
        //算法复杂度N
        for(int i =0; i <= money; i++){
            list.add(i);
        }
        //最后一次转移的时候的值
        int lastTransValue = 0;
        //算法复杂度i*j
        for(int i = 1; i <= money; i++){
            int finalI = i;
            //状态转移[如果属于原始unit，直接设置为1]
            if(Arrays.stream(unit).anyMatch(item->item == finalI)){
                list.set(i,1);
                lastTransValue = i;
            }
            else{
                //最后一次转移值和现值的差
                int gap =i - lastTransValue;
                //如果这个差能在原始unit中找到(除了0和1以外)，那么直接在上一次转移值次数上加1
                if(Arrays.stream(wegiht_unit).anyMatch(item->item == gap)){
                    list.set(i, list.get(lastTransValue) + 1);
                    //作为新的转移值
                    lastTransValue = i;
                }
                else {
                    //判断"前一个次数+1"，和"上一次转移值的次数"+"转移差的次数"。哪个大，就设定为几；
//                    int min = Math.min(list.get(i - 1) + 1, list.get(lastTransValue)+list.get(gap));
//                    list.set(i, min);

                    //别人的算法
                    for(int j = 0; j < unit.length;j++){
                        if(i>unit[j] && list.get(i-unit[j])+1<list.get(i)){
                            list.set(i,list.get(i-unit[j])+1);
                        }
                    }

                }
            }

        }
        for(int i = 0; i < list.size();i++){
            outCoinSquence.add(list.get(i));
        }
        System.out.println("最后转移状态"+lastTransValue);
        return list.get(money);
    }


}

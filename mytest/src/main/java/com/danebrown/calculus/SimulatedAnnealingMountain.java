package com.danebrown.calculus;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by danebrown on 2021/2/13
 * mail: tain198127@163.com
 * 模拟退火算法-GD-SA，用途：寻找最值，一般是找最大值，如果是找最小值则取反
 * @author danebrown
 */
@Slf4j
public class SimulatedAnnealingMountain {
    private int K = 1000;//迭代次数
    private int T = 1000;//初始温度
    private double Tmin=1e-8;//退火结束温度
    private double A = 0.98d;//退火系数


    private static int initSize = 6553500;
    private int[] mountain = new int[initSize];
    private ThreadLocalRandom random = ThreadLocalRandom.current();
    public SimulatedAnnealingMountain(){
        for (int i = 0; i < initSize; i++) {
            mountain[i] =random.nextInt();
        }

    }

    public int getMax(){
        return Arrays.stream(mountain).max().getAsInt();
    }



    /**
     * 找到局部最大值的爬山算法，被优化的函数
     * @param idx 随机初始位置
     * @return 最大值位置
     */
    private int findMax(int idx){
        int currentIdx = idx;
        if(currentIdx <=0 || currentIdx+1>= initSize-1)
            return currentIdx;
        while (mountain[currentIdx]<mountain[currentIdx+1]){
            currentIdx++;
        }
        return mountain[currentIdx];
    }
    public int sa(int idx){
        double currentT = T;//当前温度
        List<Integer> eachX = new ArrayList<>(K);//用来保存每次的结果
        List<Integer> eachResult = new ArrayList<>(K);

        while (currentT > Tmin){
            //每次退火结果中得到的最值
            int maxResultPerLoop  = Integer.MIN_VALUE;
            //每次退火结果中最大值的INDEX
            int maxIndexPerLoop = -1;
            //K是每个温度系数下，迭代次数
            for (int i=0;i<K;i++){
                //随便选个点
                int tmpIdx = random.nextInt(0,initSize);
                //找到这个点附近的最大值
                int tmpMax = findMax(tmpIdx);

                //计算邻域的X，重点
                int nextX =
                        (int) Math.ceil(tmpIdx + (random.nextDouble(-1d,1d)-1)*(currentT/T)*K/2);
                //防止溢出，进行环形取模
                nextX = nextX%initSize-1;
//                log.debug("nextX:{}",nextX);
                //根据邻域X的点再次找到最大值
                int nextMax = findMax(nextX);
                //对比本域和邻域的值，如果邻域值大，则接受
                if(nextMax > tmpMax){
                    maxResultPerLoop = nextMax;
                    maxIndexPerLoop = nextX;
                }
                else{
                    //如果邻域值比本域值小，则根据当前温度，计算接受的概率函数，重点
                    double p = 1/(1+Math.exp(-(nextMax-tmpMax)/currentT));
//                    log.debug("sa概率为{}",p);
                    //如果扔色子的结果大于某个随机数，则接受
                    if(random.nextDouble() < p){
                        maxResultPerLoop = nextMax;
                        maxIndexPerLoop = nextX;
                    }
                }
            }
            //没经过一次K循环，则把每次退火结果中最值的INDEX和最值放在数组中
            eachX.add(maxIndexPerLoop);
            eachResult.add(maxResultPerLoop);
            //计算退火算法，重点
            currentT = currentT*A;
        }
        //经过多次退火以后找到最值
        return eachResult.stream().max(Integer::compareTo).get();
    }

    /**
     * 这种方法更稳定
     * @return
     */
    public int sa1(){
        double currentT = T;//当前温度
        int[] eachX = new int[K];//用来保存每次的idx;
        int[] eachResult = new int[K];//用来保存每次的结果
        //初始化结果
        for (int i = 0; i < eachX.length; i++) {
            eachX[i] = random.nextInt(0,initSize);
            eachResult[i] = findMax(eachX[i]);
        }
        while (currentT > Tmin){
            for (int i=0;i<K;i++){
                int tmpMax = findMax(eachX[i]);
                int nextX =
                        (int) Math.ceil(eachX[i] + (random.nextDouble(-1d,1d)-1)*(currentT/T)*K/2);
                int nextMax = findMax(nextX);
                if(nextMax > tmpMax){
                    eachX[i] = nextX;
                    eachResult[i] = nextMax;
                }
                else{
                    double p = 1/(1+Math.exp(-(nextMax-tmpMax)/currentT));
//                    log.debug("sa概率为{}",p);
                    if(p > random.nextDouble()){
                        eachX[i] = nextX;
                        eachResult[i] = nextMax;
                    }

                }
            }
            currentT = currentT*A;
        }

        return Arrays.stream(eachResult).max().getAsInt();

    }

    public static void main(String[] args) {

        SimulatedAnnealingMountain sam = new SimulatedAnnealingMountain();
        int maxVal = sam.sa(ThreadLocalRandom.current().nextInt(1,
                initSize-1));
        int maxVal1 = sam.sa1();
        System.out.println("模拟退火算法得出的结果:"+maxVal);
        System.out.println("模拟退火算法1得出的结果:"+maxVal1);
        System.out.println("系统算法得出的结果:"+sam.getMax());
    }

}

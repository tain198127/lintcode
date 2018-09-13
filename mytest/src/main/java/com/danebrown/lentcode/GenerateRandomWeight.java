package com.danebrown.lentcode;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author DaneBrown
 * email:tain198127@163.com
 * 用于产生一簇随机数
 **/
public class GenerateRandomWeight {
    public static void main(String[] args) {
        GenerateRandomWeight generateRandomWeight = new GenerateRandomWeight();
        double[][] doubles =  generateRandomWeight.generate(3,10000,0.01);
        print(doubles);
    }
    public static void print(double[][] weightes){
        for(int i = 0; i < weightes.length; i++){
            System.out.println();
            for (int j = 0; j < weightes[i].length;j++){
                System.out.print(String.format("%f,",weightes[i][j]));
            }
        }
    }
    /**
     * 生成一簇权重的随机数，Sum(Wi) = 1, {Wi} 属于[0,1]
     * @param weightCount 要生成的权重的数量，跟业务有关
     * @param times 生成的条数，每条中含有一组权重，条数越多，用于计算的效果越好，相应的计算量也更大
     * @param precision 精度，精度越高，后续计算的效果越好，相应的计算量也越大,默认为0.01
     * @return 一簇随机数
     */
    public double[][] generate(int weightCount, int times, double precision){
        double[][] result = new double[times][weightCount];
        for(int i = 0; i < times; i++){
            result[i] = generateRow(weightCount,precision);
        }
        return result;
    }
    private double[] generateRow(int weightCount, double precision){
        List<Double> result = new ArrayList<>();
        double costWight = 0d;
        int costTimes = 0;
        while (costTimes < weightCount && costWight < 1){
            costTimes++;
            //剩余概率
            double maxRange = 1-costWight;
            //为了保证Sum(Wi)=1，所以最后一位是剩余的概率
            if(costTimes == weightCount){
                result.add(maxRange);
                costWight = costWight + maxRange;
                break;
            }

            double rdm = ThreadLocalRandom.current().nextDouble(0,maxRange);
            costWight = costWight + rdm;
            result.add(rdm);
        }

        return result.stream().mapToDouble(Double::doubleValue).toArray();
    }

}

package com.danebrown.ml;

import lombok.extern.log4j.Log4j2;
import com.google.common.math.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * 文档缺失熵
 * 目的，是为了计算一篇文章缺页后，剩余的价值是否能够理解。
 * 假设：一篇文章约靠前价值越大，越靠后价值越低
 */
@Log4j2
public class DocLossEntropy {
    /**
     * 计算是否有价值
     * @param loosPages true表示缺页，false表示不缺页，顺序表示页码
     * @return
     */
    public static boolean lossEntropyEnvaluate(Integer[] loosPages){
        int pages = loosPages.length;
        int allPageSum = sumAllPages(pages);
        List<Double> entropy = new ArrayList<>(loosPages.length);
        for(int i = 1;i<loosPages.length+1;i++){
            double singleEntropy = -DoubleMath.log2((double) i/(double)allPageSum);
            entropy.add(singleEntropy);
        }
        List<Double> normalizedEntrop = normalization(entropy);
        double lossEntropySum = 0;
        for(int i = 0; i < normalizedEntrop.size();i++){
            if(loosPages[i]>0){
                lossEntropySum+= normalizedEntrop.get(i);
            }
            if(lossEntropySum >= 1){
                return false;
            }
        }
        return true;
    }

    /**
     * 归一化
     * @param list
     * @return
     */
    private static List<Double> normalization(List<Double> list){
        List<Double> normalizedEntrop = new ArrayList<>(list.size());
        double min = Collections.min(list);
        double max = Collections.max(list);
        double mid = max - min;
        for(int i = 0; i < list.size();i++){
            normalizedEntrop.add((list.get(i)-min)/(mid));
        }
        return normalizedEntrop;
    }
    private static int sumAllPages(int pages){
        int mid = 0;
        //表示偶数页
        if(pages%2 == 0){

        }
        else{//表示奇数页
            mid = Math.floorMod(pages,2);
        }
        int sum = (1+pages)*(pages/2)+mid;
        return sum;
    }
    public static void test(int length){
        List<Integer> list = new ArrayList<>(length);
        for(int i = 0; i < length; i++){
            list.add(ThreadLocalRandom.current().nextBoolean()?1:0);
        }
        Integer[] test = list.toArray(new Integer[0]);
        long begin = System.nanoTime();
        boolean isAvaliable = lossEntropyEnvaluate(test);
        long span = (System.nanoTime() - begin);
        log.info("测试长度 {} 耗时 {} 纳秒  结果 {} 内容 [{}] ",length,span,isAvaliable,test);
    }
    public static void main(String[] args) {
        for(int i = 5;i < 50;i++){
            test(i);
        }
//        DocLossEntropy docLossEntropy = new DocLossEntropy();
//        Integer[] test = {0,0,0,0,0,0,0,0,0,1};
//        boolean isAvaliable = docLossEntropy.lossEntropyEnvaluate(test);
//        System.out.println(String.format("是否还有价值？[%s]",isAvaliable));
//
//        Integer[] test1 = {1,0,0,0,0,0,0,0,0,0,0};
//        isAvaliable = docLossEntropy.lossEntropyEnvaluate(test1);
//        System.out.println(String.format("是否还有价值？[%s]",isAvaliable));
//
//        Integer[] test2 = {0,1,1,0,0,0,0,0,0,0,0};
//        isAvaliable = docLossEntropy.lossEntropyEnvaluate(test2);
//        System.out.println(String.format("是否还有价值？[%s]",isAvaliable));
//
//        Integer[] test3 = {0,0,0,0,1,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0};
//        isAvaliable = docLossEntropy.lossEntropyEnvaluate(test3);
//        System.out.println(String.format("是否还有价值？[%s],长度为:%S",isAvaliable,test3.length));
//
//        Integer[] test4 = {0,0,0,0,1,1,0,0,0,0,0,0,1,0,0,};
//        isAvaliable = docLossEntropy.lossEntropyEnvaluate(test4);
//        System.out.println(String.format("是否还有价值？[%s],长度为:%S",isAvaliable,test4.length));
    }
}

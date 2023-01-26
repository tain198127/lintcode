package com.danebrown.calculus;

import com.google.common.primitives.Doubles;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created by danebrown on 2021/2/1
 * mail: tain198127@163.com
 * 中心极限定理，验证方法如下：
 * Step1: 生成P个随机数，放入initRandomList
 * Step2: 从initRandomList中挑选M个样本，然后计算样本的均值，将样本均值（点值）放入result列表
 * Step3: 重复Step2 N次，将形成一个N个元素的列表 result
 * Step4: 计算Step3输出的result 的均值 means，和方差 var
 * Step5: 计算initRandomList的均值 means1和方差var1
 * step6: 比较means和means1，以及var*N(样本均值空间的大小) 和var1
 * Step7: 会发现可以通过局部推算整体的均值和方差
 *
 * @author danebrown
 */
@Slf4j
public class CentralLimitTheorem {
    private List<Double> initRandomList = new ArrayList<>();

    private CentralLimitTheorem() {

    }

    public CentralLimitTheorem(int size) {
        for (int i = 0; i < size; i++) {
            initRandomList.add((double) ThreadLocalRandom.current().nextInt(0, size));
        }
    }

    public static void main(String[] args) {
        CentralLimitTheorem centralLimitTheorem = new CentralLimitTheorem(100000);
        //均值对数器
        Double means = centralLimitTheorem.means(centralLimitTheorem.initRandomList);
        //方差对数器
        Double var = centralLimitTheorem.var(centralLimitTheorem.initRandomList);
        //中心基线定理算法
        List<Double> sample = centralLimitTheorem.sampling(10000, 1000);
        //排序
        List<Double> sortedSample = sample.stream().sorted().collect(Collectors.toList());
        log.info("结果:{}", sortedSample);
        //样本均值
        Double sampleMeans = centralLimitTheorem.means(sample);
        //样本方差
        Double sampleVar = centralLimitTheorem.var(sample) * 10000;
        System.out.printf("整体均值:%f，整体方差:%f\n",means,var);
        log.info("对数器的整体均值:{}，整体方差:{}", means, var);
        System.out.printf("样本均值:%f，样本方差:%f\n",sampleMeans,sampleVar);
        log.info("样本均值:{}，样本方差:{}", sampleMeans, sampleVar);
        Double meansAccurRate =  Math.abs(sampleMeans - means)/means;
        Double varAccurRate = Math.abs(sampleVar-var)/var;
        System.out.printf("样本均值损失率%f, 样本方差损失率:%f\n", meansAccurRate,varAccurRate);

    }

    /**
     * 抽样
     *
     * @param size  每次抽样的数据量
     * @param times 抽样次数
     * @return 所有抽样的均值
     */
    public List<Double> sampling(int size, int times) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            List<Double> samplePerTimes = new ArrayList<>(size);
            for (int j = 0; j < size; j++) {
                Integer idx = ThreadLocalRandom.current().nextInt(0, initRandomList.size());
                samplePerTimes.add(initRandomList.get(idx));
            }
            //每完成一次，将结果求均值，放入result

            OptionalDouble average = Arrays.stream(Doubles.toArray(samplePerTimes)).average();


            if (average.isPresent()) {
                result.add(average.getAsDouble());
            }
        }
        return result;
    }

    /**
     * 求均值
     *
     * @param list
     * @return
     */
    public Double means(List<Double> list) {
        OptionalDouble average = Arrays.stream(Doubles.toArray(list)).average();
        return average.getAsDouble();
    }

    /**
     * 求方差
     *
     * @param list
     * @return
     */
    public Double var(List<Double> list) {
        Double mean = means(list);
        Optional<Double> optionalDouble = list.stream().map(o1 -> {
            return Math.pow(o1 - mean, 2);
        }).reduce((o1, o2) -> o1 + o2);
        return optionalDouble.get() / (list.size() - 1);

    }


}

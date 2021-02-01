package com.danebrown.calculus;

import com.google.common.primitives.Doubles;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created by danebrown on 2021/2/1
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Slf4j
public class CentralLimitTheorem {
    private List<Double> integerList = new ArrayList<>();

    private CentralLimitTheorem() {

    }

    public CentralLimitTheorem(int size) {
        for (int i = 0; i < size; i++) {
            integerList.add((double) ThreadLocalRandom.current().nextInt(0, size));
        }
    }

    public static void main(String[] args) {
        CentralLimitTheorem centralLimitTheorem = new CentralLimitTheorem(100000);

        Double means = centralLimitTheorem.means(centralLimitTheorem.integerList);

        Double var = centralLimitTheorem.var(centralLimitTheorem.integerList);

        List<Double> sample = centralLimitTheorem.sampling(10000, 1000);

        List<Double> sortedSample = sample.stream().sorted().collect(Collectors.toList());
        log.info("结果:{}", sortedSample);
        Double sampleMeans = centralLimitTheorem.means(sample);

        Double sampleVar = centralLimitTheorem.var(sample) * 1000;
        log.info("整体均值:{}，整体方差:{}", means, var);

        log.info("样本均值:{}，样本方差:{}", sampleMeans, sampleVar);
        //        log.info("整体均值:{}",sampleMeans);

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
                Integer idx = ThreadLocalRandom.current().nextInt(0, integerList.size());
                samplePerTimes.add(integerList.get(idx));
            }
            //每完成一次，将结果求均值，放入result

            OptionalDouble average = Arrays.stream(Doubles.toArray(samplePerTimes)).average();


            if (average.isPresent()) {
                result.add(average.getAsDouble());
            }
        }
        return result;
    }

    public Double means(List<Double> list) {
        OptionalDouble average = Arrays.stream(Doubles.toArray(list)).average();
        return average.getAsDouble();
    }

    public Double var(List<Double> list) {
        Double mean = means(list);
        Optional<Double> optionalDouble = list.stream().map(o1 -> {
            return Math.pow(o1 - mean, 2);
        }).reduce((o1, o2) -> o1 + o2);
        return optionalDouble.get() / (list.size() - 1);

    }


}

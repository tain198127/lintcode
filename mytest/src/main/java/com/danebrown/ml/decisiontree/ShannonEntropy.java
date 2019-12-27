package com.danebrown.ml.decisiontree;

import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: java
 * @author: DaneBrown
 * @create: 2019-11-15 17:04
 * @version: 1.0.0
 * email:        tain198127@163.com
 * Github:       https://github.com/tain198127
 * 香农熵计算方法，from 机器学习实战第三章决策树
 **/
@Log4j2
public class ShannonEntropy {
    public static Double calculateShannonEnt(List<String> categories) {
        Double len = Double.valueOf(categories.size());
        log.debug("len is {}", len);
        Map<String, Double> labelCount = new HashMap<>();
        categories.stream().forEach(item -> {
            if (!labelCount.containsKey(item)) {
                labelCount.put(item, 0.0);
            }

            labelCount.put(item, labelCount.get(item) + 1.0);

        });
        double result = 0.0;
        for (Map.Entry<String, Double> entry : labelCount.entrySet()) {
            String type = entry.getKey();
            Double count = entry.getValue();
            log.debug("{} count is {}", type, count);
            Double prob = (count / len);
            result -= prob * (Math.log(prob) / Math.log(2));

        }
        return result;
    }

    public static void main(String[] args) {
        List<String> test = Arrays.asList("yes", "yes", "no", "no", "no");
        double se = ShannonEntropy.calculateShannonEnt(test);
        System.out.println(se);
    }
}

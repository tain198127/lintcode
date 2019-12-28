package com.danebrown.ml.logistic;

import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Logistic分类器
 *
 * @program: mytest
 * @author: DaneBrown
 * @create: 2019-12-27 17:45
 * @version: 1.0.0
 * email:        tain198127@163.com
 * Github:       https://github.com/tain198127
 **/
@Log4j2
public class Logistic {
    /**
     * sigmoid计算
     *
     * @param x 值
     * @return y值
     */
    public Double sigmoid(Double x) {
        return 1 / (1 + Math.exp(-x));
    }

    /**
     * 随机梯度上升算法
     *
     * @param dataset      数据集合
     * @param recycle      循环次数
     * @param diffAccuracy 计算精度，步长
     */
    public List<Double> randomGradAscent(List<LogisticDataSet<Double>> dataset, Integer recycle, Double diffAccuracy) {

        ThreadLocalRandom random = ThreadLocalRandom.current();
        List<Double> Y = dataset.stream().map(item -> item.getLabel()).collect(Collectors.toList());
        List<Double> targetX = new ArrayList<>(dataset.get(0).getData().size()+1);
        dataset.get(0).getData().stream().forEach(item->{
            targetX.add(1d);
        });
        for (int i = 0; i < recycle; i++) {
            for (int j = 0; j < dataset.size(); j++) {
                Double deltaX = (double) (4 / (i + j + 1)) + diffAccuracy;
                int randomIndex = random.nextInt(0, dataset.size() - 1);

                List<Double> randomRaw = dataset.get(randomIndex).getData();
                Double x = 0d;
                for(int k = 0; k < randomRaw.size(); k++){
                    x += randomRaw.get(k)* targetX.get(k);
                }
                Double h = sigmoid(x);
                Double deltaY = dataset.get(randomIndex).getLabel() - h;
                //weights = weights + (error * alpha) * array(data_mat_in[randIndex])
                //targetX[I] = targetX[I]+deltaX*deltaY*
                for (int k = 0; k < randomRaw.size();k++){
                    targetX.set(k, targetX.get(k)+ deltaX * deltaY * randomRaw.get(k));
                }
                log.debug("第 {} 次训练的 第{}条程序集 训练的权重是 {}",i,j,targetX);
            }
        }
        return targetX;
    }

    public Double classfy(LogisticDataSet<Double> dataset, List<Double> trainWeight) throws Exception {
        boolean result;
        if(dataset.getData().size() != trainWeight.size()){
            throw new Exception("检车集合与向量集合长度不符");
        }
        Double x = 0d;
        for(int i = 0; i < dataset.getData().size(); i++){
            x += dataset.getData().get(i)* trainWeight.get(i);
        }
        return this.sigmoid(x) > 0.5d?1.0d:0.0d;

    }
    public static List<LogisticDataSet<Double>> readData(List<String> doc){
        List<LogisticDataSet<Double>> result = new ArrayList<>();
        result = doc.parallelStream().map(item->{
            List<Double> row = Lists.newArrayList(item.split("\t"))
                    .stream()
                    .map(str->Double.parseDouble(str.trim())).collect(Collectors.toList());
            LogisticDataSet<Double> element = new LogisticDataSet<>();
            element.setLabel(row.get(row.size()-1));
            element.setData(row.subList(0,row.size()-1));

            return element;


        }).collect(Collectors.toList());
        return result;
    }
    public static List<LogisticDataSet<Double>> readData(String path) throws FileNotFoundException {

        List<String> doc = new ArrayList<>();
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))){

            String line = null;
            while ((line = bufferedReader.readLine()) != null){
                doc.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readData(doc);


    }
    public BigDecimal test(List<LogisticDataSet<Double>> trainData,List<LogisticDataSet<Double>> test) throws FileNotFoundException {
        AtomicInteger errorCount = new AtomicInteger();
        int testDataSize = test.size();


        List<Double> train_mat = randomGradAscent(trainData,5000,0.0001d);
        log.info("train is {}", train_mat);

        test.stream().forEachOrdered(item->{
            try {
                Double result = classfy(item, train_mat);
                errorCount.addAndGet(result.equals(item.getLabel()) ? 0 : 1);
                log.debug("{} is {}",item, result == 1.0d?"有病":"没病");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        BigDecimal floatErrorCount = new BigDecimal(errorCount.get());
        BigDecimal errorRate = floatErrorCount.divide(new BigDecimal(testDataSize), 4, BigDecimal.ROUND_HALF_UP);
        return errorRate;
    }
    public BigDecimal multi_test(int times) throws FileNotFoundException {
        List<LogisticDataSet<Double>> trainData = Logistic.readData( this.getClass().getResource("/horseColicTraining.txt").getPath());
        log.debug(trainData.size());

        List<LogisticDataSet<Double>> test = Logistic.readData(this.getClass().getResource("/horseColicTest.txt").getPath());
        log.debug("test size is :{}",test.size());

        BigDecimal errorCount =new BigDecimal(0);
        for(int i = 0;i< times; i++){
            errorCount = errorCount.add(test(trainData,test));

        }
        return errorCount.divide(new BigDecimal(times) , 8, BigDecimal.ROUND_HALF_UP);

    }
    public static void main(String[] args) throws FileNotFoundException {
        Logistic logistic = new Logistic();
        int times = 10;
        BigDecimal errorRate = logistic.multi_test(times);
        log.info("{}次平均错误率是{}",times,errorRate);

    }


}

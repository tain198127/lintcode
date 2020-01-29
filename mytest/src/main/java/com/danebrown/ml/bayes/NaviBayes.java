package com.danebrown.ml.bayes;


import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


public class NaviBayes {


    /**
     * 获取去重后的词
     *
     * @param bayesData
     * @return
     */
    private List<String> createVocablist(BayesData bayesData) {
        Set<String> vocabset = new HashSet<>();
        for (String[] line : bayesData.getDataSet()
        ) {
            for (String word : line
            ) {
                vocabset.add(word);
            }

        }
        return vocabset.stream().sorted().collect(Collectors.toList());
    }

    /**
     * 词变向量
     *
     * @param vocablist 去重后的词向量
     * @param row       原始句子
     * @return 词向量
     */
    private List<Integer> word2vector(List<String> vocablist, String[] row) {
        List<Integer> vector = vocablist.stream().map(item->{return 0;}).collect(Collectors.toList());

        for (String word : row
        ) {
            if (vocablist.stream().anyMatch(item -> item.equals(word))) {
                vector.set(vocablist.indexOf(word), 1);
            }

        }
        return vector;

    }

    private TrainData trainNbo(List<List<Integer>> vectors, int[] labels) {
        int rows = vectors.size();
        int columns = vectors.get(0).size();
        float p_abusize = Arrays.stream(labels).sum() / (float) rows;
        List<Integer> p0Vec = vectors.get(0).stream().map(item->1).collect(Collectors.toList());
        List<Integer> p1Vec = vectors.get(0).stream().map(item->1).collect(Collectors.toList());

        AtomicReference<Float> p0Denom =new AtomicReference<>(2.0f);
        AtomicReference<Float> p1Denom = new AtomicReference<>(2.0f);
        for (int i = 0; i < rows; i++) {
            if (labels[i] == 1) {
                List<Integer> v = vectors.get(i);
                for (int j = 0; j < p1Vec.size(); j++) {
                    p1Vec.set(j, p1Vec.get(j) + v.get(j));
                }
                v.stream().forEach(item -> {
                    p1Denom.updateAndGet(v1 -> new Float((float) (v1 + item)));
                });
            } else {
                List<Integer> v = vectors.get(i);
                for (int j = 0; j < p0Vec.size(); j++) {
                    p0Vec.set(j, p0Vec.get(j) + v.get(j));
                }
                v.stream().forEach(item -> {
                    p0Denom.updateAndGet(v1 -> new Float((float) (v1 + item)));
                });
            }
        }
        List<Double> p0list = p0Vec.stream().map(item->{return Math.log((float)item/p1Denom.get());}).collect(Collectors.toList());
        List<Double> p1list = p1Vec.stream().map(item->{return Math.log(item/p1Denom.get());}).collect(Collectors.toList());
        TrainData trainData = new TrainData(p0list, p1list, p_abusize);
        return trainData;
    }
    private boolean classify(List<Integer> forClassify,TrainData trainData){
        double p0sum = Math.log(trainData.getPAbuse());
        double p1sum = (1-Math.log(trainData.getPAbuse()));
        for(int i =0 ;i < forClassify.size();i++){
            p1sum += forClassify.get(i)*trainData.getP1Vec().get(i);
        }
        for(int i = 0;i< forClassify.size();i++){
            p0sum += forClassify.get(i)*trainData.getP0Vec().get(i);
        }
       return p1sum>p0sum;

    }

    public void test(String[] testEntity) {
        BayesData bayesData = new BayesData();
        List<String> vocabSet = this.createVocablist(bayesData);
        List<List<Integer>> vector = new ArrayList<>();
        for (String[] line : bayesData.getDataSet()) {
            List<Integer> rowVector = this.word2vector(vocabSet, line);
            vector.add(rowVector);
        }
        TrainData trainData = this.trainNbo(vector, bayesData.getLabel());

        List<Integer> testVector = this.word2vector(vocabSet, testEntity);
        boolean isbadwords = classify(testVector, trainData);
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(testEntity).forEachOrdered(item->{stringBuilder.append(item);stringBuilder.append(" ");});
        System.out.println(String.format("%s is bad word? %s",stringBuilder.toString(),isbadwords));


    }

    public static void main(String[] args) {
        NaviBayes naviBayes = new NaviBayes();
        String[] testEntity ={"love","my","dalmation"};
        naviBayes.test(testEntity);

        String[] testEntity1 ={"stupid","garbage"};
        naviBayes.test(testEntity1);
    }
}

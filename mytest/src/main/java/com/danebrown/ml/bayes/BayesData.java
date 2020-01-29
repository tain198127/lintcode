package com.danebrown.ml.bayes;

import lombok.AllArgsConstructor;
import lombok.Data;

public class BayesData {
    private String[][] dataSet;
    private int[] label;
    public String[][] getDataSet(){
        return dataSet;
    }
    public int[] getLabel(){
        return label;
    }
    public BayesData(){
        String[][] initData = {{"my", "dog", "has", "flea", "problems", "help", "please"},
                {"maybe", "not", "take", "him", "to", "dog", "park", "stupid"},
                {"my", "dalmation", "is", "so", "cute", "I", "love", "him"},
                {"stop", "posting", "stupid", "worthless", "garbage"},
                {"mr", "licks", "ate", "my", "steak", "how", "to", "stop", "him"},
                {"quit", "buying", "worthless", "dog", "food", "stupid"}};
        int[] initLabel ={0, 1, 0, 1, 0, 1};
        this.dataSet = initData;
        this.label = initLabel;
    }
}

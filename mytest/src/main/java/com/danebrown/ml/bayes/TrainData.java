package com.danebrown.ml.bayes;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class TrainData {
    private List<Double> p0Vec;
    private List<Double> p1Vec;
    private float pAbuse;
}

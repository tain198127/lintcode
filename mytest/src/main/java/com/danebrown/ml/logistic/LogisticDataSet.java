package com.danebrown.ml.logistic;

import lombok.Data;

import java.util.List;

/**
 * @program: mytest
 * @author: DaneBrown
 * @create: 2019-12-27 18:03
 * @version: 1.0.0
 * email:        tain198127@163.com
 * Github:       https://github.com/tain198127
 **/
@Data
public class LogisticDataSet<T> {
    Double label;
    List<T> data;
}

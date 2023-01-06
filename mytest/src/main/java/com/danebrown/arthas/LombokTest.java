package com.danebrown.arthas;

import lombok.Builder;
import lombok.ToString;

/**
 * Created by danebrown on 2021/12/17
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
public class LombokTest {
    @Builder
    @ToString
    public static class BuilderTest{
        private boolean isB = true;
        private int val = -1;
        private String msg = "";
    }
    public static void main(String[] args) {
        BuilderTest bt =  BuilderTest.builder().msg("msg").build();
        System.out.println(bt.toString());   
    }
}

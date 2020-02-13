package com.danebrown.calculus;

import lombok.extern.log4j.Log4j2;
import sun.swing.BeanInfoUtils;

import java.math.MathContext;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 牛顿法
 */
@Log4j2
public class NewtonsMethod {
    public interface NewtonsCallback<S>{
        S invoke(S in);//原函数
        S grad(S in);//一阶导数
    }

    /**
     * 牛顿法
     * @param seed
     * @param task
     * @return
     */
    public static double newtonsMethod(double seed,  NewtonsCallback<Double> task){
        double x0;
        int i = 0;
        do {

            x0 = seed;

            double seedHat = task.invoke(x0);//原函数
            double seedGradHat = task.grad(x0);//一阶导数
            seed = x0 - (seedHat/seedGradHat);
            log.debug("原函数是:{},一阶导数是:{},牛顿值是:{},gaps:{}",seedHat, seedGradHat,seed,Math.abs(seed-x0));
            i++;


        }
        while ( Math.abs(seed-x0)>1e-5);
        log.info("执行了{}次",i);
        return seed;

    }
    public static double sqrt(double x){
//        double a=1,b=4,c=9,d=4;
        double result = NewtonsMethod.newtonsMethod(ThreadLocalRandom.current().nextDouble(), new NewtonsCallback<Double>() {


            @Override
            public Double invoke(Double in) {
                //x^2+4x+4 = 0
                return in*in*in-x;
            }

            @Override
            public Double grad(Double in) {
                return 3*in*in;
            }
        });
        return result;
    }
    public static void main(String[] args) {
        long begin = System.currentTimeMillis();
        double result = NewtonsMethod.sqrt(8d);
        long end = System.currentTimeMillis();

        System.out.println(String.format("结果是%2f，用时%d毫秒",result, end-begin));

         begin = System.currentTimeMillis();
         result =Math.pow(8d,(1/3));
         end = System.currentTimeMillis();

        System.out.println(String.format("系统算法：结果是%2f，用时%d毫秒",result, end-begin));
    }
}

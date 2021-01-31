package com.danebrown.calculus;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 牛顿法
 * Xn+1 = Xn - f(Xn)/f'(Xn)
 *
 */
@Log4j2
public class NewtonsMethod {
    public interface NewtonsCallback<S> {
        S invoke(S in);//原函数

        S grad(S in);//一阶导数
    }

    /**
     * 牛顿法
     *
     * @param seed 随机数，种子
     * @param task
     * @return
     */
    public static double newtonsMethod(double seed, NewtonsCallback<Double> task) {
        double x0;
        int i = 0;
        do {

            x0 = seed;

            double seedHat = task.invoke(x0);//原函数
            double seedGradHat = task.grad(x0);//一阶导数，也就是斜率
            //gap = x-[f(x)/f'(x)]
            //当gap与x值之间足够小时，就已经找到了那个值
            seed = x0 - (seedHat / seedGradHat);
            //如果gap
            log.debug("原函数是:{},一阶导数是:{},牛顿值是:{},gaps:{}", seedHat, seedGradHat, seed, Math.abs(seed - x0));
            i++;


        }
        while (Math.abs(seed - x0) > 1e-5);
        log.info("执行了{}次", i);
        return seed;

    }

    /**
     * 求开方
     * @param Y 被求的值。例如 X的开立方，则X就是8 ，结果就是2
     * @return
     */
    public static double sqrt(double Y) {

        double result = NewtonsMethod.newtonsMethod(
                ThreadLocalRandom.current().nextDouble(),
                new NewtonsCallback<Double>() {

            /**
             * 原函数表达式，例如，Y=X^2 ，为了表达方便，一般会写成X^2 -Y。这里要求的是当Y=X^2时，X的值
             * @param x 表示迭代中的值
             * @return
             */
            @Override
            public Double invoke(Double x) {
                /**
                 *
                 */
                return Math.pow(x,2) -2 - Y;
            }

            /**
             * 一阶函数表达式
             * @param x
             * @return
             */
            @Override
            public Double grad(Double x) {
                return 2 * x;
            }
        });
        return result;
    }

    public static void main(String[] args) {
        long begin = System.currentTimeMillis();
        //求16的平方根，这里的原公式应该是16=X^2，所以，输入的16是Y，resourt就是要求的X
        //开平方的反函数是平方，也就是说X的二次方等于16，要求X的值
        double result = NewtonsMethod.sqrt(16d);
        long end = System.currentTimeMillis();

        System.out.println(String.format("结果是%2f，用时%d毫秒", result, end - begin));

        begin = System.currentTimeMillis();
        result = Math.sqrt(16);
        end = System.currentTimeMillis();

        System.out.println(String.format("系统算法：结果是%2f，用时%d毫秒", result, end - begin));
    }
}

package com.danebrown.baseline;

import com.google.common.base.Stopwatch;
import com.google.common.base.Ticker;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * Created by danebrown on 2020/9/8
 * mail: tain198127@163.com
 */
public class FmtTest {
    public static void main(String[] args) {
        Stopwatch stopwatch = Stopwatch.createStarted(Ticker.systemTicker());
        //预热
        String.format("第%d次",-1);

        long spend = 0;
        int init = 10000;
        int j = 0;
        for (int i = 0; i < init; i++){
            String.format("第%d次",i);
            j = i;
        }
        stopwatch.stop();
        spend = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        System.out.println(j+1+"次执行；fmt总耗时："+spend+";平均耗时："+(double)spend/(j+1));

        stopwatch.reset();
        stopwatch.start();
        for (int i = 0; i < init; i++){
            String tms = "第"+i+"次";
            j = i;
        }
        spend = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        System.out.println(j+1+"次字符串相加总耗时："+spend+";平均耗时："+(double)spend/(j+1));

        MessageFormat.format("","111");
        System.out.println("结论：string的format比字符串相加要慢20倍");
    }


}

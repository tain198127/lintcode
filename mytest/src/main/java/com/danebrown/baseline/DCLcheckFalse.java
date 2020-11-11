package com.danebrown.baseline;


import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by danebrown on 2020/11/10
 * mail: tain198127@163.com
 * 模拟java 单例时，没使用DCL的后果，可能产生多个对象；
 */
@Log4j2
public class DCLcheckFalse {
    private  static DCLcheckFalse instance;
    private long rdm;

    private DCLcheckFalse() {
        rdm = ThreadLocalRandom.current().nextLong();
        System.out.println(
                rdm+"--"+System.identityHashCode(this)+"--"+this.hashCode()
        );
    }

    public static DCLcheckFalse getInstance() {
        if (instance == null) {
                    instance = new DCLcheckFalse();
        }
        return instance;
    }

    public static void main(String[] args) {
        int threadNum = 100;
        CountDownLatch cdlStart = new CountDownLatch(0);
        CountDownLatch cldEnd = new CountDownLatch(threadNum);

        for (int i =0;i <threadNum ;i++){
            Thread thread = new Thread(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                   try{
                       DCLcheckFalse.getInstance();
                       cdlStart.await();
                   }
                   catch (InterruptedException ex){
                       log.error("{}",ex.getMessage());
                   }
                   finally {
                       cldEnd.countDown();
                   }

                }
            });
            thread.start();

        }
        cdlStart.countDown();
        try {
            cldEnd.await();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        finally {

        }

    }


}

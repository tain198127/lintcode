package com.danebrown.baseline;


import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by danebrown on 2020/11/10
 * mail: tain198127@163.com
 * double check lock
 * 模拟java 单例时，使用DCL的后果，正确的使用方法
 */
@Log4j2
public class DCLcheckRight {
    private volatile static DCLcheckRight instance;
    @Autowired
    private long rdm;

    private DCLcheckRight() {
        rdm = ThreadLocalRandom.current().nextLong();
        System.out.println(
                rdm+"--"+System.identityHashCode(this)+"--"+this.hashCode()
        );
    }

    public static DCLcheckRight getInstance() {
        if (instance == null) {
            synchronized (DCLcheckFalse.class){
                if(instance == null){
                    instance = new DCLcheckRight();
                }
            }
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
                       DCLcheckRight.getInstance();
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

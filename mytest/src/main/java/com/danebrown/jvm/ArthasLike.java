package com.danebrown.jvm;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.threadly.concurrent.collections.ConcurrentArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongUnaryOperator;

/**
 * Created by danebrown on 2021/4/1
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Slf4j
public class ArthasLike {

    public void hashTest(String val){
        int v = Math.abs(val.hashCode()%100)+1;
        System.out.println(v);
    }
    @SneakyThrows
    public static void main(String[] args) {

        ArthasLike arthasLike = new ArthasLike();
        arthasLike.hashTest("12345");
//        AtomicLong atomicLong = new AtomicLong(0);
//
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        CountDownLatch countDownLatch = new CountDownLatch(100);
//        Map<Long, ConcurrentArrayList<Long>> map = new HashMap<>();
//        for (Long i=0l;i< 10 ;i++){
//            map.put(i,new ConcurrentArrayList<>());
//        }
//        for(int i=0;i<100;i++){
//            executorService.submit(() -> {
//                long v = atomicLong.updateAndGet(operand -> {
//                    countDownLatch.countDown();
//                    if(operand >= 9){
//                        return 0;
//                    }
//                    return operand+1;
//                });
//                System.out.println(v);
//                map.get(v).add(v);
//
//            });
//        }
//        countDownLatch.await();
//        while (!executorService.isTerminated()){
//            if(!executorService.awaitTermination(3,TimeUnit.SECONDS)){
//                executorService.shutdownNow();
//            }
//        }
//        log.info("结果：{}",map);


    }
}

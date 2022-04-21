package com.danebrown.thread;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import io.netty.util.concurrent.FastThreadLocalThread;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
@Log4j2
public class CFwithDiscardPool {
    public static void main(String[] args) {
        List<CompletableFuture<String>> completableFutureList =
                new ArrayList<>();
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.setCorePoolSize(1);
        poolTaskExecutor.setThreadNamePrefix("msg_resending-thread-");
        poolTaskExecutor.setMaxPoolSize(2);
        poolTaskExecutor.setKeepAliveSeconds(3);
        poolTaskExecutor.setDaemon(false);
        poolTaskExecutor.setQueueCapacity(2);
        poolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        poolTaskExecutor.setAwaitTerminationSeconds(60);
        //DiscardPolicy 或者 DiscardOldestPolicy策略，配合CompleteFuture的时候，如果用了
        //CompletableFuture.allOf 当future数量 远超过
        // future中队列的数量时，这个过程会夯住，永远不会再执行下去了。
        //这里配合completefuture allOf 配合DiscardOldestPolicy 或者DiscardPolicy就会有问题
        // 如果使用AbortPolicy 或者其他拒绝策略也OK
        poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        poolTaskExecutor.initialize();

        for(int i = 0; i < 100;i++){
            int finalI = i;
            try {
                CompletableFuture<String> asyncFuture = CompletableFuture.supplyAsync(() -> {
                    try {
                        FastThreadLocalThread.sleep(20);
                    } catch (InterruptedException e) {
                        log.error("{}", e.getMessage());
                    }
                    System.out.println(finalI);
                    return "123";
                }, poolTaskExecutor);

                completableFutureList.add(asyncFuture);
            }catch (Exception ex){
                log.info("数量超过队列，拒绝:{}",ex.getMessage());
            }
        }
        CompletableFuture<Void> voidCompletableFuture =
                CompletableFuture.allOf( completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        try {
            voidCompletableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("统一执行出错 {}",e);
        }
        System.out.println("结束");
        poolTaskExecutor.shutdown();
    }
}

/*
 *
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
 *
 */

package com.danebrown.juc;

import io.netty.util.concurrent.FastThreadLocalThread;
import lombok.extern.log4j.Log4j2;

import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Log4j2
public class ArrayBlockDeepLearn {
    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<Integer> arrayBlockingQueue =
                new ArrayBlockingQueue<Integer>(2);
        ExecutorService executorService =  Executors.newFixedThreadPool(10);
        int testCount = 5;
        for(int i=0 ;i < testCount;i++){
            int finalI = i;
            executorService.execute(()->{
                try {
                    log.info("put-->开始放:{}",finalI);
                    arrayBlockingQueue.put(finalI);
                    log.info("put-->放进去了:{}",finalI);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        FastThreadLocalThread.sleep(1000);
        log.info("array中剩余:{}",arrayBlockingQueue);
        Scanner scanner = new Scanner(System.in);

        for(int i = 0; i < testCount; i++){
            String pause =  scanner.nextLine();
            int rst = arrayBlockingQueue.take();
            log.info("take-->取出了:{}",rst);
            log.info("array中剩余:{}",arrayBlockingQueue);

        }

        scanner.close();
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

    }
}

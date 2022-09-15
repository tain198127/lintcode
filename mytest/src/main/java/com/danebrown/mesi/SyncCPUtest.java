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

package com.danebrown.mesi;

import lombok.extern.log4j.Log4j2;
import net.openhft.affinity.AffinityLock;
import net.openhft.affinity.AffinityThreadFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static net.openhft.affinity.AffinityStrategies.*;

@Log4j2
public class SyncCPUtest {
    private static Object instance = new Object();
    private static int cpucore=Runtime.getRuntime().availableProcessors();
    public static AffinityLock acquireLock(long threadID) {
        long cpuid = threadID % cpucore;
        
        return AffinityLock.acquireLock(true).acquireLock((int) cpuid).acquireLock(ANY);
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        log.info("开始测试，请输入Y");
        int r = System.in.read();
        log.info("开始构建线程池");
//      
        ThreadPoolExecutor executor = new ThreadPoolExecutor(200,500,
                1000, TimeUnit.SECONDS, 
                new ArrayBlockingQueue(10000)
                ,new AffinityThreadFactory("bg", DIFFERENT_CORE, DIFFERENT_SOCKET, ANY)
        );
        List<Callable<String>> callableList = new ArrayList<>();
        
        for(int i=0;i<500;i++){

            int finalI = i;
            callableList.add(new Callable<String>() {
                final int[] j = {0};
                @Override
                public String call() throws Exception {
                    {
//                        try(AffinityLock lock = acquireLock(Thread.currentThread().getId())){
                            
                            log.info("执行任务:{},cpuid:{}", finalI,AffinityLock.dumpLocks());
                            while (j[0] < 1000000){
                                synchronized (instance) {
                                    j[0]++;
                                }
                            }
                            return "完成";
//                        }
                    }

                }
            });
            
        }
        System.out.println("任务准备完毕，开始执行");
        try {
            List<Future<String>> result =executor.invokeAll(callableList);
            for (Future f :
                    result) {
                System.out.println(f.get());
            }
            
        } catch (InterruptedException | ExecutionException e) {
            
        }
        executor.shutdown();
        executor.awaitTermination(10,TimeUnit.MILLISECONDS);

    }
}

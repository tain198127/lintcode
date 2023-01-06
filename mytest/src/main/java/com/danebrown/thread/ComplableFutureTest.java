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

package com.danebrown.thread;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.CompletableFuture;

@Log4j2
public class ComplableFutureTest {
    public static void main(String[] args) {
        for(int i=0 ; i < 10000;i++) {
            boolean isSucc = testCF1("210");
            if(!isSucc){
                log.error("失败");
                return;
            }
        }
        
        
        
    }
    public static boolean testCF1(String compare){
        CompletableFuture<String> future = CompletableFuture.supplyAsync(()-> {
            long l = System.currentTimeMillis();
            return String.valueOf(l)+"["+Thread.currentThread().getId()+"]";
        });
        CompletableFuture<String> f2 = CompletableFuture.completedFuture("["+Thread.currentThread().getId()+"]");
        String[] strRst = new String[1];
        strRst[0] = "";
        
        CompletableFuture cf3 = future.thenCombine(f2,(r1,r2)->{
            log.info("cf3,then combine");
            strRst[0] = strRst[0]+"2" ;
            return r1 +":"+r2;
        }).thenApply((result)->{
                    strRst[0] = strRst[0]+"1" ;
                    log.info("cf3,then apply");
                    return result + "ok";

                }

        ).thenAccept((result)->{
            log.info("cf3,then accept"+result);
            strRst[0] = strRst[0]+"0" ;
        })
                ;
        

        boolean isSucc = cf3.complete("success");
//        log.info(isSucc);
        log.info(strRst[0]);
        return strRst[0].equalsIgnoreCase(compare);
    } 
}

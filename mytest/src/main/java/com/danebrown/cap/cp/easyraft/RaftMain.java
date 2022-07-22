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

package com.danebrown.cap.cp.easyraft;

import cn.hutool.core.lang.hash.CityHash;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@RestController
@Slf4j
public class RaftMain {
    
    public static final String ERROR = "<ERR>";
    public static final String END = "<EOF>";
    public static final String NULL = "<NIL>";
    public static final String TIMEOUT = "<TMO>";
    public static final List<String> errors = new ArrayList<>();
    static{
        errors.add(ERROR);
        errors.add(NULL);
        errors.add(TIMEOUT);
    }
    
    
    @Autowired
    ConfigurableApplicationContext application;

    int selfPort=0;
    @PostConstruct
    public void init(){
        selfPort = Integer.parseInt(application.getEnvironment().getProperty("server.port"));    
    }
    
    int[] ports={8080,8081,8082};
    Map<String,String> logEntry = new HashMap<>();
    private int hashIdx(String msg){
        return  Math.abs(CityHash.hash32(msg.getBytes())%ports.length);
    }

    /**
     * 广播
     * @return
     */
    private List<String> broadCast(String version, String body){
        List<String> rstList = new ArrayList<>();
        for(int i =0; i<ports.length;i++){
            if(ports[i] == selfPort){
                continue;
            }
            String url = String.format("http://127.0.0.1:%s/broadcast/%s/%s",ports[i],version,body);
            HttpUriRequest request = new HttpGet(url);
            try(CloseableHttpResponse response = HttpClients.createDefault().execute(request)){
                HttpEntity httpEntity = response.getEntity();
                String result = "";
                if(httpEntity!=null){
                    result= EntityUtils.toString(httpEntity,"utf-8");
                    rstList.add(result);
                    log.info("返回结果是:{}",result);
                }
            } catch (IOException e) {
                rstList.add(ERROR);
                e.printStackTrace();
            }
        }
        return rstList;
    }

    /**
     * 转发
     * @return
     */
    private String redirect(String url, String version, String msg){
        String result = "";
        HttpUriRequest request = new HttpGet(url);
        try(CloseableHttpResponse response = HttpClients.createDefault().execute(request)){
            HttpEntity httpEntity = response.getEntity();
            if(httpEntity!=null){
                result= EntityUtils.toString(httpEntity,"utf-8");
                log.info("返回结果是:{}",result);
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;
        }
        return NULL;

    }
    @GetMapping("/broadcast/{version}/{body}")
    public Flux<String> broadcast(@PathVariable(name = "version") String version, @PathVariable(name="body") String body){
        if(logEntry.containsKey(version)){
            if(logEntry.get(version).equals(body)){
                return Flux.just(END);
            }
        }
        logEntry.put(version, body);
        return Flux.just(END);
    }
    
    @SneakyThrows
    @GetMapping("/write/{version}/{body}")
    public Flux<String> write(@PathVariable(name = "version") String version, @PathVariable(name="body") String body){
        if(logEntry.containsKey(version)){
            if(logEntry.get(version).equals(body)){
                return Flux.just(END);
            }
        }
        int idx = hashIdx(body);
        //hash值是当前节点的
        if(ports[idx] == selfPort){
            //广播
            logEntry.put(version,body);
            List<String> braod =  broadCast(version,body);
            OptionalInt succCount = braod.stream().mapToInt((b)->{
                if(errors.contains(b)){
                    return 0;
                }
                return 1;
            }).reduce((left, right) -> left+right);
            //过半成功
            if(succCount.isPresent() && ((double)succCount.getAsInt()/(double) ports.length)>0.5){
                return Flux.just(END);
            }
            return Flux.just(ERROR);
        }
        //hash值不是当前节点的
        else{
            //转发
            String url = String.format("http://127.0.0.1:%s/write/%s/%s",ports[idx],version,body);
            String rst = redirect(url,version,body);
            return Flux.just(rst);
        }
        
//        for(int i =0; i<ports.length;i++){
//            if(ports[i] == selfPort){
//                continue;
//            }
//            String url = String.format("http://127.0.0.1:%s/write/%s/%s",ports[i],version,body);
//            HttpUriRequest request = new HttpGet(url);
//            try(CloseableHttpResponse response = HttpClients.createDefault().execute(request)){
//                HttpEntity httpEntity = response.getEntity();
//                String result = "";
//                if(httpEntity!=null){
//                    result= EntityUtils.toString(httpEntity,"utf-8");
//                    log.info("返回结果是:{}",result);
//                }
//            }
//        }
        
        
//        return Flux.just(body);
    }
    
    @GetMapping("/get/{version}")
    public Flux<String> get(@PathVariable(name = "version") String version){
        if(logEntry.containsKey(version)){
            return Flux.just(logEntry.get(version));
        }
        return Flux.just("nil");
    }
}

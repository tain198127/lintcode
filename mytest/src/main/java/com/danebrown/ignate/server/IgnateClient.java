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

package com.danebrown.ignate.server;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@Api
@RestController
@Log4j2
public class IgnateClient {
    @Autowired
    IgniteBean igniteBean;
    @ApiOperation(value = "获取用户信息",notes = "根据id获取用户详细信息")
    @ApiResponses({
            @ApiResponse(code = 200,message = "请求成功"),
            @ApiResponse(code = 400,message = "缺少必要的请求参数"),
            @ApiResponse(code = 401,message = "请求路径没有响应的权限"),
            @ApiResponse(code = 403,message = "请求路径被隐藏不能访问"),
            @ApiResponse(code = 404,message = "请求路径没有或页面跳转路径错误"),
            @ApiResponse(code = 405,message = "请求方法不支持"),
    })
    @GetMapping("put")
    public Flux<String> put(@RequestParam("name") String name, @RequestParam("value") String value){
        try {
            igniteBean.set(name, value);
        }catch (Exception ex){
            log.error("name:{},value:{},ex:{}",name,value,ex.getMessage());
        }
        return Flux.just(value);
    }
    @ApiOperation(value = "获取用户信息",notes = "根据id获取用户详细信息")
    @ApiResponses({
            @ApiResponse(code = 200,message = "请求成功"),
            @ApiResponse(code = 400,message = "缺少必要的请求参数"),
            @ApiResponse(code = 401,message = "请求路径没有响应的权限"),
            @ApiResponse(code = 403,message = "请求路径被隐藏不能访问"),
            @ApiResponse(code = 404,message = "请求路径没有或页面跳转路径错误"),
            @ApiResponse(code = 405,message = "请求方法不支持"),
    })
    @GetMapping("get")
    public Flux<String> get(@RequestParam("name") String name){
        String  msg = igniteBean.get(name);
        return Flux.just(msg);
    }
}

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

package com.danebrown.jvm;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class DynamicInvokeFeature {
    public static void main(String[] args) {
        FunctionInterfaceTest functionInterfaceTest = ()->{
            return "hello";
        };
        String result = messageProcessor(functionInterfaceTest);
        System.out.println(result);
        Supplier<String> val =
                () -> "11211-"+ThreadLocalRandom.current().nextInt();

        System.out.println(val.get());
        Flux.just("123")
                .doOnNext(c->{
            System.out.println("next"+c);
        })
                .flatMap((t)->{return Flux.just("map"+t);})
                .defer(()->{
            System.out.println("12");
            return Flux.defer(()->{
                System.out.println("13");
                return Flux.just("131");
            });
        }).subscribe(consumer->{
            System.out.println("consumer:"+consumer);
        });
        System.out.println("bye!");
    }

    public static String messageProcessor(FunctionInterfaceTest functionInterfaceTest){
        return "process1 "+ functionInterfaceTest.sayIt();
    }
    @FunctionalInterface
    public interface FunctionInterfaceTest{
        public String sayIt();
    }
}

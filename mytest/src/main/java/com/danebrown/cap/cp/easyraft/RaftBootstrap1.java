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

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        excludeName = {"org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration"}
)

public class RaftBootstrap1 implements CommandLineRunner{
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(RaftBootstrap1.class);
        springApplication.setWebApplicationType(WebApplicationType.REACTIVE);
        springApplication.run(RaftBootstrap1.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        
    }
    
}

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

package com.danebrown.hazelcast;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ISet;

import java.io.IOException;

public class SetTimedProducer {
    public static void main(String... args) throws IOException,
            InterruptedException {
        //initialize hazelcast instance
        HazelcastInstance hazelcast = Hazelcast.newHazelcastInstance();
        Thread.sleep(5000);
        // create a set
        ISet<String> hzFruits = hazelcast.getSet("fruits");
        hzFruits.add("Mango");
        Thread.sleep(2000);
        hzFruits.add("Apple");
        Thread.sleep(2000);
        hzFruits.add("Banana");
        System.exit(0);
    }
}

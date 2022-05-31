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

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;

public class HZClient {
    public static void main(String[] args) {
        //initialize hazelcast client
        HazelcastInstance hzClient = HazelcastClient.newHazelcastClient();
        //read from map
        Map<String, String> vehicleOwners = hzClient.getMap("vehicleOwnerMap");
        System.out.println(vehicleOwners.get("John"));
        System.out.println("Member of cluster: " +
                hzClient.getCluster().getMembers());
        // perform shutdown
        hzClient.getLifecycleService().shutdown();
    }
}

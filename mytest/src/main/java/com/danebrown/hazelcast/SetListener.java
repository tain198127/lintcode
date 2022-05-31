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
import com.hazelcast.core.ItemEvent;
import com.hazelcast.core.ItemListener;

import java.io.IOException;
import java.util.Scanner;

public class SetListener {
    public static void main(String... args) throws IOException, InterruptedException {
        //initialize hazelcast instance
        HazelcastInstance hazelcast = Hazelcast.newHazelcastInstance();
        // create a set
        ISet<String> hzFruits = hazelcast.getSet("fruits");
        ItemListener<String> listener = new FruitListener<String>();
        hzFruits.addItemListener(listener, true);
        System.out.println("输入任何字符将结束进程");
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        System.exit(0);
    }
    private static class FruitListener<String> implements ItemListener<String> {
        private int count = 0;
        @Override
        public void itemAdded(ItemEvent<String> item) {
            System.out.println("item added" + item);
            count ++;
            System.out.println("Total elements" + count);
        }
        @Override
        public void itemRemoved(ItemEvent<String> item) {
            count --;
        }
    }
}

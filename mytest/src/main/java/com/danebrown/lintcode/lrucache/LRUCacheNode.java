/*
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
 */
package com.danebrown.lintcode.lrucache;

/**
 * Created by dane on 2018/1/16.
 */
public class LRUCacheNode {
    public static void main(String[] args) {
        LRUCache cache = new LRUCache(10);
        cache.set(7, 28);
        cache.set(7, 1);
        cache.set(8, 15);
        System.out.printf("[%s]", cache.get(6));
        cache.set(10, 27);
        cache.set(8, 10);
        System.out.printf("[%s]", cache.get(8));
        cache.set(6, 29);
        cache.set(1, 9);
        System.out.printf("[%s]", cache.get(6));
        cache.set(10, 7);
        System.out.printf("[%s]", cache.get(1));
        System.out.printf("[%s]", cache.get(2));
        System.out.printf("[%s]", cache.get(13));
        cache.set(8, 30);
        cache.set(1, 5);
        System.out.printf("[%s]", cache.get(1));
        cache.set(13, 2);
        System.out.printf("[%s]", cache.get(12));
    }
}

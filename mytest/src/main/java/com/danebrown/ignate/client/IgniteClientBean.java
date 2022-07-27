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

package com.danebrown.ignate.client;

import lombok.extern.log4j.Log4j2;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.ClientCacheConfiguration;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.sharedfs.TcpDiscoverySharedFsIpFinder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
@Log4j2
@Component
public class IgniteClientBean {

    

//    private static final String CACHE_NAME = "CACHE_NAME";
//    static IgniteConfiguration igniteCfg = new IgniteConfiguration();
//    static {
//        igniteCfg.setLifecycleBeans(new MyLifecycleBean());
//        igniteCfg.setWorkDirectory("/Users/danebrown/develop/github/lintcode/mytest/log");
//        discovery(igniteCfg);
//    }
    public static void discovery(IgniteConfiguration cfg){
        TcpDiscoverySpi spi = new TcpDiscoverySpi();
        // Configuring IP finder.
        TcpDiscoverySharedFsIpFinder ipFinder = new TcpDiscoverySharedFsIpFinder();
        ipFinder.setPath("/Users/danebrown/develop/github/lintcode/mytest/log");

        spi.setIpFinder(ipFinder);
        cfg.setDiscoverySpi(spi);
    }
//    Ignite ignite = null;
    IgniteClient client=null;
    
    @PostConstruct
    public void init(){



        ClientConfiguration cfg = new ClientConfiguration().setAddresses("192.168.3.142:10800");
        try {
            client = Ignition.startClient(cfg);
        }catch (Exception ex){
            log.error(ex);
        }
            
            // Get data from the cache
    }
    public String get(String cacheName){
        ClientCacheConfiguration clientcacheCfg = new ClientCacheConfiguration().setName("References")
                .setCacheMode(CacheMode.REPLICATED)
                .setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
        ClientCache<String,String> cache = client.getOrCreateCache(clientcacheCfg);
        return cache.get(cacheName);
    }
    public void set(String cacheName,String value){
        ClientCacheConfiguration clientcacheCfg = new ClientCacheConfiguration().setName("References")
                .setCacheMode(CacheMode.REPLICATED)
                .setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
        ClientCache<String,String> cache = client.getOrCreateCache(clientcacheCfg);
        cache.put(cacheName,value);
    }
}

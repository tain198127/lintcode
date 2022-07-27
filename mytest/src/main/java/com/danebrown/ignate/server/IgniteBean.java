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

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterState;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.sharedfs.TcpDiscoverySharedFsIpFinder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;

@Component
public class IgniteBean {
    private static final String CACHE_NAME = "CACHE_NAME";
    static IgniteConfiguration igniteCfg = new IgniteConfiguration();
    static {
        
        
    }
    public static void discovery(IgniteConfiguration cfg){
        TcpDiscoverySpi spi = new TcpDiscoverySpi();
        // Configuring IP finder.
//        String port = System.getProperty("ignitePort");
//        TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi().setLocalPort(Integer.parseInt(port));
        TcpDiscoverySharedFsIpFinder ipFinder = new TcpDiscoverySharedFsIpFinder();
        ipFinder.setPath("/Users/danebrown/develop/github/lintcode/mytest/log");

        spi.setIpFinder(ipFinder);
//        cfg.setDiscoverySpi(discoverySpi);
        cfg.setDiscoverySpi(spi);
    }
    Ignite ignite = null;
    @PostConstruct
    public void init(){
        String port = System.getProperty("ignitePort");
        igniteCfg.setLifecycleBeans(new MyLifecycleBean());
        igniteCfg.setWorkDirectory("/Users/danebrown/develop/github/lintcode/mytest/log/"+port);
        discovery(igniteCfg);
        ignite = Ignition.start(igniteCfg);
        {
            ignite.log().info("Info Message Logged!");
            Collection<String> res = ignite.compute().broadcast(new IgniteCallable<String>() {

                @Override
                public String call() throws Exception {
                    IgniteCache<Object, Object> cache = ignite.getOrCreateCache(CACHE_NAME);
                    return cache.getName();
                }
            });
        }
        ignite.cluster().state(ClusterState.ACTIVE);
        ignite.cluster().baselineAutoAdjustEnabled(true);

        ignite.cluster().baselineAutoAdjustTimeout(30000);
    }
    public String get(String cacheName){
        IgniteCache<String,String> cache = ignite.getOrCreateCache("References");
        return cache.get(cacheName);
    }
    public void set(String cacheName,String value){
        IgniteCache<String,String> cache = ignite.getOrCreateCache("References");
        cache.put(cacheName,value);
    }
}

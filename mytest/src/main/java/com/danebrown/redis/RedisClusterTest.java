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

package com.danebrown.redis;

import io.lettuce.core.RedisChannelHandler;
import io.lettuce.core.RedisConnectionStateListener;
import io.lettuce.core.api.push.PushMessage;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.push.RedisClusterPushListener;
import io.lettuce.core.cluster.api.reactive.RedisAdvancedClusterReactiveCommands;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import io.lettuce.core.cluster.pubsub.RedisClusterPubSubListener;
import io.lettuce.core.cluster.pubsub.StatefulRedisClusterPubSubConnection;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import java.net.SocketAddress;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

@Log4j2
public class RedisClusterTest {
    public static void simpleCmd(RedisClusterClient client) {

        StatefulRedisClusterConnection<String, String> connection = client.connect();
        RedisConnectionStateListener listener = new RedisConnectionStateListener() {
            @Override
            public void onRedisDisconnected(RedisChannelHandler<?, ?> connection) {
                log.info("onRedisDisconnected:{}", connection);
            }

            @Override
            public void onRedisExceptionCaught(RedisChannelHandler<?, ?> connection, Throwable cause) {
                log.info("onRedisExceptionCaught:{},exception:{}", connection, cause);
            }

            @Override
            public void onRedisConnected(RedisChannelHandler<?, ?> connection, SocketAddress socketAddress) {
                log.info("onRedisConnected:{}; sock:{}", connection, socketAddress);
            }
        };

        client.addListener(listener);
        RedisClusterPushListener publishListener = new RedisClusterPushListener() {
            @Override
            public void onPushMessage(RedisClusterNode node, PushMessage message) {
                log.info("node:{};content:{}", node.getNodeId(), message);
            }
        };
        connection.addListener(publishListener);

        log.info("date 通过keyslot的结果是:{}", connection.sync().clusterKeyslot("date"));

        RedisAdvancedClusterReactiveCommands<String, String> async = connection.reactive();

        log.info("访问前前的connection是:{}", connection);
        async.set("key", "123").then(async.set("key1", "234")).then(async.set("key2", "567")).checkpoint();


        Mono<String> value = async.get("key");

        long dateKeySlot = async.clusterKeyslot("date").block();
        log.info("date的keyslot是:{}", dateKeySlot);

        log.info("key 是 {}， 值是:{}", "key", value.block());
        log.info("当前的connection是:{}", connection);


        connection.flushCommands();
        connection.close();
    }

    public static void subscribe(RedisClusterClient client) {
        StatefulRedisClusterPubSubConnection<String, String> statefulRedisClusterPubSubConnection
                = client.connectPubSub();

        statefulRedisClusterPubSubConnection.addListener(new RedisClusterPubSubListener() {
            @Override
            public void message(RedisClusterNode node, Object channel, Object message) {
                log.info("message:channel:{};message:{}", channel, message);
            }

            @Override
            public void message(RedisClusterNode node, Object pattern, Object channel, Object message) {
                log.info("message:channel:{};message:{}", channel, message);
            }

            @Override
            public void subscribed(RedisClusterNode node, Object channel, long count) {
                log.info("subscribed:channel:{};message:{}", channel);
            }

            @Override
            public void psubscribed(RedisClusterNode node, Object pattern, long count) {

            }

            @Override
            public void unsubscribed(RedisClusterNode node, Object channel, long count) {
                log.info("unsubscribed:channel:{};", channel);
            }

            @Override
            public void punsubscribed(RedisClusterNode node, Object pattern, long count) {

            }
        });
        statefulRedisClusterPubSubConnection.sync().subscribe("xxoo");

    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        RedisClusterClient client = RedisClusterClient.create("redis" + "://127.0.0.1" + ":7000");
        simpleCmd(client);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                subscribe(client);
            }
        });
        t.setDaemon(true);
        t.start();

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();


    }
}

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

import lombok.extern.log4j.Log4j2;
import org.apache.ignite.Ignite;
import org.apache.ignite.lifecycle.LifecycleBean;
import org.apache.ignite.lifecycle.LifecycleEventType;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class MyLifecycleBean implements LifecycleBean {
    @IgniteInstanceResource
    public Ignite ignite;

    @Override
    public void onLifecycleEvent(LifecycleEventType evt) {
        if (evt == LifecycleEventType.AFTER_NODE_START) {
            log.error("After the node (consistentId = {}) starts.\n", ignite.cluster().node().consistentId());
        }
        if (evt == LifecycleEventType.AFTER_NODE_STOP){
            log.error("AFTER THE NODE (consistentId = {}) stop.\n",ignite.cluster().node().consistentId());
        }
        if (evt == LifecycleEventType.BEFORE_NODE_START){
            log.error("BEFORE THE NODE (consistentId = {}) start.\n",ignite.name());
        }
        if (evt == LifecycleEventType.BEFORE_NODE_STOP){
            log.error("BEFORE THE NODE (consistentId = {}) stop.\n",ignite.cluster().node().consistentId());
        }
    }
}

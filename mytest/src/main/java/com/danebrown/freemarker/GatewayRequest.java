package com.danebrown.freemarker;

import com.google.common.collect.Maps;

import java.util.Map;
@lombok.Data
public class GatewayRequest {
    private Map<String, String> txHeader = Maps.newHashMap();
    /** 外部请求报文体 */
    private Map<String, Object> txBody = Maps.newHashMap();
    /** 外部请求私有域 */
    private Map<String, String> txEmb;
}

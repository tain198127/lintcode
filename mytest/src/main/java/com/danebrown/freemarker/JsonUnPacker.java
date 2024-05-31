package com.danebrown.freemarker;

import com.danebrown.freemarker.jsonutils.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.danebrown.jvm.JOLTest.printObjSize;

public class JsonUnPacker {
    
    private static JsonUtils jsonUtils = new JsonUtils();
    public static String rdmPkt(int times, String seed){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<times;i++){
            stringBuilder.append(seed);
        }
        return stringBuilder.toString();
    }
    public static  List<String> random(int  times) throws IOException {
    
        List<String> msg = new ArrayList<>();
        for(int i=0;i < times; i++) {
            String stringBuilder = rdmPkt(400, UUID.randomUUID().toString());
            String reqString = "{\"txBody\":{\"txComni\":{\"accountingDate\":\"20200513\"},\"txEntity\":{\"zr\":\"" + stringBuilder.toString() + "\"}},\"txHeader\":{\"busiSendInstNo\":\"instNooo\",\"dataCenterCode\":\"F\",\"embedMsgrptLen\":\"12\",\"globalBusiTrackNo\":\"yygn6669085976445652279323310000\",\"mainMapElemntInfo\":\"0400003894074402\",\"msgAgrType\":\"1\",\"msgrptFmtVerNo\":\"123\",\"msgrptTotalLen\":\"123\",\"pubMsgHeadLen\":\"12\",\"reqSysSriNo\":\"14234567\",\"resvedInputInfo\":\"1234567\",\"sendSysOrCmptNo\":\"1022199\",\"servNo\":\"1022199T140\",\"servTpCd\":\"1\",\"servVerNo\":\"10000\",\"startSysOrCmptNo\":\"F000007\",\"subtxNo\":\"yysn3335566695879085916565794236\",\"targetSysOrCmptNo\":\"F000002\",\"txSendTime\":\"20200909202020001\",\"txStartTime\":\"20200909202020001\"}}";
            msg.add(reqString);
        }
        return msg;
        
    }
    public static void main(String[] args) throws IOException {
        
        
//        String reqString = "{\"txBody\":{\"txComni\":{\"accountingDate\":\"20200513\"},\"txEntity\":{\"zr\":\"621300000000000433\"}},\"txHeader\":{\"busiSendInstNo\":\"instNooo\",\"dataCenterCode\":\"F\",\"embedMsgrptLen\":\"12\",\"globalBusiTrackNo\":\"yygn6669085976445652279323310000\",\"mainMapElemntInfo\":\"0400003894074402\",\"msgAgrType\":\"1\",\"msgrptFmtVerNo\":\"123\",\"msgrptTotalLen\":\"123\",\"pubMsgHeadLen\":\"12\",\"reqSysSriNo\":\"14234567\",\"resvedInputInfo\":\"1234567\",\"sendSysOrCmptNo\":\"1022199\",\"servNo\":\"1022199T140\",\"servTpCd\":\"1\",\"servVerNo\":\"10000\",\"startSysOrCmptNo\":\"F000007\",\"subtxNo\":\"yysn3335566695879085916565794236\",\"targetSysOrCmptNo\":\"F000002\",\"txSendTime\":\"20200909202020001\",\"txStartTime\":\"20200909202020001\"}}";
        
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<100;i++){
            stringBuilder.append("621300000000000433");
        }
        String reqString = "{\"txBody\":{\"txComni\":{\"accountingDate\":\"20200513\"},\"txEntity\":{\"zr\":"+stringBuilder.toString()+"}},\"txHeader\":{\"busiSendInstNo\":\"instNooo\",\"dataCenterCode\":\"F\",\"embedMsgrptLen\":\"12\",\"globalBusiTrackNo\":\"yygn6669085976445652279323310000\",\"mainMapElemntInfo\":\"0400003894074402\",\"msgAgrType\":\"1\",\"msgrptFmtVerNo\":\"123\",\"msgrptTotalLen\":\"123\",\"pubMsgHeadLen\":\"12\",\"reqSysSriNo\":\"14234567\",\"resvedInputInfo\":\"1234567\",\"sendSysOrCmptNo\":\"1022199\",\"servNo\":\"1022199T140\",\"servTpCd\":\"1\",\"servVerNo\":\"10000\",\"startSysOrCmptNo\":\"F000007\",\"subtxNo\":\"yysn3335566695879085916565794236\",\"targetSysOrCmptNo\":\"F000002\",\"txSendTime\":\"20200909202020001\",\"txStartTime\":\"20200909202020001\"}}";
        GatewayRequest preWarning = jsonUtils.deserialize(reqString, GatewayRequest.class);
        int times = 100000;
         List<String> req = random(times);
        long start = System.currentTimeMillis();
        for(int i=0;i<times;i++) {
            GatewayRequest request = jsonUtils.deserialize(req.get(i), GatewayRequest.class);
//            System.out.println(request.getTxHeader().get("busiSendInstNo"));
        }
        long end = System.currentTimeMillis();
        printObjSize(req.get(0),"大报文对象反序列化耗时");
//        System.out.println("序列化内容长度："+(req.get(0).length()*4)/1024+"KB");
        System.out.println("反序列化耗时："+((float)(end-start)/times)+"毫秒");
    }
}

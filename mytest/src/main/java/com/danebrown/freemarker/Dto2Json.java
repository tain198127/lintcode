package com.danebrown.freemarker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.threadly.util.StringBufferWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Created by danebrown on 2020/8/1
 * mail: tain198127@163.com
 */
@Log4j2
public class Dto2Json {
//    private static Template tpls;
    Configuration cfg = new Configuration();
    Map<String,String> testMap = new HashMap<>();

    public Dto2Json() throws IOException {
        cfg.setDefaultEncoding("UTF-8");

        cfg.setClassForTemplateLoading(this.getClass(), "/");
        cfg.setTemplateUpdateDelayMilliseconds(65535);
        testMap.put("test1","1");
        testMap.put("test2","2");
//        tpls = cfg.getTemplate("freemaker.ftl");
//        tpls.setAutoFlush(false);


    }

    private void preLoading() throws IOException, TemplateException {
        InnerTestFtl innerUser = new InnerTestFtl("test");
        List<Map<String, Object>> innerMap = new ArrayList<>();
        List<InnerTestFtl> paper = new ArrayList<>();
        paper.add(new InnerTestFtl("prime book"));
        paper.add(new InnerTestFtl("handle book"));

        TestTarget complexJson = new TestTarget();
        complexJson.setAge(10);
        complexJson.setMac("1122");
        complexJson.setSize(10);
        complexJson.setUserName("user");
        List<String> books = new ArrayList<>();
        books.add("test");
        books.add("book store");
        Map<String, InnerTestFtl> cplxMap = new HashMap<>();
        cplxMap.put("key", new InnerTestFtl("userNsme"));
        complexJson.setBooks(books);
        complexJson.setComplexMap(cplxMap);
        String bigData = "ABCDEFGALJFSLJMCDFABCDEFGALJFSLJMCDFABCDEFGALJFSLJMCDFABCDEFGALJFSLJMCDF";


        TestFtlInput testFtl = TestFtlInput.builder()
                .size(String.valueOf(ThreadLocalRandom.current().nextInt(10, 10000)))
                .user(innerUser)
                .paper(paper)
                .testMap(testMap)
                .SEND_BANK(bigData + ThreadLocalRandom.current().nextInt())
                .ACCOUNT(bigData + ThreadLocalRandom.current().nextInt())
                .BUSI_CODE(bigData + ThreadLocalRandom.current().nextInt())
                .CHNL_CODE(bigData + ThreadLocalRandom.current().nextInt())
                .CLR_DATE(bigData + ThreadLocalRandom.current().nextInt())
                .OUTSYS_TX_CODE(bigData + ThreadLocalRandom.current().nextInt())
                .RCV_BKNO(bigData + ThreadLocalRandom.current().nextInt())
                .RET_EXPLAIN(bigData + ThreadLocalRandom.current().nextInt())
                .SEQNO(bigData + ThreadLocalRandom.current().nextInt())
                .size1(bigData + ThreadLocalRandom.current().nextInt())
                .TRADEFLAG(bigData + ThreadLocalRandom.current().nextInt())
                .TRADEFLAG1(bigData + ThreadLocalRandom.current().nextInt())
                .TRADEFLAG2(bigData + ThreadLocalRandom.current().nextInt())
                .TRADEFLAG3(bigData + ThreadLocalRandom.current().nextInt())
                .TRADEFLAG4(bigData + ThreadLocalRandom.current().nextInt())
                .TRADEFLAG5(bigData + ThreadLocalRandom.current().nextInt())
                .TRADEFLAG6(bigData + ThreadLocalRandom.current().nextInt())
                .TRADEFLAG7(bigData + ThreadLocalRandom.current().nextInt())
                .TRADEFLAG8(bigData + ThreadLocalRandom.current().nextInt())
                .TRADEFLAG9(bigData + ThreadLocalRandom.current().nextInt())
                .TRADEFLAG10(bigData + ThreadLocalRandom.current().nextInt())
                .TRADEFLAG11(bigData + ThreadLocalRandom.current().nextInt())
                .TRADEFLAG12(bigData + ThreadLocalRandom.current().nextInt())
                .TRADEFLAG13(bigData + ThreadLocalRandom.current().nextInt())
                .TRADEFLAG14(bigData + ThreadLocalRandom.current().nextInt())
                .TRADEFLAG15(bigData + ThreadLocalRandom.current().nextInt())
                .TRADEFLAG16(bigData + ThreadLocalRandom.current().nextInt())
                .TRADEFLAG17(bigData + ThreadLocalRandom.current().nextInt())
                .TRADEFLAG18(bigData + ThreadLocalRandom.current().nextInt())
                .TRADEFLAG19(bigData + ThreadLocalRandom.current().nextInt())
                .TRADEFLAG20(bigData + ThreadLocalRandom.current().nextInt())
                .build();
        ObjectMapper oMapper = new ObjectMapper();
        Map objMap = oMapper.convertValue(testFtl, Map.class);
        objMap.put("_Sign_", new MacSign());
        String msg = this.Obj2Json(objMap, cfg.getTemplate("freemaker.ftl"));
//        log.info("{}",msg);
    }
    private void test(int times) throws IOException, TemplateException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        InnerTestFtl innerUser = new InnerTestFtl("test");
        List<Map<String, Object>> innerMap = new ArrayList<>();
        List<InnerTestFtl> paper = new ArrayList<>();
        paper.add(new InnerTestFtl("prime book"));
        paper.add(new InnerTestFtl("handle book"));

        TestTarget complexJson = new TestTarget();
        complexJson.setAge(10);
        complexJson.setMac("1122");
        complexJson.setSize(10);
        complexJson.setUserName("user");
        List<String> books = new ArrayList<>();
        books.add("test");
        books.add("book store");
        Map<String, InnerTestFtl> cplxMap = new HashMap<>();
        cplxMap.put("key", new InnerTestFtl("userNsme"));
        complexJson.setBooks(books);
        complexJson.setComplexMap(cplxMap);
        /**
         * 预热
         */
        this.preLoading();
        String json = gson.toJson(complexJson);
//        log.info("json:{}", json);
        String bigData = "ABCDEFGALJFSLJMCDFABCDEFGALJFSLJMCDFABCDEFGALJFSLJMCDFABCDEFGALJFSLJMCDF";
        Stopwatch stopwatch = Stopwatch.createStarted();

        for (int i = 0; i < 100; i++) {
            TestFtlInput testFtl = TestFtlInput.builder()
                    .size(String.valueOf(ThreadLocalRandom.current().nextInt(10, 10000)))
                    .user(innerUser)
                    .paper(paper)
                    .SEND_BANK(bigData + ThreadLocalRandom.current().nextInt())
                    .ACCOUNT(bigData + ThreadLocalRandom.current().nextInt())
                    .BUSI_CODE(bigData + ThreadLocalRandom.current().nextInt())
                    .CHNL_CODE(bigData + ThreadLocalRandom.current().nextInt())
                    .CLR_DATE(bigData + ThreadLocalRandom.current().nextInt())
                    .OUTSYS_TX_CODE(bigData + ThreadLocalRandom.current().nextInt())
                    .RCV_BKNO(bigData + ThreadLocalRandom.current().nextInt())
                    .RET_EXPLAIN(bigData + ThreadLocalRandom.current().nextInt())
                    .SEQNO(bigData + ThreadLocalRandom.current().nextInt())
                    .size1(bigData + ThreadLocalRandom.current().nextInt())
                    .testMap(testMap)
                    .TRADEFLAG(bigData + ThreadLocalRandom.current().nextInt())
                    .TRADEFLAG1(bigData + ThreadLocalRandom.current().nextInt())
                    .TRADEFLAG2(bigData + ThreadLocalRandom.current().nextInt())
                    .TRADEFLAG3(bigData + ThreadLocalRandom.current().nextInt())
                    .TRADEFLAG4(bigData + ThreadLocalRandom.current().nextInt())
                    .TRADEFLAG5(bigData + ThreadLocalRandom.current().nextInt())
                    .TRADEFLAG6(bigData + ThreadLocalRandom.current().nextInt())
                    .TRADEFLAG7(bigData + ThreadLocalRandom.current().nextInt())
                    .TRADEFLAG8(bigData + ThreadLocalRandom.current().nextInt())
                    .TRADEFLAG9(bigData + ThreadLocalRandom.current().nextInt())
                    .TRADEFLAG10(bigData + ThreadLocalRandom.current().nextInt())
                    .TRADEFLAG11(bigData + ThreadLocalRandom.current().nextInt())
                    .TRADEFLAG12(bigData + ThreadLocalRandom.current().nextInt())
                    .TRADEFLAG13(bigData + ThreadLocalRandom.current().nextInt())
                    .TRADEFLAG14(bigData + ThreadLocalRandom.current().nextInt())
                    .TRADEFLAG15(bigData + ThreadLocalRandom.current().nextInt())
                    .TRADEFLAG16(bigData + ThreadLocalRandom.current().nextInt())
                    .TRADEFLAG17(bigData + ThreadLocalRandom.current().nextInt())
                    .TRADEFLAG18(bigData + ThreadLocalRandom.current().nextInt())
                    .TRADEFLAG19(bigData + ThreadLocalRandom.current().nextInt())
                    .TRADEFLAG20(bigData + ThreadLocalRandom.current().nextInt())
                    .build();
            ObjectMapper oMapper = new ObjectMapper();
            Map objMap = oMapper.convertValue(testFtl, Map.class);
            objMap.put("_Sign_", new MacSign());
            innerMap.add(objMap);

        }

        innerMap.forEach(mp -> {
            String testTargetJson = null;
            try {
                testTargetJson = this.Obj2Json(mp, cfg.getTemplate("freemaker.ftl"));
                log.info("{}",testTargetJson);
            } catch (IOException | TemplateException e) {
                log.error("{}", e.getMessage());
            }
            TestTarget testTarget = gson.fromJson(testTargetJson, TestTarget.class);

        });

        stopwatch.stop();
        log.info("本次共执行{}条，总时间{}毫秒，平均每条耗时{}毫秒", innerMap.size(), stopwatch.elapsed(TimeUnit.MILLISECONDS), ((float) stopwatch.elapsed(TimeUnit.MILLISECONDS) / innerMap.size()));
    }
    public static void main(String[] args) throws IOException, TemplateException {

        Dto2Json dto2Json = new Dto2Json();
        dto2Json.test(100);
        dto2Json.test(100);
//        dto2Json.test(10);
//        dto2Json.test(10);

    }

    public <T> String Obj2Json(T obj, Template tpl) throws IOException, TemplateException {

        StringBuffer stringBuffer = new StringBuffer();
        StringBufferWriter stringBufferWriter = new StringBufferWriter(stringBuffer);
        tpl.process(obj, stringBufferWriter);
        stringBufferWriter.close();
        return stringBuffer.toString();

    }

    @Data
    @AllArgsConstructor
    public static class InnerTestFtl {
        private String userName;
    }

    @Data
    @Builder
    public static class TestFtlInput {
        private String size;
        private String size1;
        private String OUTSYS_TX_CODE;
        private String RET_EXPLAIN;
        private String SEND_BANK;
        private String ACCOUNT;
        private String CHNL_CODE;
        private String TRADEFLAG;
        private String SEQNO;
        private String CLR_DATE;
        private String RCV_BKNO;
        private String BUSI_CODE;
        private String TRADEFLAG1;
        private String TRADEFLAG2;
        private String TRADEFLAG3;
        private String TRADEFLAG4;
        private String TRADEFLAG5;
        private String TRADEFLAG6;
        private String TRADEFLAG7;
        private String TRADEFLAG8;
        private String TRADEFLAG9;
        private String TRADEFLAG10;
        private String TRADEFLAG11;
        private String TRADEFLAG12;
        private String TRADEFLAG13;
        private String TRADEFLAG14;
        private String TRADEFLAG15;
        private String TRADEFLAG16;
        private String TRADEFLAG17;
        private String TRADEFLAG18;
        private String TRADEFLAG19;
        private String TRADEFLAG20;
        private Map<String,String> testMap;

        private InnerTestFtl user;
        private List<InnerTestFtl> paper;

    }
}

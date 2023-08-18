package com.danebrown.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.Data;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.mvel2.MVEL;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by danebrown on 2021/12/1
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@BenchmarkMode(Mode.All)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 2)
@Threads(4)
@Fork(1)
@State(value = Scope.Benchmark)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ScriptPerformanceCompare {
    public static JexlEngine jexlEngine = new JexlEngine();
    private static Serializable s;
    private static String shell = "child.child.thirdMap.thirdName";
    private static String groovy = "return topObj." + shell;
    private static SpelExpressionParser parser = new SpelExpressionParser();

    private static String spelExp = "child.child.thirdMap['thirdName']";

    private static String jexlExp = "topObj.child.child.thirdName";

    static {
        s = MVEL.compileExpression(shell);
    }

    public static TopObj generateObj() {
        TopObj topObj = new TopObj();
        topObj.firstList.add("firstList" + UUID.randomUUID().toString());
        topObj.firstMap.put("topName", "shakyri" + UUID.randomUUID().toString());
        topObj.topIdx = ThreadLocalRandom.current().nextInt();
        topObj.topName = "topName" + UUID.randomUUID().toString();

        topObj.child.secondIdx = ThreadLocalRandom.current().nextInt();
        topObj.child.secondName = "secondName" + UUID.randomUUID().toString();
        topObj.child.secondList.add("secondList" + UUID.randomUUID().toString());
        topObj.child.secondMap.put("secondName", "maskov" + UUID.randomUUID().toString());

        topObj.child.child.thirdIdx = ThreadLocalRandom.current().nextInt();
        topObj.child.child.thirdName = "thirdName" + UUID.randomUUID().toString();
        topObj.child.child.thirdList.add("thirdList" + UUID.randomUUID().toString());
        topObj.child.child.thirdMap.put("thirdName", "brown" + UUID.randomUUID().toString());

        return topObj;
    }

    public static Object groovyPrint(Object topObj, String shell,
                                     Consumer<Long> before,
                                     Consumer<Triple<String,Long,Object> > after) {

        long start = System.currentTimeMillis();
        if (before != null) {
            before.accept(start);
        }
        Binding binding = new Binding();
        binding.setVariable("topObj", topObj);
        GroovyShell groovyShell = new GroovyShell(binding);
        Object val = groovyShell.evaluate(groovy);
        if (after != null) {
            long result = System.currentTimeMillis() - start;
            Triple<String,Long,Object> triple = Triple.of("groovy",result,val);
            after.accept(triple);
        }
        return val;
    }

    public static Object onglTest(Object topObj, String shell,
                                  Consumer<Long> before,
                                  Consumer<Triple<String,Long,Object> > after) throws OgnlException {


        long start = System.currentTimeMillis();
        if (before != null) {
            before.accept(start);
        }
        Object val = Ognl.getValue(shell, topObj);
        if (after != null) {
            long result = System.currentTimeMillis() - start;
            Triple<String,Long,Object> triple = Triple.of("ongl",result,val);
            after.accept(triple);
        }
        return val;


    }

    public static Object spelTest(Object topObj, String shell,
                                  Consumer<Long> before,
                                  Consumer<Triple<String,Long,Object>> after) {

//        long start = System.currentTimeMillis();
        long start = System.currentTimeMillis();
        if (before != null) {
            before.accept(start);
        }
        Expression thirdNameExp = parser.parseExpression(shell);
        String val = thirdNameExp.getValue(topObj, String.class);
        if (after != null) {
            long result = System.currentTimeMillis() - start;
            Triple<String,Long,Object> triple = Triple.of("spel",result,val);
            after.accept(triple);
        }
        return val;
    }

    public static Object mvelTest(Object topObj, Serializable shell,
                                  Consumer<Long> before,
                                  Consumer<Triple<String,Long,Object>> after) {


        long start = System.currentTimeMillis();
        if (before != null) {
            before.accept(start);
        }

        Object val = MVEL.executeExpression(shell, topObj);
        if (after != null) {
            long result = System.currentTimeMillis() - start;
            Triple<String,Long,Object> triple = Triple.of("mvel",result,val);
            after.accept(triple);
        }
        return val;
    }

    public static Object jexlTest(Object topObj, String shell,
                                  Consumer<Long> before,
                                  Consumer<Triple<String,Long,Object>> after) {
        long start = System.currentTimeMillis();
        if (before != null) {
            before.accept(start);
        }

        org.apache.commons.jexl2.Expression expression = jexlEngine.createExpression(shell);
        JexlContext jexlContext = new MapContext();

        jexlContext.set("topObj", topObj);
        Object val = expression.evaluate(jexlContext);
        if (after != null) {
            long result = System.currentTimeMillis() - start;
            Triple<String,Long,Object> triple = Triple.of("jexl",result,val);
            after.accept(triple);
        }
        return val;

    }

//    static Consumer<Long> begin = aLong -> System.out.println("开始时间:" + aLong);
    static Consumer<Long> begin = null;
//    static Consumer<Triple<String, Long,Object>> finish = triple -> System.out.println(triple.getLeft()+"耗时:" + triple.getMiddle() + "毫秒,值为" + triple.getRight());

    static Consumer<Triple<String, Long,Object>> finish = null;

    @Benchmark
    public static void groovyTestAccessor(Blackhole blackhole){
        TopObj topObj = generateObj();
        blackhole.consume(groovyPrint(topObj, shell, begin, finish));
    }
    @Benchmark
    public static void onglTestAccessor(Blackhole blackhole) throws OgnlException {
        TopObj topObj = generateObj();
        blackhole.consume(onglTest(topObj, shell, begin, finish));
    }
    @Benchmark
    public static void spelTestAccessor(Blackhole blackhole){

        TopObj topObj = generateObj();
        blackhole.consume(spelTest(topObj, spelExp, begin, finish));
    }
//    @Benchmark
    public static void mvelTestAccessor(Blackhole blackhole){
        TopObj topObj = generateObj();
        blackhole.consume(mvelTest(topObj, s, begin, finish));

    }
    @Benchmark
    public static void jexlTestAccessor(Blackhole blackhole){
        TopObj topObj = generateObj();
        blackhole.consume(jexlTest(topObj, jexlExp, begin, finish));
    }

    public static void main(String[] args) throws OgnlException, RunnerException {
//        groovyTestAccessor();
//        onglTestAccessor();
//        spelTestAccessor();
//        mvelTestAccessor();
//        jexlTestAccessor();

        Options opt = new OptionsBuilder()
                .include(ScriptPerformanceCompare.class.getSimpleName())
                .result("result.json")
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
//        TopObj topObj = generateObj();
//        groovyPrint(topObj, shell, begin, finish);
//        topObj = generateObj();
//        groovyPrint(topObj, shell, begin, finish);
//        topObj = generateObj();
//        groovyPrint(topObj, shell, begin, finish);
//        System.out.println("===");
//        topObj = generateObj();
//        onglTest(topObj, shell, begin, finish);
//        topObj = generateObj();
//        onglTest(topObj, shell, begin, finish);
//        topObj = generateObj();
//        onglTest(topObj, shell, begin, finish);
//        System.out.println("===");
//        topObj = generateObj();
//        String spelExp = "child.child.thirdMap['thirdName']";
//        spelTest(topObj, spelExp, begin, finish);
//        topObj = generateObj();
//        spelTest(topObj, spelExp, begin, finish);
//        topObj = generateObj();
//        spelTest(topObj, spelExp, begin, finish);
//        System.out.println("===");
//        topObj = generateObj();
//        mvelTest(topObj, s, begin, finish);
//        topObj = generateObj();
//        mvelTest(topObj, s, begin, finish);
//        topObj = generateObj();
//        mvelTest(topObj, s, begin, finish);
//        System.out.println("===");
//        topObj = generateObj();
//        jexlTest(topObj, "topObj.child.child.thirdName", begin, finish);
//        topObj = generateObj();
//        jexlTest(topObj, "topObj.child.child.thirdName", begin, finish);
//        topObj = generateObj();
//        jexlTest(topObj, "topObj.child.child.thirdName", begin, finish);
    }

    @Data
    public static class TopObj {
        private SecondObj child = new SecondObj();
        private String topName;
        private int topIdx;
        private List<String> firstList = new ArrayList<>();
        private Map<String, String> firstMap = new HashMap<>();
    }

    @Data
    public static class SecondObj {
        private ThirdObj child = new ThirdObj();
        private String secondName;
        private int secondIdx;
        private Map<String, String> secondMap = new HashMap<>();
        private List<String> secondList = new ArrayList<>();
    }

    @Data
    public static class ThirdObj {
        private String thirdName;
        private int thirdIdx;
        private List<String> thirdList = new ArrayList<>();
        private Map<String, String> thirdMap = new HashMap<>();
    }
}

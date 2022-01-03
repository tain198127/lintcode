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
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

/**
 * Created by danebrown on 2021/12/1
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
public class ScriptPerformanceCompare {
    private static Serializable s;
    private static String shell ="child.child.thirdMap.thirdName";
    private static String groovy = "return topObj."+shell;
    static {
        s = MVEL.compileExpression(shell);
    }
    @Data
    public static class TopObj{
        private SecondObj child = new SecondObj();
        private String topName;
        private int topIdx;
        private List<String> firstList = new ArrayList<>();
        private Map<String, String> firstMap = new HashMap<>();
    }
    @Data
    public static class SecondObj{
        private ThirdObj child = new ThirdObj();
        private String secondName;
        private int secondIdx;
        private Map<String,String> secondMap = new HashMap<>();
        private List<String> secondList = new ArrayList<>();
    }
    @Data
    public static class ThirdObj{
        private String thirdName;
        private int thirdIdx;
        private List<String> thirdList = new ArrayList<>();
        private Map<String,String> thirdMap = new HashMap<>();
    }
    public static TopObj generateObj(){
        TopObj topObj = new TopObj();
        topObj.firstList.add("firstList"+ UUID.randomUUID().toString());
        topObj.firstMap.put("topName","shakyri"+ UUID.randomUUID().toString());
        topObj.topIdx = ThreadLocalRandom.current().nextInt();
        topObj.topName="topName"+ UUID.randomUUID().toString();

        topObj.child.secondIdx = ThreadLocalRandom.current().nextInt();
        topObj.child.secondName="secondName"+ UUID.randomUUID().toString();
        topObj.child.secondList.add("secondList"+ UUID.randomUUID().toString());
        topObj.child.secondMap.put("secondName","maskov"+ UUID.randomUUID().toString());

        topObj.child.child.thirdIdx=ThreadLocalRandom.current().nextInt();
        topObj.child.child.thirdName = "thirdName"+ UUID.randomUUID().toString();
        topObj.child.child.thirdList.add("thirdList"+ UUID.randomUUID().toString());
        topObj.child.child.thirdMap.put("thirdName","brown"+ UUID.randomUUID().toString());

        return topObj;
    }
    public static long groovyPrint(Object topObj, String shell,
                                   Consumer<Long> before,
                                   Consumer<Pair<Long,Object>> after){

        long start = System.currentTimeMillis();
        Binding binding = new Binding();
        binding.setVariable("topObj",topObj);
        GroovyShell groovyShell = new GroovyShell(binding);
        Object val = groovyShell.evaluate(groovy);
        long end = System.currentTimeMillis();
        long result = end-start;
        System.out.println("groovyPrint耗时:"+result+"毫秒，值是"+val);

        return result;
    }

    public static long onglTest(Object topObj, String shell,
                                Consumer<Long> before,
                                Consumer<Pair<Long,Object>> after) throws OgnlException {

        long start = System.currentTimeMillis();

        Object val = Ognl.getValue(shell,topObj);
        long end = System.currentTimeMillis();
        long result = end-start;

        System.out.println("onglTest耗时:"+result+"毫秒，值是"+val);
        return result;

    }

    public static long spelTest(Object topObj, String shell,
                                Consumer<Long> before,
                                Consumer<Pair<Long,Object>> after){

        long start = System.currentTimeMillis();
        EvaluationContext context = new StandardEvaluationContext(topObj);
        ExpressionParser methodCall = new SpelExpressionParser();
        Expression methodExpr = methodCall.parseExpression(shell);
        Object val =  methodExpr.getValue(context);
        long end = System.currentTimeMillis();
        long result = end-start;

        System.out.println("spelTest耗时:"+result+"毫秒,值为"+val);
        return result;
    }

    public static long mvelTest(Object topObj, Serializable shell,
                                Consumer<Long> before,
                                Consumer<Pair<Long,Object>> after){


        long start = System.currentTimeMillis();


        Object val = MVEL.executeExpression(shell,topObj);

        long end = System.currentTimeMillis();
        long result = end-start;

        System.out.println("mvelTest耗时:"+result+"毫秒,值为"+val);
        return result;
    }

    public static long jexlTest(Object topObj, String shell,
                                Consumer<Long> before,
                                Consumer<Pair<Long,Object>> after){
        long start = System.currentTimeMillis();


        JexlEngine jexlEngine = new JexlEngine();
        org.apache.commons.jexl2.Expression expression = jexlEngine.createExpression(shell);
        JexlContext jexlContext = new MapContext();

        jexlContext.set("topObj",topObj);
        Object val = expression.evaluate(jexlContext);
        long end = System.currentTimeMillis();
        long result = end-start;

        System.out.println("jexlTest耗时:"+result+"毫秒,值为"+val);
        return result;
    }
    public static void main(String[] args) throws OgnlException {
        TopObj topObj = generateObj();
        groovyPrint(topObj,shell,null,null);
        topObj = generateObj();
        groovyPrint(topObj,shell,null,null);
        topObj = generateObj();
        groovyPrint(topObj,shell,null,null);
        System.out.println("===");
        topObj = generateObj();
        onglTest(topObj,shell,null,null);
        topObj = generateObj();
        onglTest(topObj,shell,null,null);
        topObj = generateObj();
        onglTest(topObj,shell,null,null);
        System.out.println("===");
//        topObj = generateObj();
//        spelTest(topObj,shell,null,null);
//        topObj = generateObj();
//        spelTest(topObj,shell,null,null);
//        topObj = generateObj();
//        spelTest(topObj,shell,null,null);
//        System.out.println("===");
        topObj = generateObj();
        mvelTest(topObj,s,null,null);
        topObj = generateObj();
        mvelTest(topObj,s,null,null);
        topObj = generateObj();
        mvelTest(topObj,s,null,null);
        System.out.println("===");
        topObj = generateObj();
        jexlTest(topObj,"topObj.child.child.thirdName",null,null);
        topObj = generateObj();
        jexlTest(topObj,"topObj.child.child.thirdName",null,null);
        topObj = generateObj();
        jexlTest(topObj,"topObj.child.child.thirdName",null,null);
    }
}

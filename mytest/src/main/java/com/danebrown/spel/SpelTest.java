package com.danebrown.spel;

import com.danebrown.reactor.ReactorMain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by danebrown on 2021/11/25
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Log4j2
@SpringBootApplication(scanBasePackages = {"com.danebrown.spel"})
public class SpelTest implements CommandLineRunner, ApplicationListener<ApplicationReadyEvent> {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ReactorMain.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(SpelTest.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("startup");
    }
    //调用静态方法
    public void invokeBySpel(DataName dataName, String property, String method) {


        EvaluationContext context = new StandardEvaluationContext(dataName);
        context.setVariable("txHeader", dataName);


        String invokeStatic1 = method + "({#txHeader." + property + "})";
        System.out.println(invokeStatic1);
        log.info("{}", invokeStatic1);
        ExpressionParser methodCall = new SpelExpressionParser();
        Expression methodExpr = methodCall.parseExpression(invokeStatic1);
        methodExpr.getValue(context);


    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        DataName dataName = new DataName("张三", "13");
        String invokeStatic = "T(com.danebrown.spel.StaticClass).setNameCtx";
        invokeBySpel(dataName, "name", invokeStatic);

        String invokeStaticAge = "T(com.danebrown.spel.StaticClass).setAgtCtx";
        invokeBySpel(dataName, "age", invokeStaticAge);

        String invokectxName = "";
        String invokeCtxAge = "";
        HashMap<String,String> wrapper = new HashMap<>();
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("txHeader", dataName);
        context.setVariable("ctx",wrapper);
        invokeByObj(context,"name","wrapperName");
        invokeByObj(context,"age","wrapperAge");
        log.error("{}",wrapper);


    }
    //调用动态对象
    private void invokeByObj(EvaluationContext context,
                             String sourceProperties, String targetProperties){


        String invokeSpel =
                "#ctx['"+targetProperties+"']={#txHeader."+sourceProperties+
                        "}";
        System.out.println("调用对象spel:"+invokeSpel);
        ExpressionParser methodCall = new SpelExpressionParser();
        Expression methodExpr = methodCall.parseExpression(invokeSpel);
        methodExpr.getValue(context);

    }
    @Data
    @AllArgsConstructor
    public static class DataName {
        private String name;
        private String age;
    }
}

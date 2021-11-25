package com.danebrown.spel;

import com.danebrown.reactor.ReactorMain;
import com.danebrown.spring.multitirecache.MultiTireCacheTest;
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

/**
 * Created by danebrown on 2021/11/25
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Log4j2
@SpringBootApplication(scanBasePackages = {"com.danebrown.spel"})
public class SpelTest implements CommandLineRunner, ApplicationListener<ApplicationReadyEvent> {
    @Data
    @AllArgsConstructor
    public static class DataName{
        private String name;
        private String age;
    }

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ReactorMain.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(SpelTest.class, args);
    }
    @Override
    public void run(String... args) throws Exception {
        System.out.println("startup");
    }

public void invokeBySpel(DataName dataName,String property,String method){


    EvaluationContext context = new StandardEvaluationContext(dataName);
    context.setVariable("txHeader",dataName);
//    String name = (String) exp.getValue(context);


//    System.out.println("使用SPEL获取DataName的name字段:"+name);


//    String invokeStatic =
//            method+"(\""+name+"\")";

    String invokeStatic1 =
            method+"({#txHeader."+property+"})";

    ExpressionParser methodCall = new SpelExpressionParser();
    Expression methodExpr = methodCall.parseExpression(invokeStatic1);
    methodExpr.getValue(context);


//    ExpressionParser methodCall1 = new SpelExpressionParser();
//    EvaluationContext context1 = new StandardEvaluationContext(dataName);
//    Expression methodExpr1 = methodCall1.parseExpression("T(com.danebrown.spel.StaticClass)");
//
//    methodExpr1.setValue(property,name);

}
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        DataName dataName = new DataName("张三","13");
        String invokeStatic =
                "T(com.danebrown.spel.StaticClass).setNameCtx";
        invokeBySpel(dataName,"name",invokeStatic);

        String invokeStaticAge =
                "T(com.danebrown.spel.StaticClass).setAgtCtx";
        invokeBySpel(dataName,"age",invokeStaticAge);


    }
}

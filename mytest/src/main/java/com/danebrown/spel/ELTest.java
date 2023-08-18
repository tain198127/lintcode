package com.danebrown.spel;

import org.mvel2.MVEL;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * EL表达式性能对比，以及最佳实践
 */
public class ELTest {

    public static void main(String[] args) {
        DataContext dataName = new DataContext("张三", "13");
        String property = "name";
        String spName = SpElTest(dataName, property);
        String name = MvelTest(dataName, property);
        System.out.printf("spel:%s,mvel:%s",spName,name);
    }
    static SpelExpressionParser parser = new SpelExpressionParser();
    public static String SpElTest(DataContext dataName, String property){
        Expression nameExpression = parser.parseExpression(property);
        String name = nameExpression.getValue(dataName, String.class);
        return name;
    }

    public static String MvelTest(DataContext dataName, String property){
        return MVEL.getProperty(property,dataName).toString();
    }
}

package com.danebrown.reactor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by danebrown on 2021/6/24
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ConsoleMenu {
    String name() ;
    int order() default 0;
    String desc() ;

}

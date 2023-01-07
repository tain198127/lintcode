package com.danebrown.spring.multitirecache.simplebean;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by danebrown on 2021/10/25
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Log4j2
@Component
@Order(2)
public class SimpleCycleDependBeanB {

    public SimpleCycleDependBeanB(){
        log.warn("is init");
    }
    @Autowired
    private SimpleCycleDependBeanC c;
    @MultiTireCache("B")
    public void sayHello(){
        log.warn("hello B");
        c.sayHello();
    }
}

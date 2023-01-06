package com.danebrown.spring.multitirecache.simplebean;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by danebrown on 2021/10/25
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Log4j2

public class BeanA {
    @Autowired
    private BeanB b;
    @MultiTireCache("A")
    public void sayHello(){
        log.warn("hello A");
        b.sayHello();
    }
    public void sayByCHello(String name){
        log.warn(name);
    }
}

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
public class BeanB {


    @Autowired
    private BeanC c;
    @MultiTireCache("B")
    public void sayHello(){
        log.warn("hello B");
        c.sayHello();
    }
}

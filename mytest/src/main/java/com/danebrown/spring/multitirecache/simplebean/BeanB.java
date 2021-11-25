package com.danebrown.spring.multitirecache.simplebean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by danebrown on 2021/10/25
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Component("beanB")
public class BeanB {


    @Autowired
    private BeanC c;

    public void sayHello(){
        System.out.println("hello B");
        c.sayHello();
    }
}

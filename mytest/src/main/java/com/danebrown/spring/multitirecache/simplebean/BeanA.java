package com.danebrown.spring.multitirecache.simplebean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by danebrown on 2021/10/25
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Component("beanA")
public class BeanA {
    @Autowired
    private BeanB b;

    public void sayHello(){
        System.out.println("hello A");
        b.sayHello();
    }
    public void sayByCHello(String name){
        System.out.println(name);
    }
}

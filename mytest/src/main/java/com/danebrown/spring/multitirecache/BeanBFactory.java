//package com.danebrown.spring.multitirecache;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.FactoryBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.stereotype.Component;
//
///**
// * Created by danebrown on 2021/10/25
// * mail: tain198127@163.com
// *
// * @author danebrown
// */
//@Component
//public class BeanBFactory implements FactoryBean<BeanB>  {
//
//    @Autowired
//    private BeanC c;
//
//    @Override
//    public BeanB getObject() throws Exception {
//        BeanB b = new BeanB();
//        b.setC(c);
//        return b;
//    }
//
//
//    @Override
//    public Class<?> getObjectType() {
//        return BeanB.class;
//    }
//
//
//}

package com.danebrown.spring.multitirecache;

import com.danebrown.spring.multitirecache.simplebean.BeanA;
import com.danebrown.spring.multitirecache.simplebean.BeanB;
import com.danebrown.spring.multitirecache.simplebean.BeanC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by danebrown on 2021/10/25
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Configuration
public class BeanConfiguration {
    @Bean
    public BeanC getBeanC(){
        return new BeanC();
    }
    @Bean
    public BeanA getBeanA(){
        return new BeanA();
    }
    @Bean
    public BeanB getBeanB(){
        return new BeanB();
    }
}

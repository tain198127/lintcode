package com.danebrown.aop;

import com.danebrown.reactor.ReactorMain;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.EnableLoadTimeWeaving;

/**
 * Created by danebrown on 2021/12/24
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Log4j2
@SpringBootApplication(scanBasePackages = {"com.danebrown.aop"})
//@EnableLoadTimeWeaving
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AopTestRunner implements CommandLineRunner {
    @Autowired
    IAopTest iAopTest;

    public static void main(String[] args) {
//        new SpringApplicationBuilder()
//                .web(WebApplicationType.NONE)
//                .main(AopTestRunner.class)
//                .run(args);

        SpringApplication springApplication = new SpringApplication(AopTestRunner.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(AopTestRunner.class, args);

    }

    @Override
    public void run(String... args) throws Exception {

        String sayHello = iAopTest.sayHello("danebrown");
        System.out.println(sayHello);
    }


}

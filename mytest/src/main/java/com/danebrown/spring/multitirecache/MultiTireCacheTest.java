package com.danebrown.spring.multitirecache;

import com.danebrown.reactor.ReactorMain;
import com.danebrown.spring.multitirecache.simplebean.BeanA;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * Created by danebrown on 2021/10/25
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Log4j2
@SpringBootApplication(scanBasePackages = {"com.danebrown.spring.multitirecache"})
public class MultiTireCacheTest implements CommandLineRunner,
        ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private BeanA a;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ReactorMain.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(MultiTireCacheTest.class, args);
    }
    @Override
    public void run(String... args) throws Exception {
        System.out.println("startup");
    }


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        a.sayHello();

    }


}

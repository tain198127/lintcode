package com.danebrown.spring.property;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;

/**
 * Created by danebrown on 2022/2/15
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@SpringBootApplication
@Log4j2
public class TestMain implements CommandLineRunner, ApplicationListener<ApplicationEvent> {
    @Autowired
    private PropertiesTest propertiesTest;
    @Autowired
    ApplicationContext applicationContext;
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(TestMain.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }

    @Override
    public void run(String... args) throws Exception {

    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof ApplicationReadyEvent){
            log.info("{}",propertiesTest);
//            PropertiesTest propertiesTest1=
//                    applicationContext.getBean(PropertiesTest.class);
            ConfigurableEnvironment env =
                    (ConfigurableEnvironment) applicationContext.getEnvironment();
            env.getPropertySources().addLast(new ResourcePropertySource(new ClassPathResource("app.properties")));
            applicationContext.getAutowireCapableBeanFactory().destroyBean(propertiesTest);
            applicationContext.getAutowireCapableBeanFactory().initializeBean(propertiesTest,"propertiesTest");

        }
    }
}

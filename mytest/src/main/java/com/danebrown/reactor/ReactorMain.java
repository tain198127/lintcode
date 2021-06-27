package com.danebrown.reactor;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import reactor.core.publisher.Hooks;

/**
 * Created by danebrown on 2021/6/27
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@SpringBootApplication
@Log4j2
public class ReactorMain implements CommandLineRunner, ApplicationListener<ApplicationEvent> {
    @Autowired
    ReactorOneByOne reactorOneByOne;

    public static void main(String[] args) {
        SpringApplication.run(ReactorMain.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Hooks.onOperatorDebug();
        Hooks.enableContextLossTracking();
        reactorOneByOne.starting();
    }


    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        log.warn("接收所有类型事件{}-->事件源:{}",event.getClass().getSimpleName(),
                event.getSource().getClass().getSimpleName());
    }
}
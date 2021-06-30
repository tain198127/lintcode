package com.danebrown.reactor;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

/**
 * Created by danebrown on 2021/6/28
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Log4j2
public class ReactorMainListener implements SpringApplicationRunListener
{
    public ReactorMainListener(SpringApplication application, String[]  args){
        log.warn("init");
    }
    @Override
    public void starting() {
        log.warn("SpringApplicationRunListener->ReactorMainListener" +
                "-->starting");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        log.warn("SpringApplicationRunListener->ReactorMainListener-->environmentPrepared");
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        log.warn("SpringApplicationRunListener->ReactorMainListener-->contextPrepared");
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        log.warn("SpringApplicationRunListener->ReactorMainListener-->contextLoaded");
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        log.warn("SpringApplicationRunListener->ReactorMainListener-->started");
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        log.warn("SpringApplicationRunListener->ReactorMainListener-->running");
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        log.warn("SpringApplicationRunListener->ReactorMainListener-->failed");
    }


}

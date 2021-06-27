package com.danebrown.reactor;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Created by danebrown on 2021/6/28
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Log4j2
public class ReactorMainListener implements SpringApplicationRunListener {
    public ReactorMainListener(SpringApplication application, String[]  args){
        log.warn("init");
    }
    @Override
    public void starting() {
        log.warn("ReactorMain-->starting");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        log.warn("ReactorMain-->environmentPrepared");
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        log.warn("ReactorMain-->contextPrepared");
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        log.warn("ReactorMain-->contextLoaded");
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        log.warn("ReactorMain-->started");
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        log.warn("ReactorMain-->running");
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        log.warn("ReactorMain-->failed");
    }
}

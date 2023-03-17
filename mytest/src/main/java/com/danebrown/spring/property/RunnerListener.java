package com.danebrown.spring.property;


import groovy.util.logging.Slf4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class RunnerListener {
    @EventListener
    public void starting(ApplicationStartingEvent e){
        System.out.println("starting:"+e.getSource());
    }
    @EventListener
    public void contextInitialized(ApplicationContextInitializedEvent event){
        System.out.println("contextInitialized:"+event.getSource());
    }
    @EventListener
    public void environmentPrepared(ApplicationEnvironmentPreparedEvent event){
        System.out.println("environmentPrepared:"+event.getSource());

    }
    @EventListener
    public void filed(ApplicationFailedEvent event){
        System.out.println("filed:"+event.getSource());

    }
    @EventListener
    public void prepare(ApplicationPreparedEvent event){
        System.out.println("prepare:"+event.getSource());

    }
    @EventListener
    public void ready(ApplicationReadyEvent event){
        System.out.println("ready:"+event.getSource());

    }
    @EventListener
    public void started(ApplicationStartedEvent event){
        System.out.println("started:"+event.getSource());

    }

}

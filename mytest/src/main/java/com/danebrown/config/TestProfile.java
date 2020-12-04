package com.danebrown.config;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by danebrown on 2020/11/13
 * mail: tain198127@163.com
 */
//@PropertySource(value = "TestProfile.properties")
//@ConfigurationProperties(ignoreInvalidFields = true, prefix = "com.test")
//@EnableConfigurationProperties
@Data
//@Log4j2
@SpringBootApplication(scanBasePackages = "com.danebrown")
@Slf4j
public class TestProfile {
//    private static LoggerContext loggerContext = (LoggerContext) org.slf4j.LoggerFactory.getILoggerFactory();
//
//    static {
//        loggerContext.getLogger("TestProfile").setLevel(Level.INFO);
//
//    }
    private static void test(){

        TestSvc svc = new TestSvc();
        svc.setKey("12121212");
        svc.setKey1("2323232");

        log.trace("{}",svc);
    }
    public static void main(String[] args) {
        SpringApplication.run(TestProfile.class,args);
        test();

    }

}

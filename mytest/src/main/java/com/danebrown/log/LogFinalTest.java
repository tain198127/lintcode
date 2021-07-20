package com.danebrown.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CountDownLatch;

/**
 * Created by danebrown on 2021/7/18
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@SpringBootApplication
@Slf4j
public class LogFinalTest implements CommandLineRunner {
    @Autowired
    AsyncLogProcess asyncLogProcess;
    @Autowired
    SyncLogProcess syncLogProcess;
    @Autowired
    JCToolLogProcess jcToolLogProcess;
    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public static void main(String[] args) {
        new SpringApplicationBuilder(LogFinalTest.class).web(WebApplicationType.NONE).run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("ok");
        CountDownLatch countDownLatch = new CountDownLatch(3);
        threadPoolTaskExecutor.execute(() -> {
            asyncLogProcess.runLog(1);
            asyncLogProcess.runDebugLog(1);
            countDownLatch.countDown();
        });
        threadPoolTaskExecutor.execute(() -> {
            syncLogProcess.runLog(1);
            syncLogProcess.runDebugLog(1);
            countDownLatch.countDown();
        });
        threadPoolTaskExecutor.execute(() -> {
            jcToolLogProcess.runLog(1);
            jcToolLogProcess.runDebugLog(1);
            countDownLatch.countDown();

        });
        countDownLatch.await();
        threadPoolTaskExecutor.destroy();
    }
}

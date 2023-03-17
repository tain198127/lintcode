package com.danebrown.juc;

import cn.hutool.core.lang.caller.SecurityManagerCaller;
import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.util.logging.Log4j2;
import io.netty.util.concurrent.FastThreadLocalThread;
import lombok.SneakyThrows;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.bytedeco.opencv.presets.opencv_core;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.reflections.Reflections.log;

@Log4j2
@SpringBootApplication(scanBasePackages = {"com.danebrown.juc"})
@EnableScheduling
@EnableAsync
public class ThreadPoolTest implements CommandLineRunner {
    @Autowired
    ConfigurableApplicationContext context;
    int corePoolSize = 1;
    int maximumPoolSize = 5;
    int keepAliveTime = 10;
    TimeUnit timeUnit = TimeUnit.SECONDS;
    BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(10, false);
    ThreadFactory threadFactory = new CustomDefThreadFactory("TestPool", true, 5);
    RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            if (executor.isShutdown()) {
                throw new RejectedExecutionException();
            }
            try {
                Field field = r.getClass().getDeclaredField("callable");
                field.setAccessible(true);
                Callable callable = (Callable) field.get(r);
                Field invocationField = callable.getClass().getDeclaredField("this$1");
                invocationField.setAccessible(true);
                log.warn("拒绝策略拒绝任务:{}", new ObjectMapper().writeValueAsString(callable));
            }catch (Exception e){
                log.error("{}",e.getMessage());
                throw new RejectedExecutionException(e);
            }
//            throw new RejectedExecutionException();

        }
    };
    ExecutorService ex = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, workQueue, threadFactory, rejectedExecutionHandler);

    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication(ThreadPoolTest.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(ThreadPoolTest.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Runnable runnable = new Runnable() {
            volatile boolean isShutdown=false;
            @Override
            public void run() {
                while (!isShutdown) {
                    System.out.println("请输入选项，0位结束，其他表示运行次数");
                    Scanner scanner = new Scanner(System.in);
                    while (scanner.hasNextLine()) {
                        String input = scanner.next();
                        int i = StringUtils.isNumeric(input) ? Integer.parseInt(input) : -1;
                        if (i == 0) {
                            isShutdown=true;
                            ex.shutdown();
                            scanner.close();
                            try {
                                ex.awaitTermination(3, TimeUnit.SECONDS);
                            } catch (InterruptedException e) {

                            } finally {
                                System.err.println("bye~");
                                context.close();
                            }
                            break;
                        }
                        if (i < 0) {
                            System.err.printf("错误的输入");
                            continue;
                        }

                        try {
                            List<CustomRunnable<String>> tasks = new ArrayList<>();
                            for (int k = 0; k < i; k++) {
                                CustomRunnable<String> r = new CustomRunnable<String>("Name" + k, "Age" + k) {

                                    @Override
                                    public String call() throws Exception {
                                        FastThreadLocalThread.sleep(1000);
                                        log.info("运行结束:name:{};age:{}",this.getName(),this.getAge());
                                        return UUID.randomUUID().toString();
                                    }
                                };
                                tasks.add(r);
                            }
                            ex.invokeAll(tasks,30,TimeUnit.SECONDS);
                            System.out.println("执行结束");
                        } catch (Throwable e) {

                        }finally {
                            continue;
                        }
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.setName("INPUT_THREAD");
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();

    }

    @Scheduled(cron = "*/3 * * * * *")
    public void displayThreadPool() {
        ThreadPoolExecutor e = (ThreadPoolExecutor) ex;
        log.info(
                        "ActiviteCount:{};" +
                        "TaskCount:{};" +
                        "PoolSize:{};" +
                        "completedTask:{};" +
                        "queueRemainCapcity:{};" +
                        "queueSize:{};" +
                        "MaxPoolSize:{}",
                e.getActiveCount(),
                e.getTaskCount(),
                e.getPoolSize(),
                e.getCompletedTaskCount(),
                e.getQueue().remainingCapacity(),
                e.getQueue().size(),
                e.getMaximumPoolSize()

        );
    }

    public abstract static class CustomRunnable<T> implements Callable<T> {
        public CustomRunnable(String name, String age){
            this.name = name;
            this.age = age;
        }
        private String name;
        private String age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }

    /**
     * 自定义的threadPool
     */
    public static class CustomDefThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolId = new AtomicInteger();
        protected final ThreadGroup threadGroup;
        private final AtomicInteger nextId = new AtomicInteger();
        private final String prefix;
        private final boolean daemon;
        private final int priority;

        public CustomDefThreadFactory(String poolName, boolean daemon, int priority) {
            this.prefix = poolName + '-' + poolId.incrementAndGet() + '-';
            this.daemon = daemon;
            this.priority = (priority < Thread.MIN_PRIORITY || priority > Thread.MAX_PRIORITY) ? Thread.NORM_PRIORITY : priority;
            this.threadGroup = System.getSecurityManager() == null ?
                    Thread.currentThread().getThreadGroup() : System.getSecurityManager().getThreadGroup();

        }

        @Override
        public Thread newThread(@NotNull Runnable runnable) {
            Thread t = new Thread(this.threadGroup, runnable, prefix + nextId.incrementAndGet());
            try {
                if (t.isDaemon() != this.daemon) {
                    t.setDaemon(this.daemon);
                }

                if (t.getPriority() != this.priority) {
                    t.setPriority(this.priority);
                }
            } catch (Exception var4) {
            }

            return t;
        }
    }
}

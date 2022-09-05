package com.danebrown.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.ErrorEntryFreeException;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreaker;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.EventObserverRegistry;
import com.alibaba.csp.sentinel.util.TimeUtil;
import com.alibaba.druid.util.DaemonThreadFactory;
import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * Created by danebrown on 2020/9/15
 * mail: tain198127@163.com
 */
@Log4j2
public class BasicSlowSentinel {
    public static String KEY = "degrade";
    private static volatile AtomicLong FIRST_TIME_OUT_START=new AtomicLong(0);
    private static volatile AtomicLong FIRST_TOUCH_GRADE=new AtomicLong(0);
    private static void registerStateChangeObserver() {
        EventObserverRegistry.getInstance().addStateChangeObserver("logging", (prevState, newState, rule, snapshotValue) -> {
            if (newState == CircuitBreaker.State.OPEN) {
                log.error(String.format("%s -> OPEN at %d, snapshotValue=%.2f", prevState.name(), TimeUtil.currentTimeMillis(), snapshotValue));
            } else if (newState == CircuitBreaker.State.HALF_OPEN) {

                log.error(String.format("%s ->  HALF_OPEN %s at %d", prevState.name(), newState.name(), TimeUtil.currentTimeMillis()));
            } else {
                log.error(String.format("%s -> %s at %d", prevState.name(), newState.name(), TimeUtil.currentTimeMillis()));
            }
        });
    }

    public static void initFlowRules() {
                

        List<DegradeRule> degradeRules = new ArrayList<>();
        
        DegradeRule rule = new DegradeRule(KEY)
                .setGrade(CircuitBreakerStrategy.SLOW_REQUEST_RATIO.getType())
                // Max allowed response time
                .setCount(30)
                // Retry timeout (in second)
                .setTimeWindow(10)
                // Circuit breaker opens when slow request ratio > 60%
                .setSlowRatioThreshold(0.6)
                .setMinRequestAmount(1)
                .setStatIntervalMs(20000);
        degradeRules.add(rule);
        DegradeRuleManager.loadRules(degradeRules);



    }
    public static AtomicInteger index = new AtomicInteger(0);
    public static void main(String[] args) throws BlockException, InterruptedException {
        BasicSlowSentinel basicSentinel = new BasicSlowSentinel();
        BasicSlowSentinel.initFlowRules();
        registerStateChangeObserver();
//        basicSentinel.testDegrade();
        ExecutorService es = Executors.newCachedThreadPool(new DaemonThreadFactory("test"));

        for(int i=0;i< 10;i++){
            int finalI = i;
            es.execute(()->{
                try {
                    basicSentinel.testDegrade(finalI);
                } catch (BlockException e) {
                    log.error(e);
                }
            });
            Thread.sleep(100);
        }
        es.shutdown();
        es.awaitTermination(1, TimeUnit.SECONDS);
        System.err.println("结束了");
        log.error("超时逃逸时间:{}",FIRST_TOUCH_GRADE.get() - FIRST_TIME_OUT_START.get()); 
//        System.exit(0);
        return;
    }

    /**
     * slot
     */


    //    static {
    //        ServiceLoader<InitFunc> loader = ServiceLoaderUtil.getServiceLoader(InitFunc.class);
    //        log.info(loader.toString());
    //    }
    @SentinelResource("HelloWorld")
    public String hello(String age) {
        return "hello" + age;
    }

    @SentinelResource("degrade")
    public String degrade(String age) {
        return "degrade hello" + age;
    }
    

    public void testDegrade(int batch) throws BlockException {
        long beginTime = System.currentTimeMillis();
        long endTime = -1;
        int timeout = 50;
        
        Function<Integer, Mono<String>> fn = time->Mono.fromCallable(()->{
            log.info("do some long timed work");
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("work has completed");
            return "OK"+index.incrementAndGet()+time;
        });
        for (int i = 0; i < 500000; i++) {
            Entry entry = null;
            try {
                entry = SphU.entry(KEY);
                int sleep = ThreadLocalRandom.current().nextInt(1200,1500);
                Entry finalEntry = entry;
                int finalI = i;
                Mono<String> m = Mono.fromCallable(() -> {
                    return "ok"+index.incrementAndGet();
                })
                        .then(fn.apply(sleep))
                        .timeout(Duration.ofMillis(80),Mono.from(new Publisher<String>() {
                            @Override
                            public void subscribe(Subscriber<? super String> s) {
                                FIRST_TIME_OUT_START.updateAndGet(operand -> {
                                    if(operand == 0){
                                        long time = System.currentTimeMillis();
                                        log.error("第一次触发超时时间:"+time);
                                        return time;
                                    }
                                    return operand;
                                });
                                TimeoutException e = new TimeoutException("时间超过"+timeout);
                                log.warn("第{}批次.捕获超时耗时:{},超时判定:{};循环第{}次",batch,System.currentTimeMillis()-beginTime,timeout, finalI);
                                Tracer.traceEntry(e, finalEntry);
                                finalEntry.exit();
                            }
                        }))
                        
                        ;
                        
//                log.info(m.block(Duration.ofMillis(sleep)));
                m.subscribe();
            }
            catch (BlockException ex){
                FIRST_TOUCH_GRADE.updateAndGet(operand -> {
                    if(operand == 0){
                        long time = System.currentTimeMillis();
                        log.error("第一次触发Degrade时间:{};超时逃逸时间:{}(毫秒)",time,time-FIRST_TIME_OUT_START.get());
                        return time;
                    }
                    return operand;
                });
                if(endTime == -1){
                    endTime = System.currentTimeMillis();
                    log.warn("第一次触发耗时：{}",endTime-beginTime);
                }
//                log.error("异常信息:{}--->{}",ex.getRule().getResource(),ex.getMessage());
            }
            
            
            finally {
                if(entry != null) {
                    try {
                        entry.exit();
                    }catch (ErrorEntryFreeException errorEntryFreeException){
                        log.error("异常信息:{}",errorEntryFreeException);
                    }
                }
                
            }
        }
        log.warn("本次耗时：{}",endTime-beginTime);

    }
}

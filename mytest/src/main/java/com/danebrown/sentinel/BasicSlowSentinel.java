package com.danebrown.sentinel;

import com.alibaba.csp.sentinel.Entry;
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
import io.netty.util.concurrent.FastThreadLocalThread;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by danebrown on 2020/9/15
 * mail: tain198127@163.com
 */
@Log4j2
public class BasicSlowSentinel {
    public static String KEY = "degrade";

    private static void registerStateChangeObserver() {
        EventObserverRegistry.getInstance().addStateChangeObserver("logging", (prevState, newState, rule, snapshotValue) -> {
            if (newState == CircuitBreaker.State.OPEN) {
                System.err.println(String.format("%s -> OPEN at %d, snapshotValue=%.2f", prevState.name(), TimeUtil.currentTimeMillis(), snapshotValue));
            } else if (newState == CircuitBreaker.State.HALF_OPEN) {
                
                System.err.println(String.format("%s ->  HALF_OPEN %s at %d", prevState.name(), newState.name(), TimeUtil.currentTimeMillis()));
            } else {
                System.err.println(String.format("%s -> %s at %d", prevState.name(), newState.name(), TimeUtil.currentTimeMillis()));
            }
        });
    }

    public static void initFlowRules() {
                

        List<DegradeRule> degradeRules = new ArrayList<>();
        
        DegradeRule rule = new DegradeRule(KEY)
                .setGrade(CircuitBreakerStrategy.SLOW_REQUEST_RATIO.getType())
                // Max allowed response time
                .setCount(50)
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
        basicSentinel.testDegrade();
//        ExecutorService es = Executors.newCachedThreadPool(new DaemonThreadFactory("test"));
//        
//        for(int i=0;i< 1;i++){
//            es.execute(()->{
//                try {
//                    basicSentinel.testDegrade();
//                } catch (BlockException e) {
//                    log.error(e);
//                }
//            });
//        }
//        es.shutdown();
//        es.awaitTermination(1, TimeUnit.SECONDS);
        System.err.println("结束了");
        System.exit(0);
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
    

    public void testDegrade() throws BlockException {
        long beginTime = System.currentTimeMillis();
        long endTime = -1;
        for (int i = 0; i < 50; i++) {
            Entry entry = null;
            try {
                entry = SphU.entry(KEY);
                int sleep = ThreadLocalRandom.current().nextInt(400,600);
                Mono<String> m = Mono.fromCallable(() -> {
                    FastThreadLocalThread.sleep(sleep);
                    return "ok"+index.incrementAndGet();
                }).timeout(Duration.ofMillis(50), Mono.fromCallable(() -> {
                    Tracer.trace(new TimeoutException("超时了"));
                    int v = index.incrementAndGet();
                    log.warn("超时了"+v);
                    return "mock"+v;
                }), Schedulers.boundedElastic());

                log.info(m.block());
                m.subscribe();
            }
            catch (BlockException ex){
                if(endTime == -1){
                    endTime = System.currentTimeMillis();
                    log.warn("第一次触发耗时：{}",endTime-beginTime);
                }
                log.error("异常信息:{}--->{}",ex.getRule().getResource(),ex.getMessage());
            }
            finally {
                if(entry != null) {
                    entry.exit();
                }
                
            }
        }
        log.warn("本次耗时：{}",endTime-beginTime);

    }
}

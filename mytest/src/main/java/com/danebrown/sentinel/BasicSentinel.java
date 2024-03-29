package com.danebrown.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphO;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreaker;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.EventObserverRegistry;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.util.TimeUtil;
import io.netty.util.concurrent.FastThreadLocalThread;
import io.netty.util.internal.ThreadLocalRandom;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by danebrown on 2020/9/15
 * mail: tain198127@163.com
 */
@Log4j2
public class BasicSentinel {
    public static String KEY = "degrade";

    private static void registerStateChangeObserver() {
        EventObserverRegistry.getInstance().addStateChangeObserver("logging", (prevState, newState, rule, snapshotValue) -> {
            if (newState == CircuitBreaker.State.OPEN) {
                System.err.println(String.format("%s -> OPEN at %d, snapshotValue=%.2f", prevState.name(), TimeUtil.currentTimeMillis(), snapshotValue));
            } else {
                System.err.println(String.format("%s -> %s at %d", prevState.name(), newState.name(), TimeUtil.currentTimeMillis()));
            }
        });
    }

    public static void initFlowRules() {
                List<FlowRule> rules = new ArrayList<>();
                FlowRule rule = new FlowRule();
                rule.setResource("HelloWorld");
                rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
                // Set limit QPS to 20.
                rule.setCount(1);
                rules.add(rule);
                FlowRuleManager.loadRules(rules);

        List<DegradeRule> degradeRules = new ArrayList<>();
        DegradeRule degradeRule = new DegradeRule();
        degradeRule.setResource("degrade");
        degradeRule.setGrade(CircuitBreakerStrategy.ERROR_COUNT.getType());

        degradeRule.setCount(3);
        degradeRule.setTimeWindow(3);
        degradeRule.setStatIntervalMs(100);
        //        degradeRule.setMinRequestAmount(0);

        degradeRules.add(degradeRule);
        DegradeRuleManager.loadRules(degradeRules);



    }

    public static void main(String[] args) throws BlockException, InterruptedException {
        BasicSentinel basicSentinel = new BasicSentinel();
        BasicSentinel.initFlowRules();
        registerStateChangeObserver();
        ExecutorService es = Executors.newFixedThreadPool(8);
        for(int i=0;i< 8;i++){
            es.execute(()->{
                try {
                    basicSentinel.testDegrade();
                } catch (BlockException e) {
                    log.error(e);
                }
            });
        }
        es.awaitTermination(10, TimeUnit.SECONDS);
        es.shutdown();
        System.err.println("结束了");
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

    public void testFlow() throws BlockException {
        for (int t = 0; t < 1; t++) {
            int i = ThreadLocalRandom.current().nextInt(1, 10000);
            Entry entry = null;
            try {
                entry = SphU.entry("HelloWorld");
                String age = this.hello(String.valueOf(i));
                System.out.println(age);
            } catch (Exception ex) {
                System.out.println("error:" + ex.getMessage());

                throw ex;
            } finally {
                if (entry != null) {
                    entry.exit();//释放
                }
            }

        }
    }
    @Data
    public static class Context{
        Entry entry;
        Throwable throwable;
        String age;
    }
    public Context degradeEntry(int i) throws Exception {
        Context context = new Context();
        try {
            context.setEntry(SphU.entry(KEY));
            //
            String age = this.degrade(String.valueOf(i));
            context.setAge(age);
            if (i < 10) {
                context.setThrowable(new Exception("num is too small"));

            } else {
                Thread.sleep(ThreadLocalRandom.current().nextInt(100, 300));
            }

            System.out.println(age);
            return context;
        }finally {

        }

    }

    public void testDegrade() throws BlockException {
        AtomicLong indexer = new AtomicLong(0);
        for (int i = 0; i < 1000; i++) {
            Boolean isBlocked = SphO.entry("HelloWorld");
            if(isBlocked){
                System.out.println("触发限流了");
            }
            Entry entry = null;
            try {

                Context context = degradeEntry(i);
                entry = context.getEntry();
                if(context.getThrowable() != null){
                    Tracer.traceEntry(context.getThrowable(), entry);
                }
                entry.exit();


            } catch (BlockException e) {
                if (e instanceof DegradeException) {
                    System.out.println("熔断了");
                    try {
                        FastThreadLocalThread.sleep(300);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("异常");
            } catch (Throwable e) {



            } finally {
                if (entry != null) {
                    entry.exit();
                }
            }

        }


    }
}

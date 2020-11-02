package com.danebrown.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.statistic.StatisticSlotCallbackRegistry;
import com.alibaba.csp.sentinel.spi.ServiceLoaderUtil;
import com.danebrown.sentinel.extension.CustomInitFunc;
import com.danebrown.sentinel.extension.CustomSentinelProperty;
import com.danebrown.sentinel.extension.statisticcallback.CustomsProcessorSlotExitCallback;
import com.danebrown.sentinel.extension.statisticcallback.CustomsStatisticSlotCallback;
import io.netty.util.internal.ThreadLocalRandom;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Created by danebrown on 2020/9/15
 * mail: tain198127@163.com
 */
@Log4j2
public class BasicSentinel {
    /**
     * slot
     */


    static {
        ServiceLoader<InitFunc> loader = ServiceLoaderUtil.getServiceLoader(InitFunc.class);
        log.info(loader.toString());
    }

    @SentinelResource("HelloWorld")
    public String hello(String age){
        return "hello"+age;
    }
    public static void initFlowRules(){
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("HelloWorld");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 20.
        rule.setCount(20);
        rules.add(rule);
        CustomSentinelProperty basicSentinelProperty = new CustomSentinelProperty();
        FlowRuleManager.register2Property(basicSentinelProperty);
        FlowRuleManager.loadRules(rules);



    }
    public static void main(String[] args) throws BlockException {
        BasicSentinel basicSentinel = new BasicSentinel();
        BasicSentinel.initFlowRules();
        net.sf.ehcache.CacheManager cacheManager;
        for(int t = 0; t < 1; t++){
            int i = ThreadLocalRandom.current().nextInt(1,10000);
            Entry entry = null;
            try{
                entry = SphU.entry("HelloWorld");
                String age = basicSentinel.hello(String.valueOf(i));
                System.out.println(age);
            }
            catch (Exception ex){
                System.out.println("error:"+ex.getMessage());

                throw ex;
            }
            finally {
                if(entry!=null){
                    entry.exit();//释放
                }
            }

        }
    }
}

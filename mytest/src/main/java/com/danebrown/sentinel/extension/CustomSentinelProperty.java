package com.danebrown.sentinel.extension;

/**
 * Created by danebrown on 2020/9/16
 * mail: tain198127@163.com
 */

import com.alibaba.csp.sentinel.property.PropertyListener;
import com.alibaba.csp.sentinel.property.SentinelProperty;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * extend of property，一旦使用了这个类，就会把sentinel原有的property替换掉。慎重！！！
 * 用来承载propertyListener的容器
 */
@Log4j2
public class CustomSentinelProperty implements SentinelProperty<List<FlowRule>> {

    @Override
    public void addListener(PropertyListener<List<FlowRule>> propertyListener) {
        log.info("BasicSentinelProperty add listener");
    }

    @Override
    public void removeListener(PropertyListener<List<FlowRule>> propertyListener) {
        log.info("BasicSentinelProperty removeListener ");

    }

    @Override
    public boolean updateValue(List<FlowRule> flowRules) {
        log.info("BasicSentinelProperty updateValue ");

        return true;
    }
}

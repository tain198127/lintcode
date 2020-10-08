package com.danebrown.sentinel.extension;

import com.alibaba.csp.sentinel.property.PropertyListener;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * Created by danebrown on 2020/9/16
 * mail: tain198127@163.com
 */
@Log4j2
public class CustomsPropertiesListener implements PropertyListener<List<FlowRule>> {
    @Override
    public void configUpdate(List<FlowRule> value) {

    }

    @Override
    public void configLoad(List<FlowRule> value) {

    }
}

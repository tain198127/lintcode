package com.danebrown.sentinel.extension;

import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.init.InitOrder;
import com.alibaba.csp.sentinel.slots.statistic.StatisticSlotCallbackRegistry;
import com.danebrown.sentinel.extension.statisticcallback.CustomsProcessorSlotExitCallback;
import com.danebrown.sentinel.extension.statisticcallback.CustomsStatisticSlotCallback;
import lombok.extern.log4j.Log4j2;

/**
 * Created by danebrown on 2020/9/16
 * mail: tain198127@163.com
 */
@Log4j2
@InitOrder
public class CustomInitFunc implements InitFunc {
    @Override
    public void init() throws Exception {
        StatisticSlotCallbackRegistry.addEntryCallback(CustomsStatisticSlotCallback.class.getCanonicalName(), new CustomsStatisticSlotCallback());
        StatisticSlotCallbackRegistry.addExitCallback(CustomsProcessorSlotExitCallback.class.getCanonicalName(),new CustomsProcessorSlotExitCallback());
        log.info("{}","customs init");
    }
}

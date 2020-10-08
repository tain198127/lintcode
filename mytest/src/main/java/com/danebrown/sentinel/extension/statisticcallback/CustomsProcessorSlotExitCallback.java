package com.danebrown.sentinel.extension.statisticcallback;

import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.slotchain.ProcessorSlotExitCallback;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import lombok.extern.log4j.Log4j2;

/**
 * Created by danebrown on 2020/9/17
 * mail: tain198127@163.com
 * slot exit时的callback
 */
@Log4j2
public class CustomsProcessorSlotExitCallback implements ProcessorSlotExitCallback {
    @Override
    public void onExit(Context context, ResourceWrapper resourceWrapper, int count, Object... args) {
        log.info("CustomsProcessorSlotExitCallback");
    }
}

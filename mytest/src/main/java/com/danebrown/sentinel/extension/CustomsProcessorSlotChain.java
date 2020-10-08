package com.danebrown.sentinel.extension;

import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.slotchain.AbstractLinkedProcessorSlot;
import com.alibaba.csp.sentinel.slotchain.ProcessorSlotChain;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import lombok.extern.log4j.Log4j2;

/**
 * Created by danebrown on 2020/9/16
 * mail: tain198127@163.com
 */
@Log4j2
public class CustomsProcessorSlotChain extends ProcessorSlotChain {
    @Override
    public void addFirst(AbstractLinkedProcessorSlot<?> protocolProcessor) {
        log.info("{}",protocolProcessor);
    }

    @Override
    public void addLast(AbstractLinkedProcessorSlot<?> protocolProcessor) {
        log.info("{}",protocolProcessor);
    }

    @Override
    public void entry(Context context, ResourceWrapper resourceWrapper, Object param, int count, boolean prioritized, Object... args) throws Throwable {
        log.info("{},{},{},{},{}",context,resourceWrapper,param,count,prioritized);
    }

    @Override
    public void exit(Context context, ResourceWrapper resourceWrapper, int count, Object... args) {
        log.info("{},{},{}",context,resourceWrapper,count);
    }
}

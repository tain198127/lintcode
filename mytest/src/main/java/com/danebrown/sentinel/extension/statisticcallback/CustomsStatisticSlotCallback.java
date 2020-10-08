package com.danebrown.sentinel.extension.statisticcallback;

import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.slotchain.ProcessorSlotEntryCallback;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.log4j.Log4j2;

/**
 * Created by danebrown on 2020/9/17
 * mail: tain198127@163.com
 * statistic 责任链的callback
 */
@Log4j2
public class CustomsStatisticSlotCallback implements ProcessorSlotEntryCallback<DefaultNode> {
    @Override
    public void onPass(Context context, ResourceWrapper resourceWrapper, DefaultNode param, int count, Object... args) throws Exception {
        log.info("CustomsStatisticSlotCallback pass");

    }

    @Override
    public void onBlocked(BlockException ex, Context context, ResourceWrapper resourceWrapper, DefaultNode param, int count, Object... args) {
        log.info("CustomsStatisticSlotCallback blocked");

    }
}

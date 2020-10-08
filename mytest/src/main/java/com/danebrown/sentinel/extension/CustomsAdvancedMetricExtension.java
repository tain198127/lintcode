package com.danebrown.sentinel.extension;

import com.alibaba.csp.sentinel.metric.extension.AdvancedMetricExtension;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.log4j.Log4j2;

/**
 * Created by danebrown on 2020/9/16
 * mail: tain198127@163.com
 */
@Log4j2
public class CustomsAdvancedMetricExtension implements AdvancedMetricExtension {
    @Override
    public void onPass(ResourceWrapper resourceWrapper, int i, Object[] objects) {

    }

    @Override
    public void onBlocked(ResourceWrapper resourceWrapper, int i, String s, BlockException e, Object[] objects) {

    }

    @Override
    public void onComplete(ResourceWrapper resourceWrapper, long l, int i, Object[] objects) {

    }

    @Override
    public void onError(ResourceWrapper resourceWrapper, Throwable throwable, int i, Object[] objects) {

    }

    @Override
    public void addPass(String s, int i, Object... objects) {

    }

    @Override
    public void addBlock(String s, int i, String s1, BlockException e, Object... objects) {

    }

    @Override
    public void addSuccess(String s, int i, Object... objects) {

    }

    @Override
    public void addException(String s, int i, Throwable throwable) {

    }

    @Override
    public void addRt(String s, long l, Object... objects) {

    }

    @Override
    public void increaseThreadNum(String s, Object... objects) {

    }

    @Override
    public void decreaseThreadNum(String s, Object... objects) {

    }
}

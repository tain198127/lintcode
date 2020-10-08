package com.danebrown.sentinel.extension;

import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.ResponseTimeCircuitBreaker;
import lombok.extern.log4j.Log4j2;

/**
 * Created by danebrown on 2020/9/16
 * mail: tain198127@163.com
 */
@Log4j2
public class CustomsCircuitBreaker extends ResponseTimeCircuitBreaker {
    public CustomsCircuitBreaker(DegradeRule rule) {
        super(rule);
    }

    @Override
    public void resetStat() {

    }

    @Override
    public void onRequestComplete(Context context) {

    }

    @Override
    public DegradeRule getRule() {
        return super.getRule();
    }

    @Override
    public State currentState() {
        return super.currentState();
    }

    @Override
    public boolean tryPass(Context context) {
        return super.tryPass(context);
    }

    @Override
    protected boolean retryTimeoutArrived() {
        return super.retryTimeoutArrived();
    }

    @Override
    protected void updateNextRetryTimestamp() {
        super.updateNextRetryTimestamp();
    }

    @Override
    protected boolean fromCloseToOpen(double snapshotValue) {
        return super.fromCloseToOpen(snapshotValue);
    }

    @Override
    protected boolean fromOpenToHalfOpen(Context context) {
        return super.fromOpenToHalfOpen(context);
    }

    @Override
    protected boolean fromHalfOpenToOpen(double snapshotValue) {
        return super.fromHalfOpenToOpen(snapshotValue);
    }

    @Override
    protected boolean fromHalfOpenToClose() {
        return super.fromHalfOpenToClose();
    }

    @Override
    protected void transformToOpen(double triggerValue) {
        super.transformToOpen(triggerValue);
    }
}

package com.danebrown.sentinel.extension;

import com.alibaba.csp.sentinel.node.StatisticNode;
import com.alibaba.csp.sentinel.node.metric.MetricNode;
import com.alibaba.csp.sentinel.util.function.Predicate;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;

/**
 * Created by danebrown on 2020/9/16
 * mail: tain198127@163.com
 */
@Log4j2
public class CustomsNode extends StatisticNode {
    public CustomsNode() {
        super();
    }

    @Override
    public Map<Long, MetricNode> metrics() {
        return super.metrics();
    }

    @Override
    public List<MetricNode> rawMetricsInMin(Predicate<Long> timePredicate) {
        return super.rawMetricsInMin(timePredicate);
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public long totalRequest() {
        return super.totalRequest();
    }

    @Override
    public long blockRequest() {
        return super.blockRequest();
    }

    @Override
    public double blockQps() {
        return super.blockQps();
    }

    @Override
    public double previousBlockQps() {
        return super.previousBlockQps();
    }

    @Override
    public double previousPassQps() {
        return super.previousPassQps();
    }

    @Override
    public double totalQps() {
        return super.totalQps();
    }

    @Override
    public long totalSuccess() {
        return super.totalSuccess();
    }

    @Override
    public double exceptionQps() {
        return super.exceptionQps();
    }

    @Override
    public long totalException() {
        return super.totalException();
    }

    @Override
    public double passQps() {
        return super.passQps();
    }

    @Override
    public long totalPass() {
        return super.totalPass();
    }

    @Override
    public double successQps() {
        return super.successQps();
    }

    @Override
    public double maxSuccessQps() {
        return super.maxSuccessQps();
    }

    @Override
    public double occupiedPassQps() {
        return super.occupiedPassQps();
    }

    @Override
    public double avgRt() {
        return super.avgRt();
    }

    @Override
    public double minRt() {
        return super.minRt();
    }

    @Override
    public int curThreadNum() {
        return super.curThreadNum();
    }

    @Override
    public void addPassRequest(int count) {
        super.addPassRequest(count);
    }

    @Override
    public void addRtAndSuccess(long rt, int successCount) {
        super.addRtAndSuccess(rt, successCount);
    }

    @Override
    public void increaseBlockQps(int count) {
        super.increaseBlockQps(count);
    }

    @Override
    public void increaseExceptionQps(int count) {
        super.increaseExceptionQps(count);
    }

    @Override
    public void increaseThreadNum() {
        super.increaseThreadNum();
    }

    @Override
    public void decreaseThreadNum() {
        super.decreaseThreadNum();
    }

    @Override
    public void debug() {
        super.debug();
    }

    @Override
    public long tryOccupyNext(long currentTime, int acquireCount, double threshold) {
        return super.tryOccupyNext(currentTime, acquireCount, threshold);
    }

    @Override
    public long waiting() {
        return super.waiting();
    }

    @Override
    public void addWaitingRequest(long futureTime, int acquireCount) {
        super.addWaitingRequest(futureTime, acquireCount);
    }

    @Override
    public void addOccupiedPass(int acquireCount) {
        super.addOccupiedPass(acquireCount);
    }
}

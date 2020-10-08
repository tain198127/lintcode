package com.danebrown.sentinel.extension;

import com.alibaba.csp.sentinel.AsyncEntry;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphResourceTypeSupport;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.log4j.Log4j2;

/**
 * Created by danebrown on 2020/9/16
 * mail: tain198127@163.com
 */
@Log4j2
public class CustomsSphResourceTypeSupport implements SphResourceTypeSupport {
    @Override
    public Entry entryWithType(String name, int resourceType, EntryType trafficType, int batchCount, Object[] args) throws BlockException {
        return null;
    }

    @Override
    public Entry entryWithType(String name, int resourceType, EntryType trafficType, int batchCount, boolean prioritized, Object[] args) throws BlockException {
        return null;
    }

    @Override
    public AsyncEntry asyncEntryWithType(String name, int resourceType, EntryType trafficType, int batchCount, boolean prioritized, Object[] args) throws BlockException {
        return null;
    }
}

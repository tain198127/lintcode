package com.danebrown.sentinel.extension;

import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import lombok.extern.log4j.Log4j2;

/**
 * Created by danebrown on 2020/9/16
 * mail: tain198127@163.com
 */
@Log4j2
public class CustomsResourceWrapper extends ResourceWrapper {
    public CustomsResourceWrapper(String name, EntryType entryType, int resourceType) {
        super(name, entryType, resourceType);
    }

    @Override
    public String getShowName() {
        return null;
    }
}

package com.danebrown.sentinel.extension;

import com.alibaba.csp.sentinel.slots.block.AbstractRule;
import lombok.extern.log4j.Log4j2;

/**
 * Created by danebrown on 2020/9/16
 * mail: tain198127@163.com
 */
@Log4j2
public class CustomsRule extends AbstractRule {
    public CustomsRule() {
        super();
    }

    @Override
    public String getResource() {
        return super.getResource();
    }

    @Override
    public AbstractRule setResource(String resource) {
        return super.setResource(resource);
    }

    @Override
    public String getLimitApp() {
        return super.getLimitApp();
    }

    @Override
    public AbstractRule setLimitApp(String limitApp) {
        return super.setLimitApp(limitApp);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public <T extends AbstractRule> T as(Class<T> clazz) {
        return super.as(clazz);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

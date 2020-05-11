package com.danebrown.calculus;

import io.netty.handler.ipfilter.IpSubnetFilterRule;
import lombok.extern.log4j.Log4j2;

/**
 * 莱布尼兹公式
 */
@Log4j2
public class Leibniz {
    public static void main(String[] args ){
        Integer a = new Integer(1);
        Integer b = new Integer(1);
        Integer c = Integer.valueOf(1);
        Integer d = Integer.valueOf(1);
        log.info("a == b?[{}]",a ==b);
        log.info("a == c?[{}]",a ==c);
        log.info("a equal c?[{}]",a.equals(c));
        log.info("a equal b?[{}]",a.equals(b));
        log.info("c equal d?[{}]",c.equals(d));
        log.info("c == d?[{}]",c==d);
    }
}

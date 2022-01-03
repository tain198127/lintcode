package com.danebrown.aop.impl;

import com.danebrown.aop.IAopTest;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;


/**
 * Created by danebrown on 2021/12/24
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Component
public class AopTestImpl implements IAopTest {

    @Override
    public String sayHello(String name) {
        return name+"-->process:["+ DateTime.now().toString("yyyy-MM-dd" +
                " HH:MM:ss:SSS]");
    }
}

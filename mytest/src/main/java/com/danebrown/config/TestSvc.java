package com.danebrown.config;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by danebrown on 2020/11/13
 * mail: tain198127@163.com
 */
@Data
public class TestSvc {
    private String Key = "";
    private String Key1 = "";

    @Override
    public String toString() {
        System.out.println("toString---->TestSvc");
        return "TestSvc{" +
                "Key='" + Key + '\'' +
                ", Key1='" + Key1 + '\'' +
                '}';
    }
}

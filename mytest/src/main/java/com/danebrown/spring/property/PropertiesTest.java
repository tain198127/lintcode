package com.danebrown.spring.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by danebrown on 2022/2/15
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@ConfigurationProperties(prefix = "my.test")
@Component
//@Data
public class PropertiesTest {
    private String name;
    private Integer age;
    private Map map;
    private List<String> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}

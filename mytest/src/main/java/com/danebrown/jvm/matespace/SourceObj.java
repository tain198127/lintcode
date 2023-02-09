package com.danebrown.jvm.matespace;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
public class SourceObj implements SourceInter{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    private String name;
    private String age;

    @Override
    public String showInfo() {
        return String.format("name is %s, age is %s",name,age);
    }
}

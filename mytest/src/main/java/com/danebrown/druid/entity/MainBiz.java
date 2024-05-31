package com.danebrown.druid.entity;

import lombok.Data;

import java.util.Date;

@Data
public class MainBiz {
    int id;
    String bizName;
    String bizDesc;
    String bizType;
    String bizContent;
    Date createTime;
    Date updateTime;
}

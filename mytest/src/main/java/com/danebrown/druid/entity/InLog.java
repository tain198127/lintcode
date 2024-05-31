package com.danebrown.druid.entity;

import lombok.Data;

import java.util.Date;

@Data
public class InLog {
    int id;
    Date createTime;
    Date updateTime;
    String logType;
    String logContent;
    String status;
}

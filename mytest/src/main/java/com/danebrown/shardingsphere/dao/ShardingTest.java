package com.danebrown.shardingsphere.dao;

import lombok.Data;

import java.util.Date;

/**
 * Created by danebrown on 2021/7/22
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Data
public class ShardingTest {
    private int id;
    private Date regDate;
    private String name;
    private String shardingKey;

}

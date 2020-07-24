package com.danebrown.lab.raft.log;

import lombok.Data;

import java.util.List;

/**
 * Created by danebrown on 2020/7/17
 * mail: tain198127@163.com
 * 常驻服务器的日志
 */
@Data
public class RaftPermanentLog {
    /**
     * 服务器最后一次知道的任期号（初始化为 0，持续递增）
     */
    private long currentTerm = 0;
    /**
     * 在当前获得选票的候选人的 Id,雪花算法，获取当前mac地址做hash后，取模集群数量
     */
    private String votedFor="";
    /**
     * 总选举次数
     */
    private long voteCount=0;
    /**
     * 日志条目集；每一个条目包含一个用户状态机执行的指令，和收到时的任期号
     */
    private List<LogEntry> logs;
}

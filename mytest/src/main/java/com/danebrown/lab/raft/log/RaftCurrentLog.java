package com.danebrown.lab.raft.log;

import lombok.Data;

/**
 * Created by danebrown on 2020/7/17
 * mail: tain198127@163.com
 * 会经常变化的日志
 */
@Data
public class RaftCurrentLog {
    /**
     * 已知的最大的已经被提交的日志条目的索引值
     */
    private LogEntry commitIndex;
    /**
     * 最后被应用到状态机的日志条目索引值（初始化为 0，持续递增）
     */
    private long lastApplied;
    /**
     * 当前的term
     */
    private long currentTerm = 0;
    /**
     * 在当前获得选票的候选人的 Id,雪花算法，获取当前mac地址做hash后，取模集群数量
     */
    private String voteFor = "";
    /**
     * 总选举次数
     */
    private long voteCount=0;


}

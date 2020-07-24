package com.danebrown.lab.raft.rpc;

import com.danebrown.lab.raft.log.LogEntry;
import lombok.Data;

import java.util.List;

/**
 * Created by danebrown on 2020/7/17
 * mail: tain198127@163.com
 * 由领导人负责调用来复制日志指令；也会用作heartbeat
 */
@Data
public class RaftRpcReq {
    /**
     * 领导人的任期号
     */
    private long term;
    /**
     * 领导人的 Id，以便于跟随者重定向请求
     */
    private String leaderId;
    /**
     * 新的日志条目紧随之前的索引值
     */
    private long prevLogIndex;
    /**
     * prevLogIndex 条目的任期号
     * terms[prevLogIndex] = prevLogTerm
     */
    private long prevLogTerm;
    /**
     * 准备存储的日志条目（表示心跳时为空；一次性发送多个是为了提高效率）
     */
    private List<LogEntry> entries;
    /**
     * 领导人已经提交的日志的索引值
     */
    private long leaderCommit;
}

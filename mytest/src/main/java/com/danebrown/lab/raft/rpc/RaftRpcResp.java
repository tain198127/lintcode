package com.danebrown.lab.raft.rpc;

import lombok.Data;

/**
 * Created by danebrown on 2020/7/17
 * mail: tain198127@163.com
 */
@Data
public class RaftRpcResp {
    /**
     * 当前的任期号，用于领导人去更新自己
     */
    private long term;
    /**
     * 跟随者包含了匹配上 prevLogIndex 和 prevLogTerm 的日志时为真
     */
    private boolean success;
}

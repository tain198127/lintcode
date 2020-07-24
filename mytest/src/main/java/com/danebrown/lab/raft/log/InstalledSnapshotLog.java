package com.danebrown.lab.raft.log;

import lombok.Data;

/**
 * Created by danebrown on 2020/7/18
 * mail: tain198127@163.com
 * 由领导人调用以将快照的分块发送给跟随者。领导者总是按顺序发送分块。
 */
@Data
public class InstalledSnapshotLog {
    /**
     * 领导人的任期号
     */
    private long term;
    /**
     * 领导人的 Id，以便于跟随者重定向请求
     */
    private String leaderId;
    /**
     * 快照中包含的最后日志条目的索引值
     */
    private long lastIncludedIndex;
    /**
     *
     */
    private long lastIncludedTerm;
    /**
     * 分块在快照中的字节偏移量
     */
    private long offset;
    /**
     * 从偏移量开始的快照分块的原始字节
     */
    private byte[] data;
    /**
     * 如果这是最后一个分块则为 true
     */
    private boolean done;
}

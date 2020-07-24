package com.danebrown.lab.raft.log;

import com.danebrown.lab.raft.log.LogEntry;
import lombok.Data;

import java.util.List;

/**
 * Created by danebrown on 2020/7/17
 * mail: tain198127@163.com
 * 重新选举后，将会重新初始化
 */
@Data
public class RaftTempLog {
    /**
     * 对于每一个服务器，需要发送给他的下一个日志条目的索引值（初始化为领导人最后索引值加一）
     */
    private List<LogEntry> nextIndex;
    /**
     * 对于每一个服务器，已经复制给他的日志的最高索引值
     */
    private List<LogEntry> matchIndex;
}

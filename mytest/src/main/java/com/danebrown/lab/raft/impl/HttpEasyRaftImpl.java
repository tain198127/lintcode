package com.danebrown.lab.raft.impl;

import com.danebrown.lab.raft.rpc.RaftRpcReq;
import com.danebrown.lab.raft.rpc.RaftRpcResp;

import java.net.URI;
import java.util.List;

/**
 * Created by danebrown on 2020/7/24
 * mail: tain198127@163.com
 */
public class HttpEasyRaftImpl extends BaseSimpleRaftImpl {
    /**
     * 向其他节点发送选举
     * @param rpcReq
     * @return
     */
    @Override
    public List<RaftRpcResp> leaderElect(RaftRpcReq rpcReq) {
        return null;
    }

    /**
     * 向其他节点发送复制日志的请求
     * @param rpcReq
     * @return
     */
    @Override
    public List<RaftRpcResp> syncLog(RaftRpcReq rpcReq) {
        return null;
    }

    /**
     * 响应其他节点发送的同步日志的请求
     * @param rpcReq
     * @return
     */
    @Override
    public RaftRpcResp onSyncLog(RaftRpcReq rpcReq) {
        return null;
    }

    /**
     * 向其他节点发送心跳的请求
     * @param rpcReq
     * @return
     */
    @Override
    public List<RaftRpcResp> heartbeat(RaftRpcReq rpcReq) {
        return null;
    }

    /**
     * 返回集群内所有的节点信息
     * @return
     */
    @Override
    public List<URI> getAllMergedNodes() {

        return null;
    }


}

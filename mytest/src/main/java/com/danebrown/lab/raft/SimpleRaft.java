package com.danebrown.lab.raft;

import com.danebrown.lab.raft.rpc.RaftRpcReq;
import com.danebrown.lab.raft.rpc.RaftRpcResp;

import java.net.URI;
import java.util.List;

/**
 * Created by danebrown on 2020/7/17
 * mail: tain198127@163.com
 * 一致性算法是从复制状态机的背景下提出的（参考英文原文引用37）。
 * 在这种方法中，一组服务器上的状态机产生相同状态的副本，
 * 并且在一些机器宕掉的情况下也可以继续运行。复制状态机在分布式系统中被用于解决很多容错的问题。
 * 例如，大规模的系统中通常都有一个集群领导者，像 GFS、HDFS 和 RAMCloud，
 * 典型应用就是一个独立的的复制状态机去管理领导选举和存储配置信息并且在领导人宕机的情况下也要存活下来。
 * 比如 Chubby 和 ZooKeeper。
 * 本质上是复制状态机
 * <p>
 * 不依赖时序
 * <p>
 * 非拜占庭错误下，延迟、丢包、乱序问题都能解决。
 * <p>
 * 高可用性
 */
public interface SimpleRaft {
    /**
     * 启动阶段初始化，负责重载日志等
     * 启动阶段所有节点都是follower
     */
    void onStartup();
    /**
     * 选举
     * 如果term < currentTerm返回 false
     * 如果 votedFor 为空或者为 candidateId，并且候选人的日志至少和自己一样新，那么就投票给他
     * 所有服务器：
     *
     * 如果commitIndex > lastApplied，那么就 lastApplied 加一，并把log[lastApplied]应用到状态机中（5.3 节）
     * 如果接收到的 RPC 请求或响应中，任期号T > currentTerm，那么就令 currentTerm 等于 T，并切换状态为跟随者
     *
     * 跟随者（5.2 节）：
     *
     * 响应来自候选人和领导者的请求
     * 如果在超过选举超时时间的情况之前没有收到当前领导人（即该领导人的任期需与这个跟随者的当前任期相同）的心跳/附加日志，或者是给某个候选人投了票，就自己变成候选人
     *
     * 候选人（5.2 节）：
     *
     * 在转变成候选人后就立即开始选举过程
     * 自增当前的任期号（currentTerm）
     * 给自己投票
     * 重置选举超时计时器
     * 发送请求投票的 RPC 给其他所有服务器
     * 如果接收到大多数服务器的选票，那么就变成领导人
     * 如果接收到来自新的领导人的附加日志 RPC，转变成跟随者
     * 如果选举过程超时，再次发起一轮选举
     *
     * 领导人：
     *
     * 一旦成为领导人：发送空的附加日志 RPC（心跳）给其他所有的服务器；在一定的空余时间之后不停的重复发送，以阻止跟随者超时（5.2 节）
     * 如果接收到来自客户端的请求：附加条目到本地日志中，在条目被应用到状态机后响应客户端（5.3 节）
     * 如果对于一个跟随者，最后日志条目的索引值大于等于 nextIndex，那么：发送从 nextIndex 开始的所有日志条目：
     * 如果成功：更新相应跟随者的 nextIndex 和 matchIndex
     * 如果因为日志不一致而失败，减少 nextIndex 重试
     * 如果存在一个满足N > commitIndex的 N，并且大多数的matchIndex[i] ≥ N成立，并且log[N].term == currentTerm成立，那么令 commitIndex 等于这个 N （5.3 和 5.4 节）
     * @param rpcReq
     * @return
     */
    List<RaftRpcResp> leaderElect(RaftRpcReq rpcReq);

    /**
     * 相应其他服务器发送的选举请求
     * @param rpcReq
     * @return
     */
    RaftRpcResp onLeaderElect(RaftRpcReq rpcReq);

    /**
     * 同步日志
     * @param rpcReq
     * @return
     */
    List<RaftRpcResp> syncLog(RaftRpcReq rpcReq);

    /**
     * 响应同步日志
     * @param rpcReq
     * @return
     */
    RaftRpcResp onSyncLog(RaftRpcReq rpcReq);

    /**
     * 获取当前角色
     * @return
     */
    RaftRole getCurrentRole();

    /**
     * 心跳
     * @param rpcReq
     * @return
     */
    List<RaftRpcResp> heartbeat(RaftRpcReq rpcReq);

    /**
     * 生成随机毫秒
     * @return
     */
    long generateRandomMSPeriod();

    /**
     * 获取群组内所有其他节点
     * @return
     */
    List<URI> getAllMergedNodes();


}

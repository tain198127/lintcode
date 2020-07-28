package com.danebrown.lab.raft.impl;

import com.danebrown.lab.raft.RaftRole;
import com.danebrown.lab.raft.SimpleRaft;
import com.danebrown.lab.raft.log.RaftCurrentLog;
import com.danebrown.lab.raft.rpc.RaftRpcReq;
import com.danebrown.lab.raft.rpc.RaftRpcResp;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Created by danebrown on 2020/7/18
 * mail: tain198127@163.com
 */
@Slf4j
public abstract class BaseSimpleRaftImpl implements SimpleRaft {
    /**
     * 当前角色
     */
    volatile RaftRole currentRole = RaftRole.FOLLOWER;
    /**
     * 当前日志
     */
    volatile RaftCurrentLog currentLog = new RaftCurrentLog();
    /**
     * 启动等待时间
     */
    volatile long randomStartupWaitMs = 3000;
    /**
     * 轮询等待时间
     */
    volatile long randomWaitPeriodMs = 3000;
    /**
     * 是否被其他请求，要求作为FOLLOWER
     */
    volatile boolean isBeFucked = false;
    /**
     * 响应超时时间
     */
    volatile long timeOutMs = 3000;

    volatile long isStartup = 0;

    volatile List<URI> allNodes;
    List<OnRoleChanged> roleChangedListeners = new ArrayList<>();
    String currentNodeName;

    public void registerRoleChangedListener(OnRoleChanged onRoleChanged) {
        roleChangedListeners.add(onRoleChanged);
    }

    /**
     * 1、启动时所有服务器的状态为FOLLOWER
     * 2、根据随机数向其他
     */
    @Override
    public void onStartup() {
        //所有服务启动时，状态都是FOLLOWER
        currentRole = RaftRole.FOLLOWER;
        //获取一个随机数，用于避免系统同时请求，导致雪崩
        randomStartupWaitMs = this.generateRandomMSPeriod();
        randomWaitPeriodMs = this.generateRandomMSPeriod();
        currentNodeName = this.getCurrentNodeName();
        currentLog.setCurrentTerm(0);
        currentLog.setVoteFor(currentNodeName);
        currentLog.setVoteCount(0);

        //TODO 这里要修改
        //获取所有节点
        allNodes = this.getAllMergedNodes();

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                run();
            }
        }, this.randomStartupWaitMs, this.randomWaitPeriodMs, TimeUnit.MILLISECONDS);

    }

    /**
     * 轮询
     */
    public void run() {
        //如果在轮询过程中，被其他节点 heartbeat了。
        //本质上是状态机
        if (this.isBeFucked) {
            this.isBeFucked = false;
            return;
        }
        this.isBeFucked = false;
        if (this.currentRole == RaftRole.FOLLOWER) {
            follower2Candidate((beforeRole, afterRole) -> {
                if (isStartup == 0) {
                    //如果是启动后第一次，则马上进行申请leader申请
                    candidate2Leader(((beforeRole1, afterRole1) -> {
                        leaderSendHeartBeat();
                    }));
                }
            });
        } else if (this.currentRole == RaftRole.CANDIDATE) {
            candidate2Leader(((beforeRole, afterRole) -> {
                //啥也不干
            }));
        } else if (this.currentRole == RaftRole.LEADER) {
            //发送心跳，防止其他服务申请
            leaderSendHeartBeat();
        }


    }


    /**
     * follower到candidate
     */
    private void follower2Candidate(OnRoleChanged callback) {
        if (this.isBeFucked) {
            //在进入这个函数的时候，被其他服务抢占了
            this.currentRole = RaftRole.FOLLOWER;
            return;
        }
        RaftRpcReq req = new RaftRpcReq();

        req.setTerm(currentLog.getCurrentTerm());
        req.setLeaderId(this.currentNodeName);

        List<RaftRpcResp> raftRpcResps = leaderElect(req);

        long succCt = raftRpcResps.stream().filter(RaftRpcResp::isSuccess).count();
        //选举成功
        if (succCt / allNodes.size() > (1 / 2)) {
            //在进入这个函数的时候，被其他服务抢占了
            if (this.isBeFucked) {
                this.currentRole = RaftRole.FOLLOWER;
                return;
            }
            this.currentRole = RaftRole.CANDIDATE;
            this.roleChangedListeners.forEach(item -> item.processRoleChanged(RaftRole.FOLLOWER, RaftRole.CANDIDATE));
            callback.processRoleChanged(RaftRole.FOLLOWER, RaftRole.CANDIDATE);

        } else {
            //证明没成功
            this.currentRole = RaftRole.FOLLOWER;
        }
        this.isStartup++;
    }

    /**
     * 从candidate到leader
     */
    private void candidate2Leader(OnRoleChanged callback) {
        if (this.isBeFucked) {
            //在进入这个函数的时候，被其他服务抢占了,那么降级为跟随者
            this.currentRole = RaftRole.FOLLOWER;
            return;
        }
        RaftRpcReq req = new RaftRpcReq();
        List<RaftRpcResp> rpcResps = this.leaderElect(req);
        long succCt = rpcResps.stream().filter(RaftRpcResp::isSuccess).count();
        if (succCt / allNodes.size() > (1 / 2)) {
            if (this.isBeFucked) {
                //这个期间如果被其他服务占据，则降级为跟随者
                this.currentRole = RaftRole.FOLLOWER;
                return;
            }
            //如果申请成功
            this.currentRole = RaftRole.LEADER;
            callback.processRoleChanged(RaftRole.CANDIDATE, RaftRole.LEADER);
        } else {
            //如果申请没成功，保持自己
            this.currentRole = RaftRole.CANDIDATE;
        }
    }

    private void leaderSendHeartBeat() {
        List<URI> uris = this.getAllMergedNodes();
        RaftRpcReq heartBeatRpcReq = new RaftRpcReq();
        this.heartbeat(heartBeatRpcReq);
    }


    /**
     * 相应其他组件发送的选举请求
     *
     * @param rpcReq
     * @return
     */
    @Override
    public RaftRpcResp onLeaderElect(RaftRpcReq rpcReq) {
        //维持苦逼状态
        if (this.currentRole == RaftRole.FOLLOWER) {
            this.isBeFucked = true;
        }
        //多CANDIDATE竞争，判断各自的编号
        else if (this.currentRole == RaftRole.CANDIDATE) {
            this.isBeFucked = true;
        }
        //争夺LEADER，弄死他
        else if (this.currentRole == RaftRole.LEADER) {
            this.isBeFucked = false;
        }
        return null;
    }


    @Override
    public RaftRole getCurrentRole() {
        return this.currentRole;
    }


    @Override
    public long generateRandomMSPeriod() {
        return ThreadLocalRandom.current().nextInt(2000, 8000);
    }

    /**
     * 获取当前节点名称
     *
     * @return
     */
    String getCurrentNodeName() {
        try {
            byte[] mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
            String defaultMacString = Base64.getEncoder().encodeToString(mac);
            return defaultMacString;


        } catch (SocketException | UnknownHostException e) {
            log.error("getCurrentNodeName error:{}", e);
        }
        return String.valueOf(ThreadLocalRandom.current().nextInt());
    }


}

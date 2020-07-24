package com.danebrown.lab.raft.impl;

import com.danebrown.lab.raft.RaftRole;

/**
 * Created by danebrown on 2020/7/24
 * mail: tain198127@163.com
 */
@FunctionalInterface
public interface OnRoleChanged {
    /**
     * 处理角色变化
     * @param beforeRole 变化之前的角色
     * @param afterRole 变化之后的角色
     */
    void processRoleChanged(RaftRole beforeRole, RaftRole afterRole);
}

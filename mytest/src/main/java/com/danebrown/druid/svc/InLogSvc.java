package com.danebrown.druid.svc;

import com.danebrown.druid.entity.InLog;
import com.danebrown.druid.mapper.InLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InLogSvc {
    @Autowired
    InLogMapper mapper;
    
    @Transactional(propagation= Propagation.REQUIRES_NEW,isolation = org.springframework.transaction.annotation.Isolation.READ_COMMITTED)
    public int insert(InLog inLog){
        return mapper.insert(inLog);
    }
    @Transactional(propagation= Propagation.REQUIRES_NEW,isolation = org.springframework.transaction.annotation.Isolation.READ_COMMITTED)
    public int update(InLog inLog){
        return mapper.updateById(inLog);
    }
    public InLog selectById(int id){
        return mapper.selectById(id);
    }
}

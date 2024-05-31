package com.danebrown.druid.svc;

import com.danebrown.druid.entity.InLog;
import com.danebrown.druid.entity.MainBiz;
import com.danebrown.druid.mapper.MainBizMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
@Log4j2
@Service
public class ContainerTransSvcv {
    @Autowired
    InLogSvc inLogSvc;
    @Autowired
    MainBizMapper mainBizMapper;
    @Transactional(propagation = Propagation.REQUIRED, isolation = org.springframework.transaction.annotation.Isolation.READ_COMMITTED)
    public int insert(MainBiz biz){
        int rst = 0;
        InLog inlog =  new InLog();
        inlog.setCreateTime(new Date());
        inlog.setUpdateTime(new Date());
        inlog.setLogContent(biz.getBizContent());
        inlog.setLogType("normal");
        inlog.setStatus("insert");
        
        try {
            inLogSvc.insert(inlog);
            rst = mainBizMapper.insert(biz);
        }catch (Exception ex){
            log.error("error",ex);
            inlog.setStatus("error");
            inLogSvc.update(inlog);
            throw ex;
        }
        inlog.setStatus("finish");
        inLogSvc.update(inlog);
        return rst;
    }
}

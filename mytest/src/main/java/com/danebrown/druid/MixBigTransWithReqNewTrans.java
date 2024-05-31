package com.danebrown.druid;

import com.danebrown.druid.entity.MainBiz;
import com.danebrown.druid.svc.ContainerTransSvcv;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Date;
import java.util.UUID;

@Log4j2
@SpringBootApplication(scanBasePackages = {"com.danebrown.druid"})
@EnableAspectJAutoProxy
@RestController("/")
@EnableSwagger2
@EnableTransactionManagement
@MapperScan("com.danebrown.druid.mapper")
@Api(value = "后台管理", tags = "Administrator - 后台主入口")

public class MixBigTransWithReqNewTrans {
    private static final String profile = "druid";
    @Autowired
    ContainerTransSvcv svc;
    
    public static void main(String[] args) {
        
        SpringApplication springApplication = new SpringApplication(MixBigTransWithReqNewTrans.class);
        //        springApplication.setWebApplicationType(WebApplicationType.REACTIVE);
        springApplication.setAdditionalProfiles(profile);
        springApplication.run(MixBigTransWithReqNewTrans.class, args);
    }
    
    @GetMapping("/insert")
    public Integer insert() {
        MainBiz biz = new MainBiz();
        biz.setCreateTime(new Date());
        biz.setUpdateTime(new Date());
        biz.setBizContent(UUID.randomUUID().toString());
        biz.setBizDesc(UUID.randomUUID().toString());
        biz.setBizName("test");
        biz.setBizType("normal");
        return svc.insert(biz);
        //        return Mono.just(svc.insert(biz));
        
    }
    
}

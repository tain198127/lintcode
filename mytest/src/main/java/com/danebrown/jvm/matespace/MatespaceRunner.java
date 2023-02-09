package com.danebrown.jvm.matespace;

import cn.hutool.json.JSONUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.danebrown.aop.AopTestRunner;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.Scanner;

@Log4j2
//@SpringBootApplication(scanBasePackages = {"com.danebrown.jvm.matespace"})
//@EnableLoadTimeWeaving
//@EnableAspectJAutoProxy(proxyTargetClass = true)
public class MatespaceRunner implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(MatespaceRunner.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(MatespaceRunner.class, args);
    }


    @Autowired
    private SourceInter obj;
    @Override
    public void run(String... args) throws Exception {
        obj.setAge("12");
        obj.setName("aaa");
        String str = JSONUtil.toJsonStr(obj);
        Scanner scanner = new Scanner(System.in);
        BeanCopier beanCopier = BeanCopier.create(SourceInter.class,SourceInter.class,true);
        while (scanner.hasNextLine()) {

            String input= scanner.next();

            int i = StringUtils.isNumeric(input)?Integer.parseInt(input):-1;
            if (i == 0){
                break;
            }
            if( i<0){
                System.err.printf("错误的输入");
                continue;
            }
            for(int t = 0; t < i; t++){
                SourceObj tmp = JSONUtil.toBean(str,SourceObj.class);
                SourceObj interfaceTmp = new SourceObj();
                        beanCopier.copy(obj,interfaceTmp,null);
                log.info("json:{}--->{},beancopy:{}--->{}",tmp, tmp.showInfo(),interfaceTmp,interfaceTmp.showInfo());

            }
            System.out.println("请输入循环次数\n");
        }
        System.out.println("bye~");

    }
}

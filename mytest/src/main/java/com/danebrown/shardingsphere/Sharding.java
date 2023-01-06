package com.danebrown.shardingsphere;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by danebrown on 2021/7/20
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@SpringBootApplication
@Component
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class Sharding implements CommandLineRunner {
    private static final String profile = "sp";
    public static void main(String[] args) {
        new SpringApplicationBuilder(Sharding.class).
                web(WebApplicationType.NONE)
                .profiles(profile)
                .run(args);

    }

    @Autowired
    ShardingJDBC shardingJDBC;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        shardingJDBC.insert();
//        shardingJDBC.insert_nest();
        shardingJDBC.insert_reqnew();
        int i = 1/0;
        shardingJDBC.runsql();
    }
    @Configuration
    public static class ShardingStartup{
        @Bean
        public ShardingJDBC getShardingJDBC(){
            return new ShardingJDBC();
        }
    }
}

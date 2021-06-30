package com.danebrown.netty.client;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by danebrown on 2021/6/30
 * mail: tain198127@163.com
 *
 * @author danebrown
 */

public class NettyServer implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(NettyServer.class,args);
    }
    @Override
    public void run(String... args) throws Exception {

    }
}

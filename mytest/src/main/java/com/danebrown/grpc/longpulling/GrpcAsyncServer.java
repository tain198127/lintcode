package com.danebrown.grpc.longpulling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;

@SpringBootApplication
public class GrpcAsyncServer  {
    public static void main(String[] args) {
        SpringApplication application= new SpringApplication();
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(GrpcAsyncServer.class,args);
        
    }
    public String handleReq(@RequestBody String msg){
        return "hello";
    }
}

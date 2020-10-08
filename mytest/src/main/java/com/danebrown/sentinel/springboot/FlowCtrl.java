package com.danebrown.sentinel.springboot;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by danebrown on 2020/9/15
 * mail: tain198127@163.com
 */
@RestController("/")
public class FlowCtrl {
    @PostMapping("getName")
    public String getName(@RequestParam("age") String age){
        return "Hello"+age;
    }
}

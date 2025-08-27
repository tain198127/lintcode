package lintcode.sse.controller;

import lintcode.sse.model.VoteReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class VoteController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @CrossOrigin(origins = "*")
    @PostMapping("/vote")
    public boolean vote(@RequestBody VoteReq req) {
        redisTemplate.opsForValue().increment(req.getTopic() + req.getVote(), 1);
        return true;
    }
}

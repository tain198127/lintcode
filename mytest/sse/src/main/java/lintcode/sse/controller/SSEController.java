package lintcode.sse.controller;

import lintcode.sse.conf.SseEmitterPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
@CrossOrigin(origins = "*")
@RestController
public class SSEController {
    @Autowired
    private SseEmitterPool sseEmitterPool;

    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sseEndpoint() {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L); // 30分钟超时
        sseEmitterPool.add(emitter);
        emitter.onCompletion(() -> sseEmitterPool.remove(emitter));
        emitter.onTimeout(() -> sseEmitterPool.remove(emitter));
        return emitter;
    }
}

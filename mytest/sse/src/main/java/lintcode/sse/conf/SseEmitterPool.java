package lintcode.sse.conf;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SseEmitterPool {
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public void add(SseEmitter emitter) {
        this.emitters.add(emitter);
    }

    public void remove(SseEmitter emitter) {
        this.emitters.remove(emitter);
    }

    public void sendToAllClients(String data) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().data(data));
            } catch (Exception e) {
                emitter.complete();
                remove(emitter);
            }
        }
    }
}

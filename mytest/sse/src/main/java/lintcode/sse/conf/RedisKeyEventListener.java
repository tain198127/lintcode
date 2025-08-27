package lintcode.sse.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class RedisKeyEventListener implements MessageListener {
    @Autowired
    private SseEmitterPool sseEmitterPool;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String key = new String(message.getBody());

        System.out.println("Redis Event - Channel: " + channel + " Key: " + key);

        // 检查 key，决定是否发送 SSE 事件
        if (key.startsWith("topic_")) {
            // 获取最新值

            Object value =redisTemplate.opsForValue().get(key);
            String payload = value != null ? value.toString() : "null";
            log.info("payload: " + payload);
            sseEmitterPool.sendToAllClients("key-updated: " + payload);
        }
    }
}

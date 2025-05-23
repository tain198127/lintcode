package com.danebrown.json.inttime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@SpringBootApplication
public class LocalTimeTest {
    public static void main(String[] args) {
        SpringApplication.run(LocalTimeTest.class, args);
    }

    public static class IntToTimeStringSerializer extends JsonSerializer<Long> {

        @Override
        public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value == null) {
                gen.writeNull();
                return;
            }

            Long timeInt = value;

            Long hours = timeInt / 10000;
            Long remainder = timeInt % 10000;
            Long minutes = remainder / 100;
            Long seconds = remainder % 100;

            if (hours < 0 || hours >= 24 || minutes < 0 || minutes >= 60 || seconds < 0 || seconds >= 60) {
                throw new IllegalArgumentException("Invalid time value: " + timeInt);
            }

            String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            gen.writeString(formattedTime);
        }
    }

    public static class TimeStringToIntDeserializer extends JsonDeserializer<Long> {

        @Override
        public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String timeStr = p.getValueAsString();

            if (timeStr == null || !timeStr.matches("\\d{2}:\\d{2}:\\d{2}")) {
                throw new IllegalArgumentException("Invalid time format: " + timeStr);
            }

            String[] parts = timeStr.split(":");
            Long hours = Long.parseLong(parts[0]);
            Long minutes = Long.parseLong(parts[1]);
            Long seconds = Long.parseLong(parts[2]);

            if (hours < 0 || hours >= 24 || minutes < 0 || minutes >= 60 || seconds < 0 || seconds >= 60) {
                throw new IllegalArgumentException("Invalid time value: " + timeStr);
            }

            return hours * 10000 + minutes * 100 + seconds;
        }
    }

    public static class TimeDto {

        @JsonSerialize(using = IntToTimeStringSerializer.class)
        @JsonDeserialize(using = TimeStringToIntDeserializer.class)
        private long time;

        public TimeDto() {
        }

        public TimeDto(long time) {
            this.time = time;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }





    @RestController
    @RequestMapping("/api/time")
    public static class TimeController {

        // GET: 测试输出字符串格式的时间
        @GetMapping
        public TimeDto getTime() {
            return new TimeDto(235959L); // 返回 {"time": "23:59:59"}
        }

        // POST: 接收字符串格式时间并返回整形
        @PostMapping
        public TimeDto receiveTime(@RequestBody TimeDto dto) {
            return dto; // 输入 {"time":"01:00:00"} → 输出 {"time":"01:00:00"}
        }
    }


}

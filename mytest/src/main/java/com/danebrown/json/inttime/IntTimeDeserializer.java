package com.danebrown.json.inttime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class IntTimeDeserializer extends JsonDeserializer<Long> {
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

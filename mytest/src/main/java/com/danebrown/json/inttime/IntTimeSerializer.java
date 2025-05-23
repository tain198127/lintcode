package com.danebrown.json.inttime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class IntTimeSerializer extends JsonSerializer<Long> {
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

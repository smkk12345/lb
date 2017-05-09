package com.longbei.appservice.common.web;

/**
 * Created by lixb on 2017/5/8.
 */
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

import java.io.IOException;
@JacksonStdImpl
public class CustomDoubleSerialize extends JsonSerializer<Double> {
    @Override
        public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeString(String.valueOf(value.intValue()));
    }
}

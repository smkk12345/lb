package com.longbei.appservice.common.web;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wangyongzhi 17/4/26.
 */
public class JsonDateValueProcessor implements JsonValueProcessor {
    private String format = "yyyy-MM-dd HH:mm:ss";

    public JsonDateValueProcessor() {
        super();
    }

    public JsonDateValueProcessor(String format) {
        super();
        this.format = format;
    }

    @Override
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {
        return process(value);
    }

    @Override
    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
        return process(value);
    }

    private Object process(Object value) {
        if(value instanceof Date){
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(value);
        }
        return value == null ? "" : value.toString();
    }
}

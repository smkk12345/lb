package com.longbei.appservice.common.web;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * Created by wangyongzhi 17/4/26.
 */
public class JsonLongValueProcessor implements JsonValueProcessor {

    public JsonLongValueProcessor() {
        super();
    }

    public JsonLongValueProcessor(String format) {
        super();
    }

    @Override
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {
        return process(value);
    }

    @Override
    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
        return process(value);
    }

    private Object process(Object value){
        if(value instanceof Long){
            return value == null ? "" : value.toString();
        }
        return value == null ? "" : value.toString();
    }
}

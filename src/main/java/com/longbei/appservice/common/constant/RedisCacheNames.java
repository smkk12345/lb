package com.longbei.appservice.common.constant;

import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lixb on 2017/8/28.
 */
@Component
public class RedisCacheNames {

    public static final String _USER = "smkklihai";// 缓存name
    public static final Long _USER_SECOND = 20L;// 缓存时间

    public static final String _IMPROVE = "smkkimprove";// 缓存key
    public static final Long _IMPROVE_SECOND = 2000L;// 缓存时间

    // 根据key设定具体的缓存时间
    private Map<String, Long> expiresMap = null;

    @PostConstruct
    public void init(){
        expiresMap = new HashMap<>();
        expiresMap.put(_USER, _USER_SECOND);
        expiresMap.put(_IMPROVE,_IMPROVE_SECOND);
    }

    public Map<String, Long> getExpiresMap(){
        return this.expiresMap;
    }

}

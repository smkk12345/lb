package com.longbei.appservice.cache;

import com.longbei.appservice.common.constant.RedisCacheNames;
import com.longbei.appservice.common.utils.ShortUrlUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 基本
 *
 * @author luye
 * @create 2017-09-16 下午6:29
 **/
@Service
public class CommonCache {

    @Cacheable(cacheNames = RedisCacheNames._SHORT_URL,key="#longUrl")
    public String getShortUrl(String longUrl){
        return ShortUrlUtils.getShortUrl(longUrl);
    }

}

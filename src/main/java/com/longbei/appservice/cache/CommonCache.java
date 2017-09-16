package com.longbei.appservice.cache;

import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.constant.RedisCacheNames;
import com.longbei.appservice.common.utils.HttpUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;

/**
 * Created by wangyongzhi 17/9/16.
 */
@Service
public class CommonCache {

    @Cacheable(cacheNames = RedisCacheNames._ShortUrl,key = "#longUrl")
    public String getShortUrl(String longUrl){
        longUrl = URLEncoder.encode(longUrl);
        String url = Constant.WB_SHORTURL+longUrl;
        try {
            String result = HttpUtils.getRequest(url, null);
            JSONArray jsonArr = JSONArray.fromObject(result);
            JSONObject jsonObject = JSONObject.fromObject(jsonArr.get(0));
            return jsonObject.getString("url_short");
        } catch (Exception e) {
            return longUrl;
        }
    }

}

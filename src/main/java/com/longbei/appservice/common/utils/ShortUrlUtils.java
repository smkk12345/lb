package com.longbei.appservice.common.utils;

import java.util.Date;

import com.longbei.appservice.common.constant.Constant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ShortUrlUtils {
	/**
	 * 短链接
	 */
	//http://api.t.sina.com.cn/short_url/shorten.xml?source=3271760578&url_long=http://www.douban.com/note/249723561/
	public static String getShortUrl(String longUrl){
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

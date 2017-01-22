package com.longbei.appservice.common.security;

/**
 * 获取app登录的token，app将token放入HEADER中，由服务端进行比对
 * String sign = PUBLIC_KEY + HTTPMETHOD(GET/POST) + api_url(API的访问URl) + password
 * sign = MD5(user + ":" + MD5(sign))
 * 
 * @author bear.xiong
 * 
 */
public class AppToken {
	public static String token(String key, String method, String url, String username, String password) {
		String sign = MD5.encrypt(key + method + url + password);
		return MD5.encrypt(username + ":" + sign);
	}
}

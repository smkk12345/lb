package com.longbei.appservice.service.api;

import com.longbei.appservice.common.interceptor.ApiRequestInterceptor;
import com.longbei.appservice.service.api.userservice.IUserBasicService;
import feign.Feign;
import feign.gson.GsonDecoder;

public class HttpClient {
	//user_service
	
	public static IUserBasicService userBasicService = 
			Feign.builder().requestInterceptor(new ApiRequestInterceptor())
			.decoder(new GsonDecoder())
            .target(IUserBasicService.class, "http://192.168.1.120:8081/user_service");
}

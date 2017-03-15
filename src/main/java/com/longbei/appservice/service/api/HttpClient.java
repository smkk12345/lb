package com.longbei.appservice.service.api;

import com.longbei.appservice.common.interceptor.ApiRequestInterceptor;
import com.longbei.appservice.service.api.outernetservice.IAlidayuService;
import com.longbei.appservice.service.api.outernetservice.IJPushService;
import com.longbei.appservice.service.api.outernetservice.IRongYunService;
import com.longbei.appservice.service.api.productservice.IProductBasicService;
import com.longbei.appservice.service.api.userservice.IUserBasicService;
import feign.Feign;
import feign.gson.GsonDecoder;

public class HttpClient {
	
	//product_service
	public static IProductBasicService productBasicService =
			Feign.builder()
//			.requestInterceptor(new ApiRequestInterceptor())
			.decoder(new GsonDecoder())
            .target(IProductBasicService.class, "http://192.168.1.147:8088/product_service");

	//user_service
	public static IUserBasicService userBasicService =
			Feign.builder().requestInterceptor(new ApiRequestInterceptor())
			.decoder(new GsonDecoder())
            .target(IUserBasicService.class, "http://192.168.1.105:8080/user_service");
	//外网接口 阿里大鱼
	public static IAlidayuService alidayuService =
			Feign.builder().requestInterceptor(new ApiRequestInterceptor())
					.decoder(new GsonDecoder())
					.target(IAlidayuService.class, "http://192.168.1.105:8085/outernet_service");
	//融云
	public static IRongYunService rongYunService=
			Feign.builder().requestInterceptor(new ApiRequestInterceptor())
			.decoder(new GsonDecoder())
			.target(IRongYunService.class,"http://192.168.1.105:8085/outernet_service");
	//jpush
	public static IJPushService jPushService=
			Feign.builder().requestInterceptor(new ApiRequestInterceptor())
					.decoder(new GsonDecoder())
					.target(IJPushService.class,"http://192.168.1.105:8085/outernet_service");

}

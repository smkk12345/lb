//package com.longbei.appservice.common.interceptor;
//
//import com.longbei.appservice.common.constant.Constant;
//import com.longbei.appservice.common.security.SpringUtil;
//import com.longbei.appservice.common.security.TokenManager;
//import feign.RequestInterceptor;
//import feign.RequestTemplate;
//
//import org.springframework.context.support.ApplicationObjectSupport;
//import org.springframework.stereotype.Component;
//
///**
// * Created by luye on 2017/1/14.
// */
//@Component
//public class ApiRequestInterceptor extends ApplicationObjectSupport implements RequestInterceptor{
//
//	private TokenManager tokenManager;
//
//	@Override
//	public void apply(RequestTemplate requestTemplate) {
//		if (requestTemplate.url().contains("getServiceToken")) {
//			return;
//		}
//		String token = getTokenManager().getTokenFromCache(Constant.SERVER_USER_SERVICE);
//		requestTemplate.header("Authorization", "Basic" + token);
//	}
//
//	private TokenManager getTokenManager() {
//		if (null == tokenManager) {
//			tokenManager = (TokenManager)SpringUtil.getApplicationContext().getBean("tokenManager");
//			return tokenManager;
//		} else {
//			return tokenManager;
//		}
//	}
//
//}

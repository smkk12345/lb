package com.longbei.appservice.common.interceptor;

import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.security.TokenManager;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by luye on 2017/1/14.
 */
public class ApiRequestInterceptor implements RequestInterceptor {
	private TokenManager tokenManager;
	
    @Override
    public void apply(RequestTemplate requestTemplate) {
    		if(requestTemplate.url().contains("getServiceToken")){
    			return;
    		}
        String token = getTokenManager().getTokenFromCache(Constant.SERVER_USER_SERVICE);
        requestTemplate.header("Authorization", "Basic" + token);
    }
    
    private TokenManager getTokenManager(){
    		if(null == tokenManager){
	    		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
	        tokenManager = (TokenManager) wac.getBean("tokenManager");
	        return tokenManager;
    		}else{
    			return tokenManager;
    		}
    }
    
}

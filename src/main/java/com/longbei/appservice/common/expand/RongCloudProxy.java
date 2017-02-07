package com.longbei.appservice.common.expand;

//import com.longbei.appservice.common.persistence.CustomizedPropertyConfigurer;
import com.longbei.appservice.config.AliServiceConfig;

import io.rong.RongCloud;

/**
 * 融云客户端
 * auther:smkk
 * date:2017-01-13
 */
public class RongCloudProxy {

//	public static RongCloud rongCloud = 
//			RongCloud.getInstance(CustomizedPropertyConfigurer.getContextProperty("rongyun_key"), 
//					CustomizedPropertyConfigurer.getContextProperty("rongyun_secret"));
	
	public static RongCloud rongCloud = 
			RongCloud.getInstance(AliServiceConfig.rongyun_key, AliServiceConfig.rongyun_secret);
	
	
}

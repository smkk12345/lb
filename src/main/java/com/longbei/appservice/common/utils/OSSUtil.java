package com.longbei.appservice.common.utils;

import com.longbei.appservice.common.constant.Constant;

public class OSSUtil {
	
	public static String getIMGURL(String key){
		return Constant.OSS_IMG + key + Constant.OSS_IMGSTYLE_300_300;
	}
	
}

package com.longbei.appservice.common.utils;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {
	/**
	 * 获取版本好
	 * @param request
	 * @return
	 */
	public static String getVsersion(HttpServletRequest request){
		String version = request.getParameter("version");
		String[] vArr = version.split("_");
		version = vArr[vArr.length-1];
		return version;
	}
	
	/**
	 * 获取版本好
	 * @param request
	 * @return
	 */
	public static String getDevice(HttpServletRequest request){
		String version = request.getParameter("version");
		String[] vArr = version.split("_");
		String device = vArr[1];
		return device;
	}

	/**
	 * 判断是ios还是安卓
	 * @param version
	 * @return
	 */
	public static boolean isIos(String version){
		//v1.0_2_3.0.2
		if(StringUtils.isBlank(version)){
			return false;
		}
		String vers[] = version.split("_");
		if (vers.length > 0) {
			String v = vers[2];
			v = v.trim().replaceAll("\\.", "");
			if ("2".equals(vers[1])){//andrion
				return false;
			}else{//ios
				return true;
			}
		}
		return false;
	}
}

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
}

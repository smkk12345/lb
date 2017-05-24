package com.longbei.appservice.common.security;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.constant.Constant_Version;
import com.longbei.appservice.common.utils.*;

import com.longbei.appservice.dao.redis.SpringJedisDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import net.sf.json.JSONObject;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@SuppressWarnings({ "unchecked", "unused", "rawtypes", "cast" })
@WebFilter(filterName="myFilter",urlPatterns="/*")
public class SecurityFilter extends OncePerRequestFilter {
	private static String localAddress = "";
	private static SpringJedisDao springJedisDao = null;
	private static Logger logger = LoggerFactory.getLogger(SecurityFilter.class);
	 
	/**
	 * 需要验证需要做两件事 验证token token保存在redis上 处理参数
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain arg2) throws ServletException, IOException {
		String url = request.getRequestURI();
		if(StringUtils.isBlank(localAddress)){
			localAddress = request.getLocalAddr();
		}
		MDC.put("ip", localAddress);
		//是否需要过滤逻辑
//		if(isTest(request)){
//			arg2.doFilter(request, response);
//			return ;
//		}

		if(true){
			arg2.doFilter(request, response);
			return ;
		}

		//服务器之间api调用
		if(url.contains("/api/")){
//			String authorization = request.getHeader("Authorization");
//			if(url.contains("getServiceToken")){
//				arg2.doFilter(request, response);
//				return;
//			}
//			if(StringUtils.isBlank(authorization)){
//				//未发现token
//				returnAfterErrorToken(request, response, Constant.STATUS_SYS_1000, Constant.RTNINFO_SYS_1000);
//				return;
//			}
//			String token = authorization.replaceFirst("Basic", "");
//			logger.debug(authorization);
//			Claims claims = Jwts.parser()
//					.setSigningKey(DatatypeConverter.parseBase64Binary(Constant.TOKEN_SIGN_COMMON))
//					.parseClaimsJws(token).getBody();
//			if(!"commonservice".equals(claims.getSubject())){
//				//错误的token
//				returnAfterErrorToken(request, response, Constant.STATUS_SYS_1001, Constant.RTNINFO_SYS_1001);
//				return;
//			}
//			if(claims.getExpiration().getTime() <= new Date().getTime()){
//				//token失效
//				returnAfterErrorToken(request, response, Constant.STATUS_SYS_1002, Constant.RTNINFO_SYS_1002);
//				return;
//			}
			arg2.doFilter(request, response);
			return;
		}

		
		//app调用
		boolean doFilter = true;
		if(doFilter){
			String time = request.getParameter("time");
			String sigin = request.getParameter("signature");
			String uid = request.getParameter("uid");
			String param = request.getParameter("param");
			// 0221 替换掉换行符
			param = param.replaceAll("%3D", "");
			param = param.replaceAll("%0A", "");
			String version = request.getParameter("version");
			long localTime = new Date().getTime() / 1000;
			if (Math.abs(localTime - Double.parseDouble(time)) > 10*60) {
				returnAfterErrorToken(request, response, Constant.STATUS_SYS_09, Constant.RTNINFO_SYS_09);
				return;
			}
			//重组request参数
			String paramStr = DecodesUtils.getFromBase64(param);
			JSONObject jMap = JSONObject.fromObject(paramStr);
			Iterator<String> strIt = jMap.keys();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("version", version);
			while (strIt.hasNext()) {
				String key = (String) strIt.next();
				Object value = jMap.get(key);
				map.put(key, value);
			}
			String token = "";
			try {
				String urlPath = request.getRequestURI();
				urlPath = urlPath.replace("/app_service/", "");
				if (Constant.NOT_NEED_SECURITY_FILTER_URL_ARR.indexOf(urlPath) > -1
						|| (uid.equals(Constant.VISITOR_UID)
						&&Constant.VISITOR_URL.indexOf(urlPath)>-1)) {
					token = "longbei2017";
				} else {
					token = springJedisDao.get("userid&token&"+uid);
				}
//				String vers[] = version.split("_");
//				if (vers.length > 0) {
//					if(!canLogin(version)){
//						returnAfterErrorToken(request, response, Constant.STATUS_SYS_01,
//								"请前往应用中心更新版本");
//						return;
//					}
//				}
				if (StringUtils.isBlank(token)) {
					//token过期
					returnAfterErrorToken(request, response, Constant.STATUS_SYS_08, Constant.RTNINFO_SYS_08);
					return;
				} else {
					request = new ParamHttpServletRequestWrapper((HttpServletRequest) request, map);
					String localSigin = TokenUtils.getSigin(urlPath, time, uid, param, token);
					if (sigin.equals(localSigin)) {
						arg2.doFilter(request, response);
					} else {
						// 匹配失败之后
						returnAfterErrorToken(request, response, Constant.STATUS_SYS_08, Constant.RTNINFO_SYS_08);
						return;
					}
				}
			} catch (Exception e) {
				logger.error("token error url = "+url+" and msg = "+e);
			}
		}
		
	}

	private boolean canLogin(String version){
		// IOS升级
		boolean result = true;
		String vers[] = version.split("_");
		// 获取当前版本 判断是否是最新版本
		String newver = vers[2].trim().replaceAll("\\.", "");
		String oldVersion = Constant_Version.VERSION_000000+"";

		int size = getMinSize(oldVersion,newver);
		for (int i = 0; i < size; i++) {
			int newi = newver.charAt(i);
			int enforceveri = oldVersion.charAt(i);
			if (enforceveri > newi) {
				result = false;
				break;
			}
		}
//		logger.info("version = {},oldVersion={},result={}",newver,oldVersion,result);
		return result;
	}

	private int getMinSize(String newVersion,String cversion){
		if(newVersion.length() > cversion.length()){
			return cversion.length();
		}else{
			return newVersion.length();
		}
	}

	/**
	* @Title: returnAfterErrorToken
	* @Description: 出现token失效或者时间等问题 
	* @param @param request
	* @param @param response
	* @param @param status
	* @param @param rtnInfo
	* @auther smkk
	* @currentdate:2017年1月17日
	 */
	private void returnAfterErrorToken(HttpServletRequest request, HttpServletResponse response, final int status, final String rtnInfo) {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			BaseResp<Object> baseResp = new BaseResp<>(status, rtnInfo);
			try {
				out.print(JSONObject.fromObject(baseResp));
			} catch (Exception e) {
				logger.error("token错误：" + LoggerUtil.getTrace(e));
			}
		} catch (Exception e) {
			logger.error("token错误：" + LoggerUtil.getTrace(e));
		} finally {
			out.close();
		}
	}

	// 获取bean
	private Object getBeanFromContext(HttpServletRequest request, String beanName) {
		Object result = null;
		try {
			BeanFactory beans = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
			result = beans.getBean(beanName);
		} catch (Exception e) {
			logger.error("token错误：" + LoggerUtil.getTrace(e));
		}
		return result;
	}

	/**
	 * 从request中获得参数Map，并返回可读的Map
	 * @param request
	 * @return
	 */
	public static Map getParameterMap(HttpServletRequest request) {
		// 参数Map
		Map properties = request.getParameterMap();
		// 返回值Map
		Map returnMap = new HashMap();
		Iterator entries = properties.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value += values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			returnMap.put(name, value);
		}
		return returnMap;
	}

	public String getIpAddr(HttpServletRequest request) {
		getType(request);
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	private void getType(HttpServletRequest request) {
		Enumeration headerNames = request.getHeaderNames(); // 先获取头信息
		while (headerNames.hasMoreElements()) { // 如果有头的话
			String headerName = (String) headerNames.nextElement();// 获取首个头元素
			if (headerName.equals("x-up-calling-line-id")) {// 如果头信息是x-up-calling-line-id那基本上可以确定是手机了
				// temvit=request.getHeader(headerName);//再进一步确认
				// if (temvit.substring(0,3).trim().equals("861") ||
				// temvit.substring(0,2).trim().equals("13")){
			}
		}
	}
	
	@Override
	protected void initFilterBean() throws ServletException {
		if(null == springJedisDao){
			BeanFactory beans = WebApplicationContextUtils
					.getWebApplicationContext(getServletContext());

			try {
				springJedisDao = (SpringJedisDao) beans.getBean("springJedisDao");
			} catch (Exception e) {

			}
		}
	}
	/**
	* @Title: setName
	* @Description: 判断请求方式  暂时默认get是测试  不走token
	* @param @param name
	* @auther smkk
	* @currentdate:2017年1月17日
	*/
	public Boolean isTest(HttpServletRequest request) {
		String method = request.getMethod();
		if("GET".equalsIgnoreCase(method)){
			return true;
		}else{
			return false;
		}
	}

	
}

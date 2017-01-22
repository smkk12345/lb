/**   
* @Title: UserCheckinDetailService.java 
* @Package com.longbei.appservice.service 
* @Description: TODO(用一句话描述该文件做什么) 
* @author A18ccms A18ccms_gmail_com   
* @date 2017年1月22日 上午9:57:07 
* @version V1.0   
*/
package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;

/**
 * @author smkk
 *
 */
public interface UserCheckinDetailService {
	/**
	* @Title: checkIn
	* @Description: 用户签到
	* @param @param userid
	* @auther smkk
	* @currentdate:2017年1月22日
	 */
	BaseResp<Object> checkIn(long userid);
}

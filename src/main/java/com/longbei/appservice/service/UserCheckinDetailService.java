/**   
* @Title: UserCheckinDetailService.java 
* @Package com.longbei.appservice.service 
* @Description: TODO(用一句话描述该文件做什么) 
* @author A18ccms A18ccms_gmail_com   
* @date 2017年1月22日 上午9:57:07 
* @version V1.0   
*/
package com.longbei.appservice.service;

import java.util.List;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserCheckinDetail;

/**
 * @author smkk
 *
 */
public interface UserCheckinDetailService {
	
//	BaseResp<Object> insertSelective(UserCheckinDetail record);
	
//	UserCheckinDetail selectByPrimaryKey(Integer id);
	
//	BaseResp<Object> updateByPrimaryKeySelective(UserCheckinDetail record);
	
	/**
	 * @author yinxc
	 * 获取签到详情记录
	 * 2017年2月22日
	 * return_type
	 */
	BaseResp<Object> selectDetailList(long userid);
	
	/**
	 * @author yinxc
	 * 根据年月获取签到详情记录
	 * 2017年2月22日
	 * return_type
	 */
	BaseResp<List<UserCheckinDetail>> selectDetailListByYearmonth(long userid, Integer yearmonth);
	
	/**
	 * @author yinxc
	 * 判断用户是否签到    未签到:添加redis  判断是否是连续签到(5天以上存库---更新redis,及库)   
	 * 				已签到：返回
	 * 2017年2月23日
	 * return_type
	 * UserCheckinDetailService
	 */
	BaseResp<Object> selectIsCheckIn(long userid);
}

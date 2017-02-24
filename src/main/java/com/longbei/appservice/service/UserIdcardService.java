package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserIdcard;

public interface UserIdcardService {

	/**
	 * @author yinxc
	 * 添加身份证认证信息
	 * 2017年1月20日
	 * int
	 * UserIdcardService
	 */
	BaseResp<Object> insert(UserIdcard record);
	
	/**
	 * @author yinxc
	 * 查看身份证认证信息
	 * 2017年1月20日
	 * BaseResp<Object>
	 * UserIdcardService
	 */
	UserIdcard selectByUserid(String userid);
	
	/**
	 * @author yinxc
	 * 帐号与安全(获取身份验证状态)
	 * 2017年2月24日
	 * return_type
	 * UserIdcardService
	 */
	BaseResp<Object> userSafety(long userid);
	
	/**
	 * @author yinxc
	 * 修改身份证认证信息
	 * 2017年1月20日
	 * int
	 * UserIdcardService
	 */
	BaseResp<Object> update(UserIdcard record);
	
}

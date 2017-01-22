package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserFeedback;

public interface UserFeedbackService {

	/**
	 * @author yinxc
	 * 添加意见反馈
	 * 2017年1月19日
	 * return_type
	 */
	BaseResp<Object> insertSelective(UserFeedback record);
	
}

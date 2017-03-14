package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserLevel;

public interface UserLevelService {

	/**
	 * @author yinxc
	 * 根据userid获取等级grade信息
	 * 2017年3月9日
	 * grade 等级
	 */
	BaseResp<UserLevel> selectByUserid(long userid);
    		
}

package com.longbei.appservice.service;

import java.util.Date;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserInterests;

public interface UserInterestsService {


	/**
	 * @Title: insertInterests
	 * @Description: 添加兴趣信息
	 * @param @param userid
	 * @param @param ids
	 * @param @return
	 * @auther IngaWu
	 * @currentdate:2017年2月24日
	 */
	BaseResp<Object> updateInterests(String userid,String ids);


	/**
	 * @Title: selectInterests
	 * @Description: 获取用户兴趣标签
	 * @param userid
	 * @auther IngaWu
	 * @currentdate:2017年5月9日
	 */
	BaseResp<UserInterests> selectInterests(long userid);
}

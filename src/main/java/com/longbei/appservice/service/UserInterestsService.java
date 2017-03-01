package com.longbei.appservice.service;

import java.util.Date;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserInterests;

public interface UserInterestsService {

	/**
	 * @Title: updateInterests
	 * @Description: 更改用户的兴趣信息
	 * @param @param id
	 * @param @param userid
	 * @param @param ptype十全十美类型
	 * @param @param perfectname十全十美名
	 * @param @return
	 * @auther IngaWu
	 * @currentdate:2017年2月23日
	 */
	BaseResp<Object> updateInterests(int id,String ptype,String perfectname);

	/**
	 * @Title: insertInterests
	 * @Description: 添加兴趣信息
	 * @param @param userid
	 * @param @param ptype十全十美类型
	 * @param @param perfectname十全十美名
	 * @param @return
	 * @auther IngaWu
	 * @currentdate:2017年2月24日
	 */
	BaseResp<Object> insertInterests(String userid,String ptype,String perfectname);

	/**
	 * @Title: deleteInterests
	 * @Description: 删除兴趣信息
	 * @param @param  id，userid
	 * @param @return
	 * @auther IngaWu
	 * @currentdate:2017年2月24日
	 */
	BaseResp<Object> deleteInterests(int id,String userid);
}

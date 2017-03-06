package com.longbei.appservice.service;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.longbei.appservice.common.BaseResp;


import java.util.Date;

public interface UserPlDetailService {

	/**
	 * @Title: selectNowscoreAndDiffById
	 * @Description: 查询用户现在的分数和距离升级的差分
	 * @param @param id（userPlDetail的id）
	 * @return @return nowscore现在的分数 diff距离升级的差分
	 * @auther IngaWu
	 * @currentdate:2017年3月6日
	 */
	BaseResp<Object> selectNowscoreAndDiffById(int id);

	/**
	 * @Title: selectUserPerfectListByUserId
	 * @Description: 查询用户十全十美的信息列表
	 * @param @param userid  startNum分页起始值 pageSize每页显示条数
	 * @param @return
	 * @auther IngaWu
	 * @currentdate:2017年3月6日
	 */
	BaseResp<Object> selectUserPerfectListByUserId(long userid,int startNum,int pageSize);

}

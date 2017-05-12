package com.longbei.appservice.service;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserPlDetail;


import java.util.Date;

public interface UserPlDetailService {

	/**
	 * @Title: selectNowscoreAndDiffByUseridAndPtype
	 * @Description: 查询用户现在的分数和距离升级的差分
	 * @param @param id（userPlDetail的id）
	 * @return @return nowscore现在的分数 diff距离升级的差分
	 * @auther IngaWu
	 * @currentdate:2017年3月6日
	 */
	BaseResp<Object> selectNowscoreAndDiffByUseridAndPtype(long userid,String ptype);

	/**
	 * @Title: selectUserPerfectListByUserId
	 * @Description: 查询用户十全十美的信息列表
	 * @param @param userid  startNum分页起始值 pageSize每页显示条数
	 * @param @return
	 * @auther IngaWu
	 * @currentdate:2017年3月6日
	 */
	BaseResp<Object> selectUserPerfectListByUserId(long userid,int startNum,int pageSize);

	/**
	 * @Title: insertUserPlDetail
	 * @Description: 添加用户十全十美的信息
	 * @auther IngaWu
	 * @currentdate:2017年5月3日
	 */
	Integer insertUserPlDetail (UserPlDetail userPlDetail);
}

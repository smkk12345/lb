package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;

public interface UserPointDetailService {

	/**
	 * @Title: selectPointListByUseridAndPointtype
	 * @Description: 获取用户发进步积分列表
	 * @param @param userid 用户id
	 * @param @param pointtype 积分类型
	 * @param @param startNum分页起始值
	 * @param @param pageSize每页显示条数
	 * @auther IngaWu
	 * @currentdate:2017年4月5日
	 */
	BaseResp<Object> selectPointListByUseridAndPtype(long userid,String pointtype,int startNum,int pageSize);
}

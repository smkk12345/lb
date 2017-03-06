package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;

public interface SysPerfectInfoService {

	/**
	 * @Title: selectPerfectInfoById
	 * @Description: 查询单个十全十美类型的信息
	 * @param @param Ptype十全十美类型 0 全部 1学习 等
	 * @param @return
	 * @auther IngaWu
	 * @currentdate:2017年3月6日
	 */
	BaseResp<Object> selectPerfectInfoByPtype(String ptype);

}

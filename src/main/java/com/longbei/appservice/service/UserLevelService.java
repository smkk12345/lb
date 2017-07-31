package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.UserLevel;

import java.util.List;

public interface UserLevelService {

	/**
	 * @author yinxc
	 * 根据userid获取等级grade信息
	 * 2017年3月9日
	 * grade 等级
	 */
	BaseResp<UserLevel> selectByUserid(long userid);

	/**
	 * @Description: 查看用户等级列表
	 * @param startNum 分页起始值
	 * @param pageSize 每页显示条数
	 * @auther IngaWu
	 * @currentdate:2017年6月1日
	 */
	Page<UserLevel> selectUserLevelList(Integer startNum, Integer pageSize);

	/**
	 * 批量插入等级权限
	 * @param levelList
	 * @return
	 */
	BaseResp insertBatchLevelRules(List<UserLevel> levelList);

	/**
	 * 全部删除等级权限
	 * @return
	 */
	BaseResp deleteLevelRules();

    		
}

package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserImpCoinDetail;

/**
 * @author yinxc
 * 进步币
 * 2017年2月24日
 * return_type
 * UserImpCoinDetailService
 */
public interface UserImpCoinDetailService {
	
	BaseResp<Object> deleteByPrimaryKey(Integer id);

	BaseResp<Object> insertSelective(UserImpCoinDetail record);
	
	UserImpCoinDetail selectByPrimaryKey(Integer id);
	
	BaseResp<Object> updateByPrimaryKeySelective(UserImpCoinDetail record);
	
	/**
	 * @author yinxc
	 * 获取进步币明细列表
	 * 2017年2月23日
	 * return_type
	 */
	BaseResp<Object> selectListByUserid(long userid, int pageNo, int pageSize);
    
}

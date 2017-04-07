package com.longbei.appservice.service;

import java.util.List;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserFlowerDetail;

public interface UserFlowerDetailService {
	
	/**
	 * @author yinxc
	 * 获取用户被赠与鲜花总数
	 * 2017年4月6日
	 */
	BaseResp<Integer> selectCountFlower(long userid);

	/**
	 * @author yinxc
	 * 花公用添加明细方法
	 * 2017年3月21日
	 * param userid 
	 * param origin： 来源  0:龙币兑换;  1:赠与;  2:进步币兑换
	 *
	 * param number 鲜花数量 --- 消耗：(1:赠与;)value值为负---方法里面已做判断
	 * param improveid 业务id  类型：     
	 * 						赠与：进步id
	 * param friendid 
	 * @desc 方法调用，根据龙币兑换花，进步币比例(AppserviceConfig)，查询出number数量
	 */
	BaseResp<Object> insertPublic(long userid, String origin, int number, long improveid, long friendid);
	
	/**
	 * @author yinxc
	 * 获取鲜花明细列表
	 * 2017年2月23日
	 * return_type
	 * UserImpCoinDetailMapper
	 */
	BaseResp<List<UserFlowerDetail>> selectListByUserid(long userid, int pageNo, int pageSize);
	
	/**
	 * @author yinxc
	 * 获取用户龙币，进步币以及龙币兑换进步币比例
	 * 2017年2月23日
	 */
	BaseResp<Object> selectUserInfoByUserid(long userid);
	
	/**
	 * @author yinxc
	 * 龙币兑换鲜花
	 * 2017年3月21日
	 * param userid 
	 * param number 鲜花数量 
	 * @desc 根据龙币兑换花，进步币比例(AppserviceConfig)，查询出number数量
	 */
	BaseResp<Object> moneyExchangeFlower(long userid, int number);
	
	/**
	 * @author yinxc
	 * 进步币兑换鲜花
	 * 2017年3月21日
	 * param userid 
	 * param number 鲜花数量 
	 * @desc 根据龙币兑换花，进步币比例(AppserviceConfig)，查询出number数量
	 */
	BaseResp<Object> coinExchangeFlower(long userid, int number);
	
}

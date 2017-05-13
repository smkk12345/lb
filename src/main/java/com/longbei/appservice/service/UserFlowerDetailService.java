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
	 * param origin： 来源  0:龙币兑换;  1:赠与;  2:进步币兑换      3:被赠与
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
	 * 获取鲜花明细列表
	 * 2017年2月23日
	 * param origin： 0:龙币兑换;  1:赠与---龙币兑换    2:进步币兑换    3:被赠与---龙币兑换
	 *						4:赠与---进步币兑换    5:被赠与---进步币兑换
	 */
	BaseResp<List<UserFlowerDetail>> selectListByUseridAndOrigin(long userid, String origin, int pageNo, int pageSize);
	
	/**
	 * @author yinxc
	 * 获取用户龙币，进步币以及龙币兑换进步币比例
	 * 2017年2月23日
	 */
	BaseResp<Object> selectUserInfoByUserid(long userid);
	
	/**
	 * @author yinxc
	 * 龙币兑换鲜花
	 * 2017年4月10日
	 * @param @param  userid 
	 * @param @param  number 鲜花数量
	 * @param @param friendid被赠送人id
     * @param @param improveid    进步id
     * @param @param businessid  各类型对应的id
     * @param @param businesstype  类型    0 零散进步评论   1 目标进步评论    2 榜评论  3圈子评论 4 教室评论 
	 * @desc 根据龙币兑换花，进步币比例(AppserviceConfig)，查询出number数量
	 */
	BaseResp<Object> moneyExchangeFlower(long userid, int number, String friendid, 
    		String improveid, String businesstype, String businessid);
	
	/**
	 * @author yinxc
	 * 进步币兑换鲜花
	 * 2017年4月10日
	 * @param @param  userid 
	 * @param @param  number 鲜花数量
	 * @param @param friendid被赠送人id
     * @param @param improveid    进步id
     * @param @param businessid  各类型对应的id
     * @param @param businesstype  类型    0 零散进步评论   1 目标进步评论    2 榜评论  3圈子评论 4 教室评论 
	 * @desc 根据龙币兑换花，进步币比例(AppserviceConfig)，查询出number数量
	 */
	BaseResp<Object> coinExchangeFlower(long userid, int number, String friendid, 
    		String improveid, String businesstype, String businessid);
	
}

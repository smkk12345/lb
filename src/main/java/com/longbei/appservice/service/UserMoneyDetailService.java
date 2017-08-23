package com.longbei.appservice.service;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.entity.UserMoneyDetail;

/**
 * @author yinxc
 * 龙币
 * 2017年2月27日
 * return_type
 * UserMoneyDetailService
 */
public interface UserMoneyDetailService {
	
	UserMoneyDetail selectByPrimaryKey(Integer id);

	/**
	 * @author yinxc
	 * 龙币公用添加明细方法
	 * 2017年2月27日
	 * param userid 
	 * param origin： 来源   0:充值  购买     1：购买礼物(花,钻)  2:兑换商品时抵用进步币
	 * 					3：设榜单    4：赞助榜单    5：赞助教室  6:取消订单返还龙币  7: 设榜单后台审核不通过返还龙币   
	 * 
	 * param number 数量 --- 消耗：(除充值  购买外)value值为负---方法里面已做判断
	 * param friendid 
	 */
	BaseResp<Object> insertPublic(long userid, String origin, int number, long friendid);
	
	/**
	 * @author yinxc
	 * 获取龙币明细列表
	 * 2017年2月27日
	 * return_type
	 */
	BaseResp<Object> selectListByUserid(long userid, int pageNo, int pageSize);

	/**
	 *
	 * @param userInfo
	 * @param origin
	 * @param number
	 * @param friendid
	 * @return
	 */
	BaseResp<Object> insertPublic(UserInfo userInfo, String origin, int number, long friendid);

	/**
	 * 获取龙币总量
	 * @param userid  用户id
	 * @param origin  //origin： 来源   0:充值  购买     1：购买礼物(花,钻)  2:兑换商品时抵用龙币
	//3：发布龙榜    4：赞助榜单    5：赞助教室  6:取消订单返还龙币 7 龙榜审核未通过返回。8 龙榜撤回。   9 龙榜奖品剩余
	// 10.榜单提交审核  11：教室收益   12：教室收费  13：送礼物支出
	 * @return
	 */
	BaseResp<Integer> selectMoneyNum(long userid,String origin);
	
}

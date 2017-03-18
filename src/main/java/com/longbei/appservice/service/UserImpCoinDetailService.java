package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserImpCoinDetail;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yinxc
 * 进步币
 * 2017年2月24日
 * return_type
 * UserImpCoinDetailService
 */
public interface UserImpCoinDetailService extends BaseService{
	
	BaseResp<Object> deleteByPrimaryKey(Integer id);

//	BaseResp<Object> insertSelective(UserImpCoinDetail record);
	
	UserImpCoinDetail selectByPrimaryKey(Integer id);
	
	BaseResp<Object> updateByPrimaryKeySelective(UserImpCoinDetail record);
	
	/**
	 * @author yinxc
	 * 获取进步币明细列表
	 * 2017年2月23日
	 * return_type
	 */
	BaseResp<Object> selectListByUserid(long userid, int pageNo, int pageSize);
	
	/**
	 * @author yinxc
	 * 进步币公用添加明细方法
	 * 2017年2月25日
	 * param userid 
	 * param origin： 来源   0:签到   1:发进步  2:分享  3：邀请好友  4：榜获奖  5：收到钻石礼物 
	 * 					6：收到鲜花礼物  7:兑换商品  8：公益抽奖获得进步币  
	 * 					9：公益抽奖消耗进步币  10.消耗进步币(例如超级用户扣除进步币)
	 *
	 * param number 数量 --- 消耗：(7:兑换商品    9：公益抽奖消耗进步币)value值为负---方法里面已做判断
	 * param impid 业务id  类型：     
	 * 						发进步：进步id，  榜获奖：榜id，   收到钻石礼物和收到鲜花礼物：进步id
	 * 						兑换商品:商品id
	 * param friendid 
	 */
	BaseResp<Object> insertPublic(long userid, String origin, int number, long impid, Long friendid);
	
	/**
	 * @author yinxc
	 * 获取我的钱包
	 * 2017年2月25日
	 * param userid 
	 */
	BaseResp<Object> selectWallet(long userid);

	/**
	 * 进步币发生变化之后
	 * @param userid  用户id
	 * @param icon 进步币数量
	 * @param origin  进步币变化类型
	 * @return
	 */
	@Transactional
	BaseResp<Object> impIconChange(long userid,int icon,String origin);

    
}

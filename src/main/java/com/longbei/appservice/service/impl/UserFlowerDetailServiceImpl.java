package com.longbei.appservice.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.dao.UserFlowerDetailMapper;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.entity.UserFlowerDetail;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.service.ImproveService;
import com.longbei.appservice.service.UserFlowerDetailService;
import com.longbei.appservice.service.UserImpCoinDetailService;
import com.longbei.appservice.service.UserMoneyDetailService;

@Service("userFlowerDetailService")
public class UserFlowerDetailServiceImpl extends BaseServiceImpl implements UserFlowerDetailService {
	
	@Autowired
	private UserFlowerDetailMapper userFlowerDetailMapper;
	@Autowired
	private IdGenerateService idGenerateService;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private UserMoneyDetailService userMoneyDetailService;
	@Autowired
	private UserImpCoinDetailService userImpCoinDetailService;
	@Autowired
	private ImproveService improveService;
	
	private static Logger logger = LoggerFactory.getLogger(UserFlowerDetailServiceImpl.class);

	
	/**
	 * @author yinxc
	 * 花公用添加明细方法
	 * 2017年3月21日
	 * param userid 
	 * param origin： 来源  0:龙币兑换;  1:赠与    2:进步币兑换    3:被赠与
	 *
	 * param number 鲜花数量 --- 消耗：(1:赠与;)value值为负---方法里面已做判断
	 * param improveid 业务id  类型：     
	 * 						赠与：进步id
	 * param friendid 
	 * @desc 方法调用，根据龙币兑换花，进步币比例(AppserviceConfig)，查询出number数量
	 */
	@Transactional
	@Override
	public BaseResp<Object> insertPublic(long userid, String origin, int number, long improveid, long friendid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			UserFlowerDetail userFlowerDetail = new UserFlowerDetail();
			userFlowerDetail.setDrawdate(new Date());
			userFlowerDetail.setFriendid(friendid);
			//ftype  0:花朵;1:花束;2:花篮
			userFlowerDetail.setFtype("0");
			userFlowerDetail.setId(idGenerateService.getUniqueIdAsLong());
			userFlowerDetail.setImproveid(improveid);
			if("1".equals(origin)){
				//消耗
				number = 0- number;
			}
			userFlowerDetail.setNumber(number);
			//origin   来源，0:龙币兑换;  1:赠与;  2:进步币兑换
			userFlowerDetail.setOrigin(origin);
			userFlowerDetail.setUserid(userid);
			boolean temp = insert(userFlowerDetail);
			if (temp) {
				reseResp.setData(userFlowerDetail);
				//查询龙币兑换花的比例
				int moneytoflower = AppserviceConfig.moneytoflower;
				//花兑换进步币比例  
				int flowertocoin = AppserviceConfig.flowertocoin;
				//修改user_info表用户总花，总龙币数
				//origin  0:龙币兑换       1:赠与;  2:进步币兑换
				if("0".equals(origin)){
					userInfoMapper.updateMoneyAndFlowerByUserid(userid, -number*moneytoflower, number);
					//添加一条龙币消费明细
					//origin： 来源   0:充值  购买     1：购买礼物(花,钻)  2:兑换商品时抵用进步币
					// 					3：设榜单    4：赞助榜单    5：赞助教室  6:取消订单返还龙币 
					userMoneyDetailService.insertPublic(userid, "1", number*moneytoflower, 0);
				}else if("1".equals(origin)){
					userInfoMapper.updateMoneyAndFlowerByUserid(userid, 0, -number);
				}else if("2".equals(origin)){
					userInfoMapper.updateCoinAndFlowerByUserid(userid, -number*flowertocoin, number);
					//添加一条进步币消费明细
					//origin： 来源   0:签到   1:发进步  2:分享  3：邀请好友  4：榜获奖  5：收到钻石礼物 
					// 					6：收到鲜花礼物  7:兑换商品  8：公益抽奖获得进步币  
					// 					9：公益抽奖消耗进步币  10.消耗进步币(例如超级用户扣除进步币)
					// 					11:取消订单返还龙币   12:兑换鲜花
					userImpCoinDetailService.insertPublic(userid, "12", number*flowertocoin, 0, 0l);
				}
				UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
				Map<String, Object> expandData = new HashMap<>();
				if(null != userInfo){
					expandData.put("totalmoney", userInfo.getTotalmoney());
					expandData.put("totalcoin", userInfo.getTotalcoin());
					expandData.put("totalflower", userInfo.getTotalflower());
				}
				reseResp.setExpandData(expandData);
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("insertPublic userid = {}, origin = {}, number = {}, improveid = {}, friendid = {}", 
					userid, origin, number, improveid, friendid, e);
			printExceptionAndRollBackTransaction(e);
		}
		return reseResp;
	}
	
	private boolean insert(UserFlowerDetail record) {
		int temp = userFlowerDetailMapper.insertSelective(record);
		return temp > 0 ? true : false;
	}
	
	/**
	 * @author yinxc
	 * 获取用户被赠与鲜花总数
	 * 2017年4月6日
	 * origin： 来源  0:龙币兑换;  1:赠与    2:进步币兑换    3:被赠与
	 */
	@Override
	public BaseResp<Integer> selectCountFlower(long userid) {
		BaseResp<Integer> reseResp = new BaseResp<>();
		try {
			Integer sumnum = userFlowerDetailMapper.selectCountFlower(userid);
			reseResp.setData(sumnum);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectCountFlower userid = {}", userid, e);
		}
		return reseResp;
	}
	

	@Override
	public BaseResp<List<UserFlowerDetail>> selectListByUserid(long userid, int pageNo, int pageSize) {
		BaseResp<List<UserFlowerDetail>> reseResp = new BaseResp<>();
		try {
			List<UserFlowerDetail> list = userFlowerDetailMapper.selectListByUserid(userid, pageNo, pageSize);
			reseResp.setData(list);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectListByUserid userid = {}, pageNo = {}, pageSize = {}", 
					userid, pageNo, pageSize, e);
		}
		return reseResp;
	}

	@Override
	public BaseResp<Object> selectUserInfoByUserid(long userid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
			Map<String, Object> expandData = new HashMap<>();
			if(null != userInfo){
				expandData.put("totalmoney", userInfo.getTotalmoney());
				expandData.put("totalcoin", userInfo.getTotalcoin());
			}
			expandData.put("yuantomoney", AppserviceConfig.yuantomoney);
			expandData.put("moneytocoin", AppserviceConfig.moneytocoin);
			expandData.put("flowertocoin", AppserviceConfig.flowertocoin);
			expandData.put("moneytoflower", AppserviceConfig.moneytoflower);
			reseResp.setExpandData(expandData);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectUserInfoByUserid userid = {}", userid, e);
		}
		return reseResp;
	}

	/**
	 * @author yinxc
	 * 龙币兑换鲜花
	 * 2017年3月21日
	 * @param @param  userid 
	 * @param @param  number 鲜花数量
	 * @param @param friendid被赠送人id
     * @param @param improveid    进步id
     * @param @param businessid  各类型对应的id
     * @param @param businesstype  类型    0 零散进步评论   1 目标进步评论    2 榜评论  3圈子评论 4 教室评论 
	 * @desc 根据龙币兑换花，进步币比例(AppserviceConfig)，查询出number数量
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> moneyExchangeFlower(long userid, int number, String friendid, 
    		String improveid, String businesstype, String businessid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		//先判断用户龙币是否够用兑换
		UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
		//查询龙币兑换花的比例
		int moneytoflower = AppserviceConfig.moneytoflower;
		if(null != userInfo){
			if(number*moneytoflower > userInfo.getTotalmoney()){
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_23, Constant.RTNINFO_SYS_23);
			}
		}
		//origin： 来源  0:龙币兑换;  1:赠与;  2:进步币兑换
		reseResp = insertPublic(userid, "0", number, 0, 0);
		//赠送
		if(ResultUtil.isSuccess(reseResp)){
			improveService.addFlower(userid + "", friendid, improveid, number, businesstype, businessid);
		}
		reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		return reseResp;
	}
	
	/**
	 * @author yinxc
	 * 进步币兑换鲜花
	 * 2017年3月21日
	 * @param @param  userid 
	 * @param @param  number 鲜花数量
	 * @param @param friendid被赠送人id
     * @param @param improveid    进步id
     * @param @param businessid  各类型对应的id
     * @param @param businesstype  类型    0 零散进步评论   1 目标进步评论    2 榜评论  3圈子评论 4 教室评论 
	 * @desc 根据龙币兑换花，进步币比例(AppserviceConfig)，查询出number数量
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> coinExchangeFlower(long userid, int number, String friendid, 
    		String improveid, String businesstype, String businessid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		//先判断用户进步币是否够用兑换
		UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
		//花兑换进步币比例  
		int flowertocoin = AppserviceConfig.flowertocoin;
		if(null != userInfo){
			if(number*flowertocoin > userInfo.getTotalmoney()){
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_24, Constant.RTNINFO_SYS_24);
			}
		}
		//origin： 来源  0:龙币兑换;  1:赠与;  2:进步币兑换
		reseResp = insertPublic(userid, "2", number, 0, 0);
		//赠送
		if(ResultUtil.isSuccess(reseResp)){
			improveService.addFlower(userid + "", friendid, improveid, number, businesstype, businessid);
		}
		reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		return reseResp;
	}


}

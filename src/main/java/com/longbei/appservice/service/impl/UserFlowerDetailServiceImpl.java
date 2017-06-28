package com.longbei.appservice.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.longbei.appservice.service.*;
import com.longbei.appservice.service.api.productservice.IProductBasicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.dao.UserFlowerDetailMapper;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.entity.UserFlowerDetail;
import com.longbei.appservice.entity.UserInfo;

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
	@Autowired
	private UserRelationService userRelationService;
	@Autowired
	private UserMongoDao userMongoDao;
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	@Autowired
	private PayService payService;
	@Autowired
	private StatisticService statisticService;
	
	private static Logger logger = LoggerFactory.getLogger(UserFlowerDetailServiceImpl.class);

	
	/**
	 * @author yinxc
	 * 花公用添加明细方法
	 * 2017年3月21日
	 * param userid 
	 * param origin： 来源  0:龙币兑换;  1:赠与---龙币兑换    2:进步币兑换    3:被赠与---龙币兑换
	 * 						4:赠与---进步币兑换    5:被赠与---进步币兑换
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
			//origin   来源，0:龙币兑换;  1:赠与;  2:进步币兑换   3:被赠与
			userFlowerDetail.setOrigin(origin);
			userFlowerDetail.setUserid(userid);
			boolean temp = insert(userFlowerDetail);
			if (temp) {
				reseResp.setData(userFlowerDetail);

				//查询flowertomoney---花兑换龙币比例
				double flowertomoney = AppserviceConfig.flowertomoney;
				//花兑换进步币比例
				double flowertocoin = AppserviceConfig.flowertocoin;
				//修改user_info表用户总花，总龙币数
				//origin  0:龙币兑换       1:赠与;  2:进步币兑换   3:被赠与
				if("0".equals(origin)){
					int num = (int) (number*flowertomoney);
//					userInfoMapper.updateMoneyAndFlowerByUserid(userid, -num, 0);
					//修改被赠送人收到的礼物
//					userInfoMapper.updateMoneyAndFlowerByUserid(friendid, 0, number);
					//添加一条龙币消费明细
					//origin： 来源   0:充值  购买     1：购买礼物(花,钻)  2:兑换商品时抵用进步币
					// 					3：设榜单    4：赞助榜单    5：赞助教室  6:取消订单返还龙币
					userMoneyDetailService.insertPublic(userid, "1", num, 0);
				}
//				else if("1".equals(origin)){
//					userInfoMapper.updateMoneyAndFlowerByUserid(userid, 0, 0);
//				}
				else if("2".equals(origin)){
					int num = (int) (number*flowertocoin);
//					userInfoMapper.updateCoinAndFlowerByUserid(userid, -num, 0);
					//修改被赠送人收到的礼物
//					userInfoMapper.updateCoinAndFlowerByUserid(friendid, 0, number);
					//添加一条进步币消费明细
					//origin： 来源   0:签到   1:发进步  2:分享  3：邀请好友  4：榜获奖  5：收到钻石礼物
					// 					6：收到鲜花礼物  7:兑换商品  8：公益抽奖获得进步币
					// 					9：公益抽奖消耗进步币  10.消耗进步币(例如超级用户扣除进步币)
					// 					11:取消订单返还龙币   12:兑换鲜花 13:添加好友
					userImpCoinDetailService.insertPublic(userid, "12", num, 0, 0l);
				}else if("3".equals(origin)){
					//   3:被赠与---龙币兑换
					//被赠与用户，修改收到的花数量
					userInfoMapper.updateMoneyAndFlowerByUserid(userid, 0, number);
				}else if("5".equals(origin)){
					//   5:被赠与---进步币兑换
					//被赠与用户，修改收到的花数量
					userInfoMapper.updateCoinAndFlowerByUserid(userid, 0, number);
//					userInfoMapper.updateMoneyAndFlowerByUserid(friendid, 0, number);
				}
				UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
				Map<String, Object> expandData = new HashMap<>();
				if(null != userInfo){
					expandData.put("totalmoney", userInfo.getTotalmoney());
					expandData.put("totalcoin", userInfo.getTotalcoin());
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
	
	/**
	 * @author yinxc
	 * 获取鲜花明细列表
	 * 2017年4月14日
	 * param origin： 0:龙币兑换;  1:赠与---龙币兑换    2:进步币兑换    3:被赠与---龙币兑换
	 *						4:赠与---进步币兑换    5:被赠与---进步币兑换
	 */
	@Override
	public BaseResp<List<UserFlowerDetail>> selectListByUseridAndOrigin(long userid, long friendid, String origin, int pageNo,
			int pageSize) {
		BaseResp<List<UserFlowerDetail>> reseResp = new BaseResp<>();
		try {
			List<UserFlowerDetail> list = userFlowerDetailMapper.selectListByOrigin(userid, origin, pageNo, pageSize);
			if(null != list && list.size()>0){
				for (UserFlowerDetail userFlowerDetail : list) {
					//初始化用户信息
					initFlowerUserInfoByUserid(userFlowerDetail, friendid);
				}
			}
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
			expandData.put("flowertomoney", AppserviceConfig.flowertomoney);
			//getMoneyUrl 查看获得财富的方式URL    
			String getMoneyUrl = AppserviceConfig.h5_helper;
			expandData.put("moneyurl", getMoneyUrl);
			
			logger.info("selectUserInfoByUserid yuantomoney = {}, moneytocoin = {}, " +
					"flowertocoin = {}, flowertomoney = {}",
					AppserviceConfig.yuantomoney, AppserviceConfig.moneytocoin,
					AppserviceConfig.flowertocoin, AppserviceConfig.flowertomoney);
			reseResp.setExpandData(expandData);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			threadPoolTaskExecutor.execute(new Runnable() {
				@Override
				public void run() {
						try {
							payService.testWx();
						}catch (Exception e){
							logger.error("testWx ", e);
						}
				}
			});
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
	public BaseResp<Object> moneyExchangeFlower(long userid, final int number, String friendid,
    		String improveid, String businesstype, String businessid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		//先判断用户龙币是否够用兑换
		UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
		//查询flowertomoney---花兑换龙币比例
		double flowertomoney = AppserviceConfig.flowertomoney;
		if(null != userInfo){
			if(number*flowertomoney > userInfo.getTotalmoney()){
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_23, Constant.RTNINFO_SYS_23);
			}
		}
		//origin： 0:龙币兑换;  1:赠与---龙币兑换    2:进步币兑换    3:被赠与---龙币兑换
		//						4:赠与---进步币兑换    5:被赠与---进步币兑换
		reseResp = insertPublic(userid, "0", number, 0, 0);
		//赠送
		if(ResultUtil.isSuccess(reseResp)){
			improveService.addFlower(userid + "", friendid, improveid, number, businesstype, businessid);
			//赠送后，被赠与用户添加一条花的明细  赠与用户添加一条花明细
			//来源  0:龙币兑换;  1:赠与---龙币兑换    2:进步币兑换    3:被赠与---龙币兑换
			//						4:赠与---进步币兑换    5:被赠与---进步币兑换
			insertPublic(Long.parseLong(friendid), "3", number, Long.parseLong(improveid), userid);
			insertPublic(userid, "1", number, Long.parseLong(improveid), Long.parseLong(friendid));

			//系统今日赠花总数＋number
			threadPoolTaskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					statisticService.updateStatistics(Constant.SYS_FLOWER_NUM,number);
				}
			});
		}
		Improve improve = improveService.selectImproveByImpid(Long.parseLong(improveid), userid + "", businesstype, businessid);
		if(null != improve){
			improveService.afterImproveInfoChange(improve,number,Constant.MONGO_IMPROVE_LFD_OPT_FLOWER);
			Map<String, Object> expandData = reseResp.getExpandData();
			expandData.put("totalflower", improve.getFlowers());
			reseResp.setExpandData(expandData);
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
	public BaseResp<Object> coinExchangeFlower(long userid, final int number, String friendid,
    		String improveid, String businesstype, String businessid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		//先判断用户进步币是否够用兑换
		UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
		//花兑换进步币比例  
		double flowertocoin = AppserviceConfig.flowertocoin;
		if(null != userInfo){
			if(number*flowertocoin > userInfo.getTotalcoin()){
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_24, Constant.RTNINFO_SYS_24);
			}
		}
		//origin： 0:龙币兑换;  1:赠与---龙币兑换    2:进步币兑换    3:被赠与---龙币兑换
		//						4:赠与---进步币兑换    5:被赠与---进步币兑换
		reseResp = insertPublic(userid, "2", number, 0, 0);
		//赠送
		if(ResultUtil.isSuccess(reseResp)){
			improveService.addFlower(userid + "", friendid, improveid, number, businesstype, businessid);
			//赠送后，被赠与用户添加一条花的明细  赠与用户添加一条花明细
			//origin： 0:龙币兑换;  1:赠与---龙币兑换    2:进步币兑换    3:被赠与---龙币兑换
			//						4:赠与---进步币兑换    5:被赠与---进步币兑换
			insertPublic(Long.parseLong(friendid), "5", number, Long.parseLong(improveid), userid);
			insertPublic(userid, "4", number, Long.parseLong(improveid), Long.parseLong(friendid));

			//系统今日赠花总数＋number
			threadPoolTaskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					statisticService.updateStatistics(Constant.SYS_FLOWER_NUM,number);
				}
			});
		}
		Improve improve = improveService.selectImproveByImpid(Long.parseLong(improveid), userid + "", businesstype, businessid);
		if(null != improve){
			improveService.afterImproveInfoChange(improve,number,Constant.MONGO_IMPROVE_LFD_OPT_FLOWER);
			Map<String, Object> expandData = reseResp.getExpandData();
			expandData.put("totalflower", improve.getFlowers());
			reseResp.setExpandData(expandData);
		}
		reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		return reseResp;
	}

	
	/**
     * 初始化用户信息 ------Userid
     */
    private void initFlowerUserInfoByUserid(UserFlowerDetail userFlowerDetail, long userid){
    	if(userFlowerDetail.getFriendid() != null){
    		//获取好友昵称
    		String remark = userRelationService.selectRemark(userid, userFlowerDetail.getFriendid(), "0");
            AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(userFlowerDetail.getFriendid()));
            if(null != appUserMongoEntity){
            	if(!StringUtils.isBlank(remark)){
            		appUserMongoEntity.setNickname(remark);
            	}
            	userFlowerDetail.setAppUserMongoEntity(appUserMongoEntity);
            }else{
            	userFlowerDetail.setAppUserMongoEntity(new AppUserMongoEntity());
            }
    	}
    }

}

package com.longbei.appservice.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.dao.UserMoneyDetailMapper;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.entity.UserMoneyDetail;
import com.longbei.appservice.service.UserMoneyDetailService;

@Service("userMoneyDetailService")
public class UserMoneyDetailServiceImpl implements UserMoneyDetailService {
	
	@Autowired
	private UserMoneyDetailMapper userMoneyDetailMapper;
//	@Autowired
//	private UserMongoDao userMongoDao;
	@Autowired
	private UserInfoMapper userInfoMapper;
//	@Autowired
//	private SpringJedisDao springJedisDao;
//	@Autowired
//	private UserRelationService userRelationService;
	
	private static Logger logger = LoggerFactory.getLogger(UserMoneyDetailServiceImpl.class);

	@Override
	public UserMoneyDetail selectByPrimaryKey(Integer id) {
		UserMoneyDetail userMoneyDetail = userMoneyDetailMapper.selectByPrimaryKey(id);
		return userMoneyDetail;
	}

	/**
	 * @author yinxc
	 * 龙币公用添加明细方法
	 * 2017年2月27日
	 * param userid 
	 * param origin： 来源   0:充值  购买     1：购买礼物(花,钻)  2:兑换商品时抵用进步币
	 * 					3：发布龙榜    4：赞助榜单    5：赞助教室  
	 * 					6:取消订单返还龙币 7 龙榜审核未通过返回。8 龙榜撤回。   9 龙榜奖品剩余 10.榜单提交审核
	 *
	 * param number 数量 --- 消耗：(1：购买礼物(花,钻)  2:兑换商品时抵用进步币 3：发布龙榜    4：赞助榜单    5：赞助教室)
	 * 						value值为负---方法里面已做判断
	 * param friendid 
	 */
	@Override
	public BaseResp<Object> insertPublic(long userid, String origin, int number, long friendid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			UserMoneyDetail userMoneyDetail = new UserMoneyDetail();
			userMoneyDetail.setCreatetime(new Date());
			userMoneyDetail.setUpdatetime(new Date());
			userMoneyDetail.setFriendid(friendid);
			userMoneyDetail.setOrigin(origin);
			userMoneyDetail.setUserid(userid);
			if("0".equals(origin) || "6".equals(origin) || "7".equals(origin) 
					|| "8".equals(origin) || "9".equals(origin)){
				userMoneyDetail.setNumber(number);
			}else{
				number = 0 - number;
				userMoneyDetail.setNumber(number);
			}
			boolean temp = insert(userMoneyDetail);
			if (temp) {
				userInfoMapper.updateMoneyAndFlowerByUserid(userid, number, 0);
//				UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userid);
//				int allnum = userInfo.getTotalmoney() + userMoneyDetail.getNumber();
//				userInfo.setTotalmoney(allnum);
//				userInfoMapper.updateByUseridSelective(userInfo);
				
				//先从redis里面取，如果存在，数据累加，减，之后修改表数据，   不存在存入redis
//				boolean result = springJedisDao.hasKey(Constant.RP_USER_MONEY_VALUE + userid);
//				if(result){
//					//存在
//					jedisKey(userid, userMoneyDetail.getNumber());
//				}else{
//					//不存在   数据库查找   存入redis
//					UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userid);
//					//并发时，判断是否已经存在
//					boolean res = springJedisDao.hasKey(Constant.RP_USER_MONEY_VALUE + userid);
//					if(!res){
//						//不存在
//						springJedisDao.set(Constant.RP_USER_MONEY_VALUE + userid, userInfo.getTotalcoin().toString(), 10);
//						//递增
//						springJedisDao.increment(Constant.RP_USER_MONEY_VALUE + userid, userMoneyDetail.getNumber());
//						String value = springJedisDao.get(Constant.RP_USER_MONEY_VALUE + userid);
//						//修改数据库数据
//						userInfoMapper.updateTotalmoneyByUserid(userid, Integer.parseInt(value));
//					}else{
//						//存在
//						jedisKey(userid, userMoneyDetail.getNumber());
//					}
//
//				}
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insertPublic userid = {}, origin = {}, number = {}, friendid = {}",
					userid, origin, number, friendid, e);
		}
		return reseResp;
	}
	
//	private void jedisKey(long userid, Integer number){
//		//存在
//		springJedisDao.increment(Constant.RP_USER_MONEY_VALUE + userid, number);
//		//重新设置过期时间---10秒
//		String value = springJedisDao.get(Constant.RP_USER_MONEY_VALUE + userid);
//		springJedisDao.set(Constant.RP_USER_MONEY_VALUE + userid, value, 10);
//		//修改数据库数据
//		userInfoMapper.updateTotalmoneyByUserid(userid, Integer.parseInt(value));
//	}
	
	private boolean insert(UserMoneyDetail record){
		int temp = userMoneyDetailMapper.insertSelective(record);
		return temp > 0 ? true : false;
	}

	/**
	 * @author yinxc
	 * 获取龙币明细列表
	 * 2017年2月27日
	 * return_type
	 */
	@Override
	public BaseResp<Object> selectListByUserid(long userid, int pageNo, int pageSize) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<UserMoneyDetail> list = userMoneyDetailMapper.selectListByUserid(userid, pageNo, pageSize);
//			if(null != list && list.size()>0){
//				for (UserMoneyDetail userMoneyDetail : list) {
//					//初始化用户信息
//					initMsgUserInfoByUserid(userMoneyDetail, userid);
//				}
//			}
			reseResp.setData(list);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectListByUserid userid = {}, pageNo = {}, pageSize = {}", userid, pageNo, pageSize, e);
		}
		return reseResp;
	}

	@Override
	public BaseResp<Object> insertPublic(UserInfo userInfo, String origin, int number, long friendid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			UserMoneyDetail userMoneyDetail = new UserMoneyDetail();
			userMoneyDetail.setCreatetime(new Date());
			userMoneyDetail.setUpdatetime(new Date());
			userMoneyDetail.setFriendid(friendid);
			userMoneyDetail.setOrigin(origin);
			userMoneyDetail.setUserid(userInfo.getUserid());
			if(!"0".equals(origin)){
				userMoneyDetail.setNumber(0 - number);
			}else{
				userMoneyDetail.setNumber(number);
			}
			boolean temp = insert(userMoneyDetail);
			if (temp) {
				//修改用户userInfo表---龙币总数
				int allnum = userInfo.getTotalmoney() + userMoneyDetail.getNumber();
				userInfo.setTotalmoney(allnum);
				userInfoMapper.updateByUseridSelective(userInfo);
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insertPublic userid = {}, origin = {}, number = {}, friendid = {}",
					userInfo.getUserid(), origin, number, friendid, e);
		}
		return reseResp;
	}

	//------------------------公用方法，初始化用户龙币信息------------------------------------------
	/**
     * 初始化用户龙币信息 ------Friendid
     */
//    private void initMsgUserInfoByFriendid(UserMoneyDetail userMoneyDetail){
//    	if(!StringUtils.hasBlankParams(userMoneyDetail.getFriendid().toString())){
//    		AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(userMoneyDetail.getFriendid()));
//    		userMoneyDetail.setAppUserMongoEntityFriendid(appUserMongoEntity);
//		}
//    }

    /**
     * 初始化用户龙币信息 ------Userid
     */
//   private void initMsgUserInfoByUserid(UserMoneyDetail userMoneyDetail, long userid){
//	   if(userMoneyDetail.getFriendid() != 0){
//			//获取好友昵称
//			String remark = userRelationService.selectRemark(userid, userMoneyDetail.getFriendid(), "0");
//	       AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(userMoneyDetail.getFriendid()));
//	       if(null != appUserMongoEntity){
//		       	if(!StringUtils.isBlank(remark)){
//		       		appUserMongoEntity.setNickname(remark);
//		       	}
//		       	userMoneyDetail.setAppUserMongoEntity(appUserMongoEntity);
//	       }else{
//	    	   userMoneyDetail.setAppUserMongoEntity(new AppUserMongoEntity());
//	       }
//		}
//   }

}

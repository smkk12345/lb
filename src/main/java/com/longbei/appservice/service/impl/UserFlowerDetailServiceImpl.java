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
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.dao.UserFlowerDetailMapper;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.entity.UserFlowerDetail;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.service.UserFlowerDetailService;

@Service("userFlowerDetailService")
public class UserFlowerDetailServiceImpl extends BaseServiceImpl implements UserFlowerDetailService {
	
	@Autowired
	private UserFlowerDetailMapper userFlowerDetailMapper;
	@Autowired
	private IdGenerateService idGenerateService;
	@Autowired
	private UserInfoMapper userInfoMapper;
	
	private static Logger logger = LoggerFactory.getLogger(UserFlowerDetailServiceImpl.class);

	
	/**
	 * @author yinxc
	 * 花公用添加明细方法
	 * 2017年3月21日
	 * param userid 
	 * param origin： 来源  0:龙币兑换;  1:赠与;
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
			//origin   来源，0:龙币兑换;  1:赠与;
			userFlowerDetail.setOrigin(origin);
			userFlowerDetail.setUserid(userid);
			boolean temp = insert(userFlowerDetail);
			if (temp) {
				reseResp.setData(userFlowerDetail);
				//查询龙币兑换花的比例
				int moneytoflower = AppserviceConfig.moneytoflower;
				//修改user_info表用户总花，总龙币数
				//0:龙币兑换       1:赠与;
				if("0".equals(origin)){
					userInfoMapper.updateMoneyAndFlowerByUserid(userid, -number, number/moneytoflower);
				}else if("1".equals(origin)){
					userInfoMapper.updateMoneyAndFlowerByUserid(userid, 0, -number/moneytoflower);
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

	@Override
	public BaseResp<Object> selectListByUserid(long userid, int pageNo, int pageSize) {
		BaseResp<Object> reseResp = new BaseResp<>();
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
			expandData.put("moneytocoin", AppserviceConfig.moneytocoin);
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
	 * param userid 
	 * param number 鲜花数量 
	 * @desc 根据龙币兑换花，进步币比例(AppserviceConfig)，查询出number数量
	 */
	@Override
	public BaseResp<Object> moneyExchangeFlower(long userid, int number) {
		BaseResp<Object> reseResp = new BaseResp<>();
		reseResp = insertPublic(userid, "0", number, 0, 0);
		reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		return reseResp;
	}
	
	

}

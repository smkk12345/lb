package com.longbei.appservice.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.UserImpCoinDetailMapper;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.dao.redis.SpringJedisDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.UserImpCoinDetail;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.service.UserImpCoinDetailService;

@Service("userImpCoinDetailService")
public class UserImpCoinDetailServiceImpl implements UserImpCoinDetailService {
	
	@Autowired
	private UserImpCoinDetailMapper userImpCoinDetailMapper;
	@Autowired
	private UserMongoDao userMongoDao;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private SpringJedisDao springJedisDao;
	
	private static Logger logger = LoggerFactory.getLogger(UserImpCoinDetailServiceImpl.class);
	
	
	
	/**
	 * @author yinxc
	 * 进步币公用添加明细方法
	 * 2017年2月25日
	 * param userid 
	 * param origin： 来源   0:签到   1:发进步  2:分享  3：邀请好友  4：榜获奖  5：收到钻石礼物 
	 * 					6：收到鲜花礼物  7:兑换商品  8：公益抽奖获得进步币  
	 * 					9：公益抽奖消耗进步币  
	 * 
	 * param number 数量 --- 消耗：(7:兑换商品    9：公益抽奖消耗进步币)value值为负---方法里面已做判断
	 * param impid 业务id  类型：     
	 * 						发进步：进步id，  榜获奖：榜id，   收到钻石礼物和收到鲜花礼物：进步id
	 * 						兑换商品:商品id
	 * param friendid 
	 */
	@Override
	public BaseResp<Object> insertPublic(long userid, String origin, int number, long impid, long friendid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			UserImpCoinDetail record = new UserImpCoinDetail();
			record.setCreatetime(new Date());
			record.setUpdatetime(new Date());
			record.setFriendid(friendid);
			record.setImpid(impid);
			record.setOrigin(origin);
			record.setUserid(userid);
			if("7".equals(origin) || "9".equals(origin)){
				record.setNumber(0 - number);
			}else{
				record.setNumber(number);
			}
			boolean temp = insert(record);
			if (temp) {
				//修改用户userInfo表---进步币总数
//				UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userid);
//				int allnum = userInfo.getTotalcoin() + record.getNumber();
//				userInfo.setTotalcoin(allnum);
//				userInfoMapper.updateByUseridSelective(userInfo);
				//先从redis里面取，如果存在，数据累加，减，之后修改表数据，   不存在存入redis
				boolean result = springJedisDao.hasKey(Constant.RP_USER_IMP_COIN_VALUE + userid);
				if(result){
					//存在
					jedisKey(userid, record.getNumber());
				}else{
					//不存在   数据库查找   存入redis
					UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userid);
					//并发时，判断是否已经存在
					boolean res = springJedisDao.hasKey(Constant.RP_USER_IMP_COIN_VALUE + userid);
					if(!res){
						//不存在
						springJedisDao.set(Constant.RP_USER_IMP_COIN_VALUE + userid, userInfo.getTotalcoin().toString(), 10);
						//递增
						springJedisDao.increment(Constant.RP_USER_IMP_COIN_VALUE + userid, record.getNumber());
						String value = springJedisDao.get(Constant.RP_USER_IMP_COIN_VALUE + userid);
						//修改数据库数据
						userInfoMapper.updateTotalcoinByUserid(userid, Integer.parseInt(value));
					}else{
						//存在
						jedisKey(userid, record.getNumber());
					}
					
				}
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insertPublic userid = {}, origin = {}, number = {}, impid = {}, friendid = {}", 
					userid, origin, number, impid, friendid, e);
		}
		return reseResp;
	}
	
	private void jedisKey(long userid, Integer number){
		//存在
		springJedisDao.increment(Constant.RP_USER_IMP_COIN_VALUE + userid, number);
		//重新设置过期时间---10秒
		String value = springJedisDao.get(Constant.RP_USER_IMP_COIN_VALUE + userid);
		springJedisDao.set(Constant.RP_USER_IMP_COIN_VALUE + userid, value, 10);
		//修改数据库数据
		userInfoMapper.updateTotalcoinByUserid(userid, Integer.parseInt(value));
	}

	/**
	 * @author yinxc
	 * 获取我的钱包
	 * 2017年2月25日
	 * param userid 
	 */
	@Override
	public BaseResp<Object> selectWallet(long userid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userid);
			reseResp.setData(userInfo);
			//商城推荐
			
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectWallet userid = {}", userid, e);
		}
		return reseResp;
	}

	public BaseResp<Object> insertSelective(UserImpCoinDetail record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = insert(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insertSelective record = {}", JSONArray.toJSON(record).toString(), e);
		}
		return reseResp;
	}
	
	private boolean insert(UserImpCoinDetail record){
		int temp = userImpCoinDetailMapper.insertSelective(record);
		return temp > 0 ? true : false;
	}
	
	@Override
	public UserImpCoinDetail selectByPrimaryKey(Integer id) {
		UserImpCoinDetail userImpCoinDetail = userImpCoinDetailMapper.selectByPrimaryKey(id);
		return userImpCoinDetail;
	}

	@Override
	public BaseResp<Object> updateByPrimaryKeySelective(UserImpCoinDetail record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = update(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateByPrimaryKeySelective record = {}", JSONArray.toJSON(record).toString(), e);
		}
		return reseResp;
	}
	
	private boolean update(UserImpCoinDetail record){
		int temp = userImpCoinDetailMapper.updateByPrimaryKeySelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<Object> selectListByUserid(long userid, int pageNo, int pageSize) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<UserImpCoinDetail> list = userImpCoinDetailMapper.selectListByUserid(userid, pageNo, pageSize);
			if(null != list && list.size()>0){
				for (UserImpCoinDetail userImpCoinDetail : list) {
					//初始化用户信息
					initMsgUserInfoByFriendid(userImpCoinDetail);
					initMsgUserInfoByUserid(userImpCoinDetail);
				}
			}
			reseResp.setData(list);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectListByUserid userid = {}, pageNo = {}, pageSize = {}", userid, pageNo, pageSize, e);
		}
		return reseResp;
	}

	@Override
	public BaseResp<Object> deleteByPrimaryKey(Integer id) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = delete(id);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("deleteByPrimaryKey id = {}", id, e);
		}
		return reseResp;
	}
	
	private boolean delete(Integer id){
		int temp = userImpCoinDetailMapper.deleteByPrimaryKey(id);
		return temp > 0 ? true : false;
	}
	
	//------------------------公用方法，初始化用户进步币信息------------------------------------------
	/**
     * 初始化用户进步币信息 ------Friendid
     */
    private void initMsgUserInfoByFriendid(UserImpCoinDetail userImpCoinDetail){
    	if(!StringUtils.hasBlankParams(userImpCoinDetail.getFriendid().toString())){
    		AppUserMongoEntity appUserMongoEntity = userMongoDao.findById(String.valueOf(userImpCoinDetail.getFriendid()));
            userImpCoinDetail.setAppUserMongoEntityFriendid(appUserMongoEntity);
		}
    }

    /**
     * 初始化用户进步币信息 ------Userid
     */
    private void initMsgUserInfoByUserid(UserImpCoinDetail userImpCoinDetail){
        AppUserMongoEntity appUserMongoEntity = userMongoDao.findById(String.valueOf(userImpCoinDetail.getUserid()));
        userImpCoinDetail.setAppUserMongoEntityUserid(appUserMongoEntity);
    }

}

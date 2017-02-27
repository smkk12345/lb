package com.longbei.appservice.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.dao.UserMoneyDetailMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.entity.UserMoneyDetail;
import com.longbei.appservice.service.UserMoneyDetailService;

@Service("userMoneyDetailService")
public class UserMoneyDetailServiceImpl implements UserMoneyDetailService {
	
	@Autowired
	private UserMoneyDetailMapper userMoneyDetailMapper;
	@Autowired
	private UserMongoDao userMongoDao;
	@Autowired
	private UserInfoMapper userInfoMapper;
	
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
	 * 					3：设榜单    4：赞助榜单    5：赞助教室     
	 * 
	 * param number 数量 --- 消耗：(除充值  购买外)value值为负---方法里面已做判断
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
			if(!"0".equals(origin)){
				userMoneyDetail.setNumber(0 - number);
			}else{
				userMoneyDetail.setNumber(number);
			}
			boolean temp = insert(userMoneyDetail);
			if (temp) {
				//修改用户userInfo表---龙币总数
				UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userid);
				int allnum = userInfo.getTotalmoney() + userMoneyDetail.getNumber();
				userInfo.setTotalmoney(allnum);
				userInfoMapper.updateByUseridSelective(userInfo);
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insertPublic userid = {}, origin = {}, number = {}, friendid = {}, msg = {}", 
					userid, origin, number, friendid, e);
		}
		return reseResp;
	}
	
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
			if(null != list && list.size()>0){
				for (UserMoneyDetail userMoneyDetail : list) {
					//初始化用户信息
					initMsgUserInfoByFriendid(userMoneyDetail);
				}
			}
			reseResp.setData(list);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectListByUserid userid = {}, pageNo = {}, pageSize = {}, msg = {}", userid, pageNo, pageSize, e);
		}
		return reseResp;
	}
	
	//------------------------公用方法，初始化用户龙币信息------------------------------------------
	/**
     * 初始化用户龙币信息 ------Friendid
     */
    private void initMsgUserInfoByFriendid(UserMoneyDetail userMoneyDetail){
    	if(!StringUtils.hasBlankParams(userMoneyDetail.getFriendid().toString())){
    		AppUserMongoEntity appUserMongoEntity = userMongoDao.findById(String.valueOf(userMoneyDetail.getFriendid()));
    		userMoneyDetail.setAppUserMongoEntityFriendid(appUserMongoEntity);
		}
    }

    /**
     * 初始化用户龙币信息 ------Userid
     */
//    private void initMsgUserInfoByUserid(UserImpCoinDetail userImpCoinDetail){
//        AppUserMongoEntity appUserMongoEntity = userMongoDao.findById(String.valueOf(userImpCoinDetail.getUserid()));
//        userImpCoinDetail.setAppUserMongoEntityUserid(appUserMongoEntity);
//    }

}

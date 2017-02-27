/**   
* @Title: UserCheckinDetailImpl.java 
* @Package com.longbei.appservice.service.impl 
* @author A18ccms A18ccms_gmail_com   
* @date 2017年1月22日 上午9:58:55 
* @version V1.0   
*/
package com.longbei.appservice.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Cache.SysRulesCache;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.dao.UserCheckinDetailMapper;
import com.longbei.appservice.dao.UserCheckinInfoMapper;
import com.longbei.appservice.dao.redis.SpringJedisDao;
import com.longbei.appservice.service.UserBehaviourService;
import com.longbei.appservice.service.UserCheckinDetailService;
import com.longbei.appservice.service.UserImpCoinDetailService;

/**
 * @author yinxc 签到 2017年2月22日 return_type UserCheckinDetailImpl
 */
@Service("userCheckinDetailService")
public class UserCheckinDetailImpl implements UserCheckinDetailService {

	@Autowired
	private UserCheckinDetailMapper userCheckinDetailMapper;
	@Autowired
	private UserCheckinInfoMapper userCheckinInfoMapper;
	@Autowired
	private UserBehaviourService userBehaviourService;
//	@Autowired
//	private UserImpCoinDetailMapper userImpCoinDetailMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private SpringJedisDao springJedisDao;
	@Autowired
	private UserImpCoinDetailService userImpCoinDetailService;

	private static Logger logger = LoggerFactory.getLogger(UserCheckinDetailImpl.class);

	public BaseResp<Object> insertSelective(UserCheckinDetail record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			Date date = new Date();
			record.setCheckindate(date);
			record.setCreatetime(date);
			String yearmonth = DateUtils.getYear() + DateUtils.getMonth();
			record.setYearmonth(Integer.parseInt(yearmonth));
			boolean temp = insert(record);
			if (temp) {
				//签到成功后，+积分---十全十美类型中的社交类型   
//				int checkin = userBehaviourService.getPointByType(record.getUserid(),"DAILY_CHECKIN");
				//十全十美类型---  2,"社交"
				String pType = SysRulesCache.perfectTenMap.get(2);
//				userBehaviourService.levelUp(record.getUserid(), checkin, pType);
				UserInfo userInfo = userInfoMapper.selectByPrimaryKey(record.getUserid());
				userBehaviourService.pointChange(userInfo,"DAILY_CHECKIN",pType);
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insertSelective record = {}, msg = {}", JSONArray.toJSON(record).toString(), e);
		}
		return reseResp;
	}
	
	@Override
	public BaseResp<Object> selectIsCheckIn(long userid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			String day = DateUtils.getDate("yyyyMMdd");
			// 判断redis中是否存在 存在+1
			boolean result = springJedisDao.hasKey(Constant.RP_USER_CHECK + userid,
					Constant.RP_USER_CHECK_VALUE + userid);
			if(result){
				//存在   判断日期是否相差一天     判断签到是否是连续
				//获取签到第一天
				String redisDate = springJedisDao.getHashValue(Constant.RP_USER_CHECK + userid, 
						Constant.RP_USER_CHECK_DATE + userid);
				//连续签到天数
				String redisvalue = springJedisDao.getHashValue(Constant.RP_USER_CHECK + userid, 
						Constant.RP_USER_CHECK_VALUE + userid);
				int cha = Integer.parseInt(day) - Integer.parseInt(redisDate);
				if(cha == Integer.parseInt(redisvalue) -1){
					//当天已签到
					reseResp.initCodeAndDesp(Constant.STATUS_SYS_30,Constant.RTNINFO_SYS_30);
					return reseResp;
				}else if(cha > Integer.parseInt(redisvalue)){
					//不是连续签到     先清除    再添加
					springJedisDao.del(Constant.RP_USER_CHECK + userid);
					
					addRedisCheck(userid, day, "1");
					
					UserCheckinDetail userCheckinDetail = new UserCheckinDetail();
					userCheckinDetail.setUserid(userid);
					reseResp = insertSelective(userCheckinDetail);
					//+进步币
					SysRuleCheckin sysRuleCheckin = SysRulesCache.sysRuleCheckinMap.get(1);
					insertUserImpCoinDetail(userid, sysRuleCheckin.getAwardmoney(), Constant.USER_IMP_COIN_CHECK);
				}else{
					UserCheckinDetail userCheckinDetail = new UserCheckinDetail();
					userCheckinDetail.setUserid(userid);
					reseResp = insertSelective(userCheckinDetail);
					
					//连续签到 +1
					springJedisDao.increment(Constant.RP_USER_CHECK + userid, 
							Constant.RP_USER_CHECK_VALUE + userid, 1);
					//查看是否已经连续签到5天以上    ---5天以上存库user_checkin_info
//						String addDate = springJedisDao.getHashValue(Constant.RP_USER_CHECK + record.getUserid(), 
//								Constant.RP_USER_CHECK_DATE + record.getUserid());
//						int res = Integer.parseInt(day) - Integer.parseInt(addDate);
					int res = Integer.parseInt(redisvalue) + 1;
					//+进步币
					SysRuleCheckin sysRuleCheckin = null;
					if(res >= 7){
						//7天以上   奖励规则一样
						sysRuleCheckin = SysRulesCache.sysRuleCheckinMap.get(7);
					}else{
						sysRuleCheckin = SysRulesCache.sysRuleCheckinMap.get(res);
					}
					insertUserImpCoinDetail(userid, sysRuleCheckin.getAwardmoney(), Constant.USER_IMP_COIN_CHECK);
					
					if(res >= 5){
						String start = redisDate.substring(0, 4) + "-" + redisDate.substring(4, 6) 
							+ "-" + redisDate.substring(6, redisDate.length());
						//需要先判断数据库里面是否已有这条记录    有：修改     无：添加
						UserCheckinInfo checkinInfo = userCheckinInfoMapper.selectByStarttimeAndUserid(userid, start);
						if(null != checkinInfo){
							userCheckinInfoMapper.updateContinuedaysByid(checkinInfo.getId(), res, new Date());
						}else{
							UserCheckinInfo userCheckinInfo = new UserCheckinInfo();
							userCheckinInfo.setContinuedays(res);
							userCheckinInfo.setCreatetime(new Date());
							userCheckinInfo.setEndtime(new Date());
							//20170222
							start = start + " 00:00:00";
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date starttime = sdf.parse(start);
							userCheckinInfo.setStarttime(starttime);
							userCheckinInfo.setUserid(userid);
							userCheckinInfoMapper.insertSelective(userCheckinInfo);
						}
					}
				}
			}else{
				//不存在    添加
				addRedisCheck(userid, day, "1");
				
				UserCheckinDetail userCheckinDetail = new UserCheckinDetail();
				userCheckinDetail.setUserid(userid);
				reseResp = insertSelective(userCheckinDetail);
				//+进步币
				SysRuleCheckin sysRuleCheckin = SysRulesCache.sysRuleCheckinMap.get(1);
				insertUserImpCoinDetail(userid, sysRuleCheckin.getAwardmoney(), Constant.USER_IMP_COIN_CHECK);
			}
			
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectIsCheckIn userid = {}, msg = {}", userid, e);
		}
		return reseResp;
	}
	
	/**
	 * @author yinxc
	 * 添加进步币origin
	 * 2017年2月23日
	 * void
	 * UserCheckinDetailImpl
	 */
	private void insertUserImpCoinDetail(long userid, int number, String origin){
//		UserImpCoinDetail detail = new UserImpCoinDetail();
//		detail.setCreatetime(new Date());
//		detail.setNumber(number);
//		detail.setOrigin(Constant.USER_IMP_COIN_CHECK);
//		detail.setUpdatetime(new Date());
//		detail.setUserid(userid);
//		userImpCoinDetailMapper.insertSelective(detail);
		userImpCoinDetailService.insertPublic(userid, origin, number, 0, 0);
	}
	
	/**
	 * @author yinxc
	 * 签到天数    添加redis
	 * 2017年2月22日
	 * return_type
	 * UserCheckinDetailImpl
	 */
	private void addRedisCheck(long userid, String day, String count){
		// 存入redis 周期：俩天
		Map<String, String> map = new HashMap<String, String>();
		// {"user_check_value_userid":"2","user_check_date_userid":"20170222"}
		map.put(Constant.RP_USER_CHECK_VALUE + userid, count);
		map.put(Constant.RP_USER_CHECK_DATE + userid, day);
		springJedisDao.putAll(Constant.RP_USER_CHECK + userid, map, 60*60*24*2);
		
	}

	private boolean insert(UserCheckinDetail record) {
		int temp = userCheckinDetailMapper.insertSelective(record);
		return temp > 0 ? true : false;
	}

	public UserCheckinDetail selectByPrimaryKey(Integer id) {
		UserCheckinDetail userCheckinDetail = userCheckinDetailMapper.selectByPrimaryKey(id);
		return userCheckinDetail;
	}

	public BaseResp<Object> updateByPrimaryKeySelective(UserCheckinDetail record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = update(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateByPrimaryKeySelective record = {}, msg = {}", JSONArray.toJSON(record).toString(), e);
		}
		return reseResp;
	}

	private boolean update(UserCheckinDetail record) {
		int temp = userCheckinDetailMapper.updateByPrimaryKeySelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<Object> selectDetailList(long userid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<UserCheckinDetail> list = userCheckinDetailMapper.selectDetailList(userid);
			if (null != list && list.size() > 0) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			} else {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_29, Constant.RTNINFO_SYS_29);
			}
			reseResp.setData(list);
		} catch (Exception e) {
			logger.error("selectDetailList userid = {}, msg = {}", userid, e);
		}
		return reseResp;
	}

	@Override
	public BaseResp<Object> selectDetailListByYearmonth(long userid, Integer yearmonth) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			int continuousday = 0;
			List<UserCheckinDetail> list = userCheckinDetailMapper.selectDetailListByYearmonth(userid, yearmonth);
			if (null != list && list.size() > 0) {
				//获取redis中的连续签到天数
				// 判断redis中是否存在 
				boolean result = springJedisDao.hasKey(Constant.RP_USER_CHECK + userid,
						Constant.RP_USER_CHECK_VALUE + userid);
				if(result){
					//连续签到天数
					String redisvalue = springJedisDao.getHashValue(Constant.RP_USER_CHECK + userid, 
							Constant.RP_USER_CHECK_VALUE + userid);
					continuousday = Integer.parseInt(redisvalue);
				}
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			} else {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_29, Constant.RTNINFO_SYS_29);
			}
			reseResp.getExpandData().put("continuousday", continuousday);
			//共签到多少天
			int count = userCheckinDetailMapper.selectCountByUserid(userid);
			reseResp.getExpandData().put("allday", count);
			reseResp.getExpandData().put("sysRuleCheckin", SysRulesCache.sysRuleCheckinMap);
			reseResp.setData(list);
		} catch (Exception e) {
			logger.error("selectDetailListByYearmonth userid = {}, yearmonth = {}, msg = {}", userid, yearmonth, e);
		}
		return reseResp;
	}

}

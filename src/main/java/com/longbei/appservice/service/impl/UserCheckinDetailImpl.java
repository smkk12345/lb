/**   
* @Title: UserCheckinDetailImpl.java 
* @Package com.longbei.appservice.service.impl 
* @author A18ccms A18ccms_gmail_com   
* @date 2017年1月22日 上午9:58:55 
* @version V1.0   
*/
package com.longbei.appservice.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.longbei.appservice.common.constant.Constant_Imp_Icon;
import com.longbei.appservice.common.constant.Constant_Perfect;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.entity.*;

import org.apache.commons.collections.map.HashedMap;
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
import com.longbei.appservice.dao.UserImproveStatisticMapper;
import com.longbei.appservice.dao.redis.SpringJedisDao;
import com.longbei.appservice.service.UserBehaviourService;
import com.longbei.appservice.service.UserCheckinDetailService;

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
	private UserImproveStatisticMapper userImproveStatisticMapper;
//	@Autowired
//	private UserImpCoinDetailService userImpCoinDetailService;

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
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insertSelective record = {}", JSONArray.toJSON(record).toString(), e);
		}
		return reseResp;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> selectIsCheckIn(long userid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			//先判断用户是否已签到
			String date = DateUtils.getDate("yyyy-MM-dd");
			UserCheckinDetail userCheckinDetail = userCheckinDetailMapper.selectDetail(userid, date);
			if(null != userCheckinDetail){
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_30);
			}
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
				//获取上一次签到的日期
				String checkinday = springJedisDao.getHashValue(Constant.RP_USER_CHECK + userid, 
						Constant.RP_USER_CHECK_DAY_DATE + userid);
				//判断上一次签到的日期
				int cha = DateUtils.daysBetween(checkinday, DateUtils.formatDateTime1(new Date()));

				if(cha > 1 || cha == 0){
					//不是连续签到     先清除    再添加
					springJedisDao.del(Constant.RP_USER_CHECK + userid);
					addRedisCheck(userid, date, "1");
					operate(userid);
				}else{
					//连续签到 +1
					springJedisDao.increment(Constant.RP_USER_CHECK + userid,
							Constant.RP_USER_CHECK_VALUE + userid, 1);
					operate(userid);
					//查看是否已经连续签到5天以上    ---5天以上存库user_checkin_info
					int res = Integer.parseInt(redisvalue) + 1;
					//重新设置过期时间
//					springJedisDao.expire(Constant.RP_USER_CHECK + userid, 60*60*24*2);
					addRedisCheck(userid, redisDate, res+"");
					//+进步币
					if(res >= 5){
						//需要先判断数据库里面是否已有这条记录    有：修改     无：添加
						UserCheckinInfo checkinInfo = userCheckinInfoMapper.selectByStarttimeAndUserid(userid, redisDate);
						if(null != checkinInfo){
							userCheckinInfoMapper.updateContinuedaysByid(checkinInfo.getId(), res, new Date());
						}else{
							UserCheckinInfo userCheckinInfo = new UserCheckinInfo();
							userCheckinInfo.setContinuedays(res);
							userCheckinInfo.setCreatetime(new Date());
							userCheckinInfo.setEndtime(new Date());
							//20170222
							redisDate = redisDate + " 00:00:00";
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date starttime = sdf.parse(redisDate);
							userCheckinInfo.setStarttime(starttime);
							userCheckinInfo.setUserid(userid);
							userCheckinInfoMapper.insertSelective(userCheckinInfo);
						}
					}
				}
			}else{
				//不存在    添加
				addRedisCheck(userid, date, "1");
				operate(userid);
			}
			//获得当天签到得到的进步币数量
 			String checkvalue = springJedisDao.get(Constant.RP_USER_NEWDATE_CHECK + userid);
			reseResp.getExpandData().put("moneycount", checkvalue);
			reseResp.getExpandData().put("moneydate", date);
// 			reseResp.setData(checkvalue);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectIsCheckIn userid = {}", userid, e);
		}
		return reseResp;
	}

	//
	private BaseResp<Object> operate(long userid){
		BaseResp<Object> reseResp = new BaseResp<Object>();
		UserCheckinDetail userCheckinDetail = new UserCheckinDetail();
		userCheckinDetail.setUserid(userid);
		reseResp = insertSelective(userCheckinDetail);
		//+进步币
		if(ResultUtil.isSuccess(reseResp)){
			UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userid);
			reseResp = userBehaviourService.pointChange(userInfo,"DAILY_CHECKIN",Constant_Perfect.PERFECT_GAM,
					Constant.USER_IMP_COIN_CHECKIN,0,0);

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
//	private void insertUserImpCoinDetail(long userid, int number, String origin){
////		UserImpCoinDetail detail = new UserImpCoinDetail();
////		detail.setCreatetime(new Date());
////		detail.setNumber(number);
////		detail.setOrigin(Constant.USER_IMP_COIN_CHECK);
////		detail.setUpdatetime(new Date());
////		detail.setUserid(userid);
////		userImpCoinDetailMapper.insertSelective(detail);
//		userImpCoinDetailService.insertPublic(userid, origin, number, 0, null);
//	}
	
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
		map.put(Constant.RP_USER_CHECK_DAY_DATE + userid, DateUtils.formatDateTime1(new Date()));
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
			logger.error("updateByPrimaryKeySelective record = {}", JSONArray.toJSON(record).toString(), e);
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
			logger.error("selectDetailList userid = {}", userid, e);
		}
		return reseResp;
	}
	
	/**
	 * @author yinxc
	 * 获取签到规则
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> sysRuleCheckin() {
		BaseResp<Object> baseResp = new BaseResp<>();
		Map<String,Object> map = new HashedMap();
		List<String> list = getPointInfoPerDay();
		map.put("ruleCheckin", list);
		baseResp.setData(map);
		return baseResp.initCodeAndDesp();
	}

	private List<String> getPointInfoPerDay(){
		List<String> list = new ArrayList<>();
		Map<Integer,Integer> checkInImpIconMap = Constant_Imp_Icon.checkInImpIconMap;
		logger.info("getPointInfoPerDay sysRuleCheckinMap = {}", checkInImpIconMap);
		Iterator<Integer> iterator = checkInImpIconMap.keySet().iterator();
		while (iterator.hasNext()){
			Integer subKey = iterator.next();
			Integer value = checkInImpIconMap.get(subKey);
//			int value = sysRuleCheckin.getAwardmoney();
			String disStr = "";
			switch (subKey){
				case 1:
					disStr = "签到,第"+subKey+"天获得"+value+"个进步币";
					break;
				default:
					disStr = "连续签到,第"+subKey+"天获得"+value+"个进步币";
					break;
			}
			list.add(disStr);
		}
		list.add("连续签到第"+checkInImpIconMap.size()+"天开始,每天都能获得"+SysRulesCache.behaviorRule.getSignimpcoinsmax()+
				"个进步币,一旦中断就要从"+checkInImpIconMap.get(1)+"进步币开始咯~");
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> selectDetailListByYearmonth(long userid, String yearmonth) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			int continuousday = 0;
			List<UserCheckinDetail> list = userCheckinDetailMapper.selectDetailListByYearmonth(userid, yearmonth);

			if (null != list && list.size() > 0) {
				//获取签到是否已发进步状态  isimprove 0:没有发进步   1：已发进步
				String isimprove = "0";
				for (UserCheckinDetail userCheckinDetail : list) {
					@SuppressWarnings("deprecation")
					String day = userCheckinDetail.getCreatetime().getDate() + "";
					userCheckinDetail.setDay(day);;
					String currentday = DateUtils.formatDate(userCheckinDetail.getCheckindate(), "yyyy-MM-dd");
					UserImproveStatistic userImproveStatistic = userImproveStatisticMapper.selectByUseridAndCurrentday(userid, 
							currentday);
					if(null != userImproveStatistic){
						isimprove = "1";
					}else{
						isimprove = "0";
					}
					userCheckinDetail.setIsimprove(isimprove);
				}
				
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
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_29);
			}
			reseResp.getExpandData().put("continuousday", continuousday);
			//共签到多少天
			int count = userCheckinDetailMapper.selectCountByUserid(userid);
			reseResp.getExpandData().put("allday", count);
			
			Map<String,Object> map = new HashedMap();
			List<String> checkinList = getPointInfoPerDay();
			map.put("ruleCheckin", checkinList);
			map.put("userCheckinDetailList", list);
			reseResp.setData(map);
		} catch (Exception e) {
			logger.error("selectDetailListByYearmonth userid = {}, yearmonth = {}", userid, yearmonth, e);
		}
		return reseResp;
	}

}

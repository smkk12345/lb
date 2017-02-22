package com.longbei.appservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.UserSettingCommonMapper;
import com.longbei.appservice.entity.UserSettingCommon;
import com.longbei.appservice.service.UserSettingCommonService;

@Service("userSettingCommonService")
public class UserSettingCommonServiceImpl implements UserSettingCommonService {
	
	@Autowired
	private UserSettingCommonMapper userSettingCommonMapper;
	
	private static Logger logger = LoggerFactory.getLogger(UserSettingCommonServiceImpl.class);

	@Override
	public BaseResp<Object> insert(UserSettingCommon record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = insertUserSettingCommon(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insert record={},msg={}",record,e);
		}
		return reseResp;
	}
	
	private boolean insertUserSettingCommon(UserSettingCommon record){
		int temp = userSettingCommonMapper.insertSelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public UserSettingCommon selectByid(Integer id) {
		UserSettingCommon userSettingCommon = new UserSettingCommon();
		try {
			userSettingCommon = userSettingCommonMapper.selectByPrimaryKey(id);
		} catch (Exception e) {
			logger.error("selectByid id={},msg={}",id,e);
		}
		return userSettingCommon;
	}

	@Override
	public BaseResp<Object> updateByid(UserSettingCommon record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = updateUserSettingCommon(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateByid record={},msg={}",record,e);
		}
		return reseResp;
	}
	
	private boolean updateUserSettingCommon(UserSettingCommon record){
		int temp = userSettingCommonMapper.updateByPrimaryKeySelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<Object> selectByKey(String userid, String key) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			UserSettingCommon userSettingCommon = userSettingCommonMapper.selectByKey(userid, key);
			reseResp.setData(userSettingCommon);
			if (userSettingCommon != null) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("selectByKey userid={},key={},msg={}",userid,key,e);
		}
		return reseResp;
	}

	@Override
	public BaseResp<Object> selectByUserid(String userid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<UserSettingCommon> list = userSettingCommonMapper.selectByUserid(userid);
			if(null != list && list.size()>0){
				Map<String, Object> expandData = new HashMap<>();
				for (UserSettingCommon userSettingCommon : list) {
					expandData.put(userSettingCommon.getUkey(), userSettingCommon.getUvalue());
				}
				reseResp.setExpandData(expandData);
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("selectByUserid userid={},msg={}",userid,e);
		}
		return reseResp;
	}
	
	@Override
	public Map<String, Object> selectMapByUserid(String userid) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<UserSettingCommon> list = userSettingCommonMapper.selectByUserid(userid);
			if(null != list && list.size()>0){
				for (UserSettingCommon userSettingCommon : list) {
					map.put(userSettingCommon.getUkey(), userSettingCommon.getUvalue());
				}
			}
		} catch (Exception e) {
			logger.error("selectByUserid userid={},msg={}",userid,e);
		}
		return map;
	}

	@Override
	public BaseResp<Object> updateByUseridKey(String userid, String key, String value) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			int temp = userSettingCommonMapper.updateByUseridKey(userid, key, value);
			if (temp>0) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateByUseridKey userid={},key={},value={},msg={}", userid, key, value, e);
		}
		return reseResp;
	}

	@Override
	public BaseResp<Object> updateByUseridMap(String userid, String value) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			userSettingCommonMapper.updateByUseridKey(userid, "is_new_fans", value);
			userSettingCommonMapper.updateByUseridKey(userid, "is_like", value);
			userSettingCommonMapper.updateByUseridKey(userid, "is_flower", value);
			userSettingCommonMapper.updateByUseridKey(userid, "is_diamond", value);
			userSettingCommonMapper.updateByUseridKey(userid, "is_comment", value);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("updateByUseridMap userid = {}, msg = {}", userid, e);
		}
		return reseResp;
	}
	
	//map转换成lisy
//	@SuppressWarnings("rawtypes")
//	public static List<UserSettingCommon>  mapTransitionList(Map<String, String> map) {
//		List<UserSettingCommon> list = new ArrayList<UserSettingCommon>();
//		Iterator iter = map.entrySet().iterator();  //获得map的Iterator
//		while(iter.hasNext()) {
//			Entry entry = (Entry)iter.next();
//			UserSettingCommon common = new UserSettingCommon();
//			
//		}
//		return list;
//	}

}

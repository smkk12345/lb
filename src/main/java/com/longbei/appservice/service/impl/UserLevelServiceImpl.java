package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.syscache.SysRulesCache;
import com.longbei.appservice.common.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.dao.UserLevelMapper;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.entity.UserLevel;
import com.longbei.appservice.service.UserLevelService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userLevelService")
public class UserLevelServiceImpl implements UserLevelService {
	
	@Autowired
	private UserLevelMapper userLevelMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;
	
	private static Logger logger = LoggerFactory.getLogger(UserLevelServiceImpl.class);

	@Override
	public BaseResp<UserLevel> selectByUserid(long userid) {
		BaseResp<UserLevel> baseResp = new BaseResp<UserLevel>();
		try {
			UserLevel userLevel = null;
			UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
			if(null != userInfo){
				userLevel = userLevelMapper.selectByGrade(userInfo.getGrade());
				baseResp.setData(userLevel);
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("selectByUserid userid = {}", userid, e);
		}
		return baseResp;
	}


	@Override
	public Page<UserLevel> selectUserLevelList(Integer startNum, Integer pageSize) {
		Integer pageNo = startNum/pageSize+1;
		Page<UserLevel> page = new Page<>(pageNo,pageSize);
		try {
			int totalcount = userLevelMapper.selectUserLevelListCount();
			pageNo = Page.setPageNo(pageNo,totalcount,pageSize);
			List<UserLevel> userLevelList = userLevelMapper.selectAll(startNum,pageSize);
			page.setTotalCount(totalcount);
			page.setList(userLevelList);
		} catch (Exception e) {
			logger.error("selectUserLevelList for adminservice error and msg={}",e);
		}
		return page;
	}

	@Override
	public BaseResp insertBatchLevelRules(List<UserLevel> levelList) {
		try {
			int res = userLevelMapper.insertBatchLevelRules(levelList);
			if (res > 0) {
				//更新等级特权缓存
				this.updateSysRuleLevelPointCache();
                return BaseResp.ok();
            }
		} catch (Exception e) {
			logger.error("insertBatchLevelRules is error:", e);
		}
		return BaseResp.fail();
	}

	/**
	 * 更新等级特权缓存
	 */
	private void updateSysRuleLevelPointCache(){
		Map<Integer,UserLevel> map = new HashMap<>();
		List<UserLevel> list = userLevelMapper.selectAll(null,null);
		for (int i = 0; i < list.size(); i++) {
			UserLevel userLevel = list.get(i);
			map.put(userLevel.getGrade(),userLevel);
		}
		SysRulesCache.levelPointMap = map;
	}

	@Override
	public BaseResp deleteLevelRules() {
		try {
			int res = userLevelMapper.deleteLevelRules();
			if (res > 0) {
				return BaseResp.ok();
			}
		} catch (Exception e) {
			logger.error("deleteLevelRules is error:", e);
		}
		return BaseResp.fail();
	}
}

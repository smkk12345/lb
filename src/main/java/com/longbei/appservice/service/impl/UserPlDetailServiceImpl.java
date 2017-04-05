package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.Cache.SysRulesCache;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.UserPlDetailMapper;
import com.longbei.appservice.dao.SysPerfectInfoMapper;
import com.longbei.appservice.entity.UserPlDetail;
import com.longbei.appservice.entity.SysPerfectInfo;
import com.longbei.appservice.service.UserPlDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

@Service("userPlDetailService")
public class UserPlDetailServiceImpl implements UserPlDetailService {

	@Autowired
	private UserPlDetailMapper userPlDetailMapper;
	@Autowired
	private SysPerfectInfoMapper sysPerfectInfoMapper;
	
	private static Logger logger = LoggerFactory.getLogger(UserPlDetailServiceImpl.class);
	

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> selectNowscoreAndDiffById(int id) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			UserPlDetail userPlDetail = userPlDetailMapper.selectUserPlDetailById(id);
			int score = 0;
			int level = 0;
			if(null != userPlDetail){
				if(null != userPlDetail.getScorce()){
					score = userPlDetail.getScorce().intValue();
				}
				if(null != userPlDetail.getLeve()){
					level = userPlDetail.getLeve().intValue();
				}
			}
//			int score = userPlDetail.getScorce();
//			int level = userPlDetail.getLeve();
			int levelscore = 0;
			for (int i = 0; i <level-1 ; i++) {
				//sysRule.getPtype()+"&"+sysRule.getPlevel()
				if(null != SysRulesCache.pLevelPointMap.get(userPlDetail.getPtype()+"&"+(i+1))){
					levelscore = levelscore+ SysRulesCache.pLevelPointMap.get(userPlDetail.getPtype()+"&"+(i+1));
				}
			}
            int nowscore = levelscore + score;
			int levelscore1 = 0;
			if(null != SysRulesCache.pLevelPointMap.get(userPlDetail.getPtype()+"&"+(level))){
				 levelscore1 = SysRulesCache.pLevelPointMap.get(userPlDetail.getPtype()+"&"+(level));
			}
			int diff = levelscore1 - score;
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			baseResp.getExpandData().put("diff",diff);
			baseResp.getExpandData().put("nowscore",nowscore);
		} catch (Exception e) {
			logger.error("selectNowscoreAndDiffById error and id={}",id,e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> selectUserPerfectListByUserId(long userid,int startNum,int pageSize) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			List<UserPlDetail> list = userPlDetailMapper.selectUserPerfectListByUserId(userid,startNum,pageSize);
			baseResp.initCodeAndDesp();
			String photos;
			for (int i = 0; i <list.size() ; i++) {
				UserPlDetail userPlDetail = list.get(i);
				userPlDetail.setPerfectname(SysRulesCache.perfectTenMap.get(Integer.parseInt(userPlDetail.getPtype())));
				String ptype = userPlDetail.getPtype();
				SysPerfectInfo sysPerfectInfo = sysPerfectInfoMapper.selectPerfectPhotoByPtype(ptype);
				if (null != sysPerfectInfo) {
					userPlDetail.setPhoto(sysPerfectInfo.getPhotos());
				}
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
				baseResp.setData(list);
			}
		} catch (Exception e) {
			logger.error("selectUserPerfectListByUserId and userid={},startNum={},pageSize={}",userid,startNum,pageSize,e);
		}
		return baseResp;
	}
	
}

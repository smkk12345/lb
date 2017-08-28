package com.longbei.appservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.dao.LiveInfoMongoMapper;
import com.longbei.appservice.entity.LiveInfo;
import com.longbei.appservice.service.LiveInfoMongoService;

import net.sf.json.JSONObject;

@Service
public class LiveInfoMongoServiceImpl implements LiveInfoMongoService {

	@Autowired
	private LiveInfoMongoMapper liveInfoMongoMapper;

	private static Logger logger = LoggerFactory.getLogger(LiveInfoMongoServiceImpl.class);
	
	@Override
	public void insertLiveInfo(LiveInfo liveInfo) {
		try {
//			liveInfo.setLiveid(idGenerateService.getUniqueIdAsLong());
			liveInfoMongoMapper.insertLiveInfo(liveInfo);
		} catch (Exception e) {
			logger.error("insertLiveInfo liveInfo = {}", JSONObject.fromObject(liveInfo).toString(), e);
		}
		
	}

	@Override
	public LiveInfo selectLiveInfoByLiveid(long liveid) {
		LiveInfo liveInfo = liveInfoMongoMapper.selectLiveInfoByLiveid(liveid);
		return liveInfo;
	}

}

package com.longbei.appservice.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.utils.DateUtils;
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

	@Override
	public void updateLiveInfo(long classroomid, Long courseid, Date starttime, Date endtime) {
		try {
			liveInfoMongoMapper.updateLiveInfo(classroomid, courseid, starttime, endtime);
		} catch (Exception e) {
			logger.error("updateLiveInfo classroomid = {}, courseid = {}, starttime = {}, endtime = {}", 
					classroomid, courseid, DateUtils.formatDateTime1(starttime), DateUtils.formatDateTime1(endtime), e);
		}
	}

	@Override
	public void deleteLiveInfo(long classroomid, Long courseid) {
		try {
			liveInfoMongoMapper.deleteLiveInfo(classroomid, courseid);
		} catch (Exception e) {
			logger.error("deleteLiveInfo classroomid = {}, courseid = {}", 
					classroomid, courseid, e);
		}
	}

	/**
	 * @author yinxc
	 * 获取所有的直播信息
	 * @param isdel 是否删除    0:未删除   1：删除
	 */
	@Override
	public List<LiveInfo> selectLiveInfoList(String isdel) {
		List<LiveInfo> list = null;
		try {
			list = liveInfoMongoMapper.selectLiveInfoList(isdel);
		} catch (Exception e) {
			logger.error("selectLiveInfoList isdel = {}", isdel, e);
		}
		return list;
	}

}

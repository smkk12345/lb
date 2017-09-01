package com.longbei.appservice.service;

import java.util.Date;
import java.util.List;

import com.longbei.appservice.entity.LiveInfo;

public interface LiveInfoMongoService {

	/**
	 * @author yinxc
	 * 添加
	 */
	void insertLiveInfo(LiveInfo liveInfo);
	
	/**
	 * @author yinxc
	 * 根据liveid查询
	 */
	LiveInfo selectLiveInfoByLiveid(long liveid);
	
	
	/**
	 * @author yinxc
	 * 获取所有的直播信息
	 * @param isdel 是否删除    0:未删除   1：删除
	 */
	List<LiveInfo> selectLiveInfoList(String isdel);
	
	/**
	 * @author yinxc
	 * 修改
	 */
	void updateLiveInfo(long classroomid, Long courseid, Date starttime, Date endtime);
	
	
	/**
	 * @author yinxc
	 * 删除
	 */
	void deleteLiveInfo(long classroomid, Long courseid);
	
}

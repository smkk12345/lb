package com.longbei.appservice.service;

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
	
}

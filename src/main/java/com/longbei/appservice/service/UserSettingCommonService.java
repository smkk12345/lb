package com.longbei.appservice.service;

import java.util.Map;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserSettingCommon;

public interface UserSettingCommonService {

	/**
	 * @author yinxc
	 * 添加设置信息
	 * 2017年1月19日
	 * return_type
	 */
	BaseResp<Object> insert(UserSettingCommon record);
	
	/**
	 * @author yinxc
	 * 查看设置信息
	 * 2017年1月19日
	 * return_type
	 */
	UserSettingCommon selectByid(Integer id);
	
	/**
	 * @author yinxc
	 * 修改设置信息
	 * 2017年1月19日
	 * return_type
	 */
	BaseResp<Object> updateByid(UserSettingCommon record);
	
	/**
	 * @author yinxc
	 * 根据userid,key获取设置信息
	 * 2017年1月19日
	 * return_type
	 */
	BaseResp<Object> selectByKey(String userid, String key);
	
	/**
	 * @author yinxc
	 * 根据userid获取设置信息列表
	 * 2017年1月19日
	 * return_type
	 */
	BaseResp<Object> selectByUserid(String userid);
	
	/**
	 * @author yinxc
	 * 根据userid获取设置信息列表Map
	 * 2017年1月19日
	 * return_type
	 */
	Map<String, String> selectMapByUserid(String userid);
	
	/**
	 * @author yinxc
	 * 根据userid,key修改设置信息
	 * 2017年1月19日
	 * return_type
	 */
	BaseResp<Object> updateByUseridKey(String userid, String key, String value);
	
	/**
	 * @author yinxc
	 * 根据userid,key修改设置Map信息
	 * 2017年1月19日
	 * return_type
	 */
	BaseResp<Object> updateByUseridMap(String userid, String value);

}

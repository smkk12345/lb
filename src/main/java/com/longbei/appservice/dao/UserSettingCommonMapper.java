package com.longbei.appservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.UserSettingCommon;

public interface UserSettingCommonMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserSettingCommon record);

    int insertSelective(UserSettingCommon record);
    
    int insertList(List<UserSettingCommon> commons);

    UserSettingCommon selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserSettingCommon record);

    int updateByPrimaryKey(UserSettingCommon record);
    
    /**
	 * @author yinxc
	 * 根据userid,key获取设置信息
	 * 2017年1月19日
	 * return_type
	 */
    UserSettingCommon selectByKey(@Param("userid") String userid, @Param("ukey") String ukey);
    
    /**
	 * @author yinxc
	 * 根据userid获取设置信息列表
	 * 2017年1月19日
	 * return_type
	 */
    List<UserSettingCommon> selectByUserid(@Param("userid") String userid);
    
    /**
	 * @author yinxc
	 * 根据userid,key修改设置信息
	 * 2017年1月19日
	 * return_type
	 */
    int updateByUseridKey(@Param("userid") String userid, @Param("ukey") String ukey, @Param("uvalue") String uvalue);
    
}
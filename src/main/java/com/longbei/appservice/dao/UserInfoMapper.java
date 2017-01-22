package com.longbei.appservice.dao;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.UserInfo;

public interface UserInfoMapper {
    int deleteByPrimaryKey(long id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(@Param("userid")long userid);
    /**
    * @Title: getByUserName
    * @Description: 通过手机号获取用户基本信息
    * @param @param username
    * @param @return
    * @auther smkk
    * @currentdate:2017年1月17日
     */
    UserInfo getByUserName(String username);

    int updateByUseridSelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);
    
    UserInfo getByNickName(String nickname);
}
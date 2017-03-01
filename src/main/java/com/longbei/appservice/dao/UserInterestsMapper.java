package com.longbei.appservice.dao;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.UserInterests;

public interface UserInterestsMapper {

    int updateInterests(UserInterests record);
    int insertInterests(UserInterests record);
    int deleteInterests(@Param("id") int id,@Param("userid")String userid);
}
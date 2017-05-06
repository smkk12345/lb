package com.longbei.appservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.UserInterests;

public interface UserInterestsMapper {

    int updateInterests(UserInterests record);
    int insert(UserInterests record);
    List<UserInterests> selectInterests(@Param("userid") long userid);
    
}
package com.longbei.appservice.dao;

import java.util.List;

import com.longbei.appservice.entity.SnsFriends;

import feign.Param;

public interface SnsFriendsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SnsFriends record);

    int insertSelective(SnsFriends record);

    SnsFriends selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SnsFriends record);

    int updateByPrimaryKey(SnsFriends record);
    
    List<SnsFriends> selectListByUsrid(@Param("userid")long userid,
    		@Param("startNum")int startNum,@Param("endNum")long endNum);
    
    int  deleteByUidAndFid(@Param("userid")long userid,@Param("friendid")long friendid);
    
    
    
}
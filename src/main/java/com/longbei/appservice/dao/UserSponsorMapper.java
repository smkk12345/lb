package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserSponsor;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserSponsorMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSponsor(UserSponsor record);

    int insertSelective(UserSponsor record);

    UserSponsor selectByPrimaryKey(Integer id);

    int updateSponsor(UserSponsor record);

    int updateByPrimaryKey(UserSponsor record);

    List<UserSponsor> selectSponsorList(@Param("bid") long bid,@Param("startNum") int startNum,@Param("pageSize") int pageSize);

    UserSponsor selectByUseridAndBid(@Param("userid")long userid,@Param("bid")long bid);

}
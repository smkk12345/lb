package com.longbei.appservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.UserCard;

public interface UserCardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCard record);

    int insertSelective(UserCard record);

    UserCard selectByCardid(@Param("cardid") long cardid);
    
    List<UserCard> selectList(@Param("startNum") int startNum, @Param("endNum") int endNum);
    
    List<String> selectUseridByCardid(@Param("cardid") long cardid);

    int updateByPrimaryKeySelective(UserCard record);

    int updateByPrimaryKey(UserCard record);
    
}
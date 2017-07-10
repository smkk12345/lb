package com.longbei.appservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.UserCard;

public interface UserCardMapper {
    int deleteByUserCardId(Long userCardId);

    int insert(UserCard record);

    int insertUserCard(UserCard userCard);

    UserCard selectByCardid(@Param("cardid") long cardid);
    
    List<UserCard> selectUserCardList(@Param("userCard")UserCard userCard,@Param("startNum") Integer startNum, @Param("pageSize") Integer pageSize);

    Integer selectUserCardListCount(@Param("userCard")UserCard userCard);

    int updateByUserCardId(UserCard record);

    int updateByPrimaryKey(UserCard record);
    
}
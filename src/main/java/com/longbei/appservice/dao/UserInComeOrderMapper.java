package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserInComeOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInComeOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserInComeOrder record);

    int insertSelective(UserInComeOrder record);

    UserInComeOrder selectByPrimaryKey(Integer id);

    int selectCount(@Param("uorder") UserInComeOrder userInComeOrder);

    List<UserInComeOrder> selectList(@Param("uorder") UserInComeOrder userInComeOrder,
                                     @Param("startNo") Integer startNo,
                                     @Param("pageSize") Integer pageSize);

    int updateByPrimaryKeySelective(UserInComeOrder record);

    int updateByPrimaryKey(UserInComeOrder record);
}
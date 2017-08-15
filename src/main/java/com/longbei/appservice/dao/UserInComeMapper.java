package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserInCome;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface UserInComeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserInCome record);

    int insertSelective(UserInCome record);

    UserInCome selectByPrimaryKey(Integer id);

    UserInCome selectByUserId(@Param("userid") String userid);

    int updateByPrimaryKeySelective(UserInCome record);

    int updateByPrimaryKey(UserInCome record);

    int updateTotalByUserId(@Param("userid") String userid,
                            @Param("num") int num,
                            @Param("updatetime") Date updatetime);

    int updateOutGoByUserId(@Param("userid") String userid,
                            @Param("num") int num,
                            @Param("updatetime") Date updatetime);

}
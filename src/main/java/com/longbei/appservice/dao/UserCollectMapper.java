package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserCollect;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserCollectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCollect record);

    int insertSelective(UserCollect record);

    UserCollect selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCollect record);

    int updateByPrimaryKey(UserCollect record);

    int removeCollect(@Param("userid") Long userid,@Param("impid")Long impid, @Param("ctype") String ctype);

    List<UserCollect> selectCollect(@Param("userid")Long uerid,@Param("startNum")int startNum,@Param("endNum")int endNum);

}
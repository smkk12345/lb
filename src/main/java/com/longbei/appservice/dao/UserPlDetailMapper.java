package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserPlDetail;
import org.apache.ibatis.annotations.Param;

public interface UserPlDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPlDetail record);

    int insertSelective(UserPlDetail record);

    UserPlDetail selectByPrimaryKey(Integer id);

    UserPlDetail selectByUserIdAndType(@Param("userid") long userid, @Param("ptype") String ptype);

    int updateByPrimaryKeySelective(UserPlDetail record);

    int updateByPrimaryKey(UserPlDetail record);


}
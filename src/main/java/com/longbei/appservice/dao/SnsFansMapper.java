package com.longbei.appservice.dao;

import java.util.List;

import com.longbei.appservice.entity.SnsFans;
import org.apache.ibatis.annotations.Param;

public interface SnsFansMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SnsFans record);

    int insertSelective(SnsFans record);

    SnsFans selectByPrimaryKey(Integer id);
    
    SnsFans selectByUidAndLikeid(@Param("userid") long userid, @Param("likeuserid") long likeuserid);

    int updateByPrimaryKeySelective(SnsFans record);

    int updateByPrimaryKey(SnsFans record);
    
    int deleteByUidAndLid(@Param("userid") long userid, @Param("likeuserid") long likeuserid);
    
    List<SnsFans> selectFansByUserid(@Param("userid") long userid, @Param("startNum") int startNum, @Param("endNum") int endNum);
    
    List<String> selectListidByUid(@Param("userid") long userid);
    
}
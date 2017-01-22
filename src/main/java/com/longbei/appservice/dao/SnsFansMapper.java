package com.longbei.appservice.dao;

import java.util.List;

import com.longbei.appservice.entity.SnsFans;

public interface SnsFansMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SnsFans record);

    int insertSelective(SnsFans record);

    SnsFans selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SnsFans record);

    int updateByPrimaryKey(SnsFans record);
    
    int deleteByUidAndLid(long userid,long likeuserid);
    
    List<SnsFans> selectFansByUserid(long userid,int startNum,int endNum);
    
}
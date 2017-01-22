package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Award;

public interface AwardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Award record);

    int insertSelective(Award record);

    Award selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Award record);

    int updateByPrimaryKey(Award record);
}
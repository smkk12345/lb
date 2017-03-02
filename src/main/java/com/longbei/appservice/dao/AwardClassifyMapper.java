package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Award;
import com.longbei.appservice.entity.AwardClassify;

import java.util.List;


public interface AwardClassifyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AwardClassify record);

    int insertSelective(AwardClassify record);

    AwardClassify selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AwardClassify record);

    int updateByPrimaryKey(AwardClassify record);

    List<AwardClassify> selectList();

}
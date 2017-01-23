package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ImproveTopic;
import org.apache.ibatis.annotations.Param;

public interface ImproveTopicMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ImproveTopic record);

    int insertSelective(ImproveTopic record);

    ImproveTopic selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImproveTopic record);

    int updateByPrimaryKey(ImproveTopic record);


}
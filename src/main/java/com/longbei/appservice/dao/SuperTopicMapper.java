package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SuperTopic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SuperTopicMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SuperTopic record);

    int insertSelective(SuperTopic record);

    SuperTopic selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SuperTopic record);

    int updateByPrimaryKey(SuperTopic record);


    List<SuperTopic> selectList(
                                @Param("startNum")int startNum,
                                @Param("endNum")int endNum);

}
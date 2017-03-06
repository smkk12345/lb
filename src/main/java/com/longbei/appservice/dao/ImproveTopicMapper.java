package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ImproveTopic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImproveTopicMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ImproveTopic record);

    int insertSelective(ImproveTopic record);

    ImproveTopic selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImproveTopic record);

    int updateByPrimaryKey(ImproveTopic record);

    List<ImproveTopic> selectByImpId(@Param("impid") long impid,
                                     @Param("startNum")int startNum,
                                     @Param("endNum")int endNum);

    List<ImproveTopic> selectByTopicId(
            @Param("topicId") long topicId,
            @Param("startNum")int startNum,
            @Param("endNum")int endNum
    );


}
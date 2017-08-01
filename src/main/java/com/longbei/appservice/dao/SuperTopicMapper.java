package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SuperTopic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SuperTopicMapper {

    int insert(SuperTopic record);

    int updateByPrimaryKey(SuperTopic record);

    List<SuperTopic> selectList(
                                @Param("startNum")int startNum,
                                @Param("endNum")int endNum);

    List<SuperTopic> selectSuperTopicList(@Param("superTopic") SuperTopic SuperTopic,
                                                @Param("startNum") Integer startNum,
                                                @Param("pageSize") Integer pageSize);

    int selectSuperTopicListCount(@Param("superTopic") SuperTopic SuperTopic);

    SuperTopic selectSuperTopicByTopicId(@Param("topicId")Long topicId);

    int removeSuperTopicByTopicId(@Param("topicId") Long topicId);

    int insertSuperTopic(SuperTopic record);

    int updateSuperTopicByTopicId(SuperTopic record);

    int updateImpcount(@Param("topicId") Long topicId,@Param("num") Integer num);

    int updateScancount(Long topicId);

}
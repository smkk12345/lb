package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ImproveTopic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImproveTopicMapper {

    int insert(ImproveTopic record);

    int updateByPrimaryKey(ImproveTopic record);

    List<ImproveTopic> selectByTopicId(
            @Param("topicId") long topicId,
            @Param("startNum")int startNum,
            @Param("endNum")int endNum
    );

    List<ImproveTopic> selectImproveTopicList(@Param("improveTopic") ImproveTopic ImproveTopic,
                                              @Param("nickname")  String nickname,
                                              @Param("startNum") Integer startNum,
                                              @Param("pageSize") Integer pageSize);

    int selectImproveTopicListCount(@Param("improveTopic") ImproveTopic ImproveTopic,
                                    @Param("nickname")  String nickname);

    ImproveTopic selectImproveTopicByImpId(@Param("impId")Long impId);

    /**
     * 更新进步是否话题状态
     * @param impids 进步id
     * @param isTopic 是否为话题 0否 1是
     * @auther IngaWu
     * @currentdate:2017年7月28日
     */
    int updateImpTopicStatusByImpId(@Param("impids") List<Long> impids,
                                    @Param("isTopic")String isTopic);

    int removeImproveTopicByImpId(@Param("impId") Long impId);

    int deleteImpTopicListByTopicId(@Param("topicId") long topicId);

    int insertImproveTopic(ImproveTopic record);

    int updateImproveTopicByImpId(ImproveTopic record);

    int updateImpcount(Long impId);

    int updateScancount(Long impId);
}
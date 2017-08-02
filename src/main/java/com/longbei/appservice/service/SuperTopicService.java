package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.SuperTopic;

/**
 * Created by lixb on 2017/3/1.
 */
public interface SuperTopicService {

    /**
     * 获取超级话题列表
     * @param startNum
     * @param endNum
     * @return
     */
    BaseResp<Object> selectSuerTopicList(int startNum, int endNum);

    /**
     * 通过topicid获取话题的进步列表
     * @param topicId
     * @param startNum
     * @param endNum
     * @return
     */
    BaseResp<Object> selectImprovesByTopicId(long userid,long topicId,int startNum,int endNum);

    /**
     * 获取超级话题列表
     * @title selectSuperTopicList
     * @author IngaWu
     * @currentdate:2017年7月27日
     */
    Page<SuperTopic> selectSuperTopicList(SuperTopic superTopic, Integer startNum, Integer pageSize);

    /**
     * @Title: selectSuperTopicByTopicId
     * @Description: 通过话题id查看超级话题详情
     * @param topicId 话题id
     * @auther IngaWu
     * @currentdate:2017年7月27日
     */
    BaseResp<SuperTopic> selectSuperTopicByTopicId(Long topicId);

    /**
     * @Title: deleteSuperTopicByTopicId
     * @Description: 删除超级话题
     * @param topicId 话题id
     * @auther IngaWu
     * @currentdate:2017年7月27日
     */
    BaseResp<Object> deleteSuperTopicByTopicId(Long topicId);

    /**
     * 添加超级话题
     * @title insertSuperTopic
     * @author IngaWu
     * @currentdate:2017年7月27日
     */
    BaseResp<Object> insertSuperTopic(SuperTopic superTopic);

    /**
     * 编辑超级话题
     * @title updateSuperTopicByTopicId
     * @author IngaWu
     * @currentdate:2017年7月27日
     */
    BaseResp<Object> updateSuperTopicByTopicId(SuperTopic superTopic);
}

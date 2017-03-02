package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.entity.SuperTopic;

import java.util.List;

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
    BaseResp<Object> selectImprovesByTopicId(long topicId,int startNum,int endNum);


}

package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.PubMessage;

/**
 * 推送消息
 *
 * @author luye
 * @create 2017-08-07 上午11:47
 **/
public interface PubMessageService {

    /**
     * 添加推送消息
     * @param pubMessage
     * @return
     */
    BaseResp insertPubMessage(PubMessage pubMessage);

    /**
     * 获取消息明细
     * @param id
     * @return
     */
    BaseResp<PubMessage> selectPubMessage(String id);

    /**
     * 更新消息
     * @param pubMessage
     * @return
     */
    BaseResp updatePubMessage(PubMessage pubMessage);


    /**
     * 删除推送消息
     * @param id
     * @return
     */
    BaseResp deletePubMessage(String id);




}

package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.PubMessage;

import java.util.Date;
import java.util.List;

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
     * 获取推送消息列表
     * @param pubMessage
     * @param pageNo
     * @param pageSize
     * @return
     */
    BaseResp<Page<PubMessage>> selectPubMessageList(PubMessage pubMessage,Integer pageNo,Integer pageSize);


    /**
     * 获取消息列表无分页 支持 msgcontent msgtarget msgstartus 赛选
     * @param pubMessage
     * @return
     */
    BaseResp<List<PubMessage>> selectPubMessageList(PubMessage pubMessage);


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


    /**
     * 发布消息
     * @param id
     * @return
     */
    BaseResp publishMessage(String id, String msgstatus, Date publishtime);




}

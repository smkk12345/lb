package com.longbei.appservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.dao.PubMessageMapper;
import com.longbei.appservice.entity.PubMessage;
import com.longbei.appservice.service.PubMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 推送消息
 *
 * @author luye
 * @create 2017-08-07 上午11:54
 **/
@Service
public class PubMessageServiceImpl implements PubMessageService {

    private static Logger logger = LoggerFactory.getLogger(PubMessageServiceImpl.class);

    @Autowired
    private PubMessageMapper pubMessageMapper;

    @Override
    public BaseResp insertPubMessage(PubMessage pubMessage) {
        BaseResp baseResp = new BaseResp();
        try {
            int res = pubMessageMapper.insertSelective(pubMessage);
            if (res > 0) {
                baseResp.initCodeAndDesp();
            }
        } catch (Exception e) {
            logger.error("insert publish message:{} is error:", JSON.toJSONString(pubMessage),e);
        }

        return baseResp;
    }

    @Override
    public BaseResp<PubMessage> selectPubMessage(String id) {
        BaseResp<PubMessage> baseResp = new BaseResp<>();

        try {
            PubMessage pubMessage = pubMessageMapper.selectByPrimaryKey(Integer.parseInt(id));
            baseResp.initCodeAndDesp();
            baseResp.setData(pubMessage);
        } catch (NumberFormatException e) {
            logger.error("select publish message by id={} is error:",id,e);
        }

        return baseResp;
    }

    @Override
    public BaseResp updatePubMessage(PubMessage pubMessage) {
        BaseResp baseResp = new BaseResp();

        try {
            int res = pubMessageMapper.updateByPrimaryKeySelective(pubMessage);
            if (res > 0){
                baseResp.initCodeAndDesp();
            }
        } catch (Exception e) {
            logger.error("update publish message:{} is error:",JSON.toJSONString(pubMessage),e);
        }

        return baseResp;
    }

    @Override
    public BaseResp deletePubMessage(String id) {
        BaseResp baseResp = new BaseResp();

        try {
            int res = pubMessageMapper.deleteByPrimaryKey(Integer.parseInt(id));
            if (res > 0){
                baseResp.initCodeAndDesp();
            }
        } catch (NumberFormatException e) {
            logger.error("delete publish message by id={} is error:",id,e);
        }

        return baseResp;
    }
}

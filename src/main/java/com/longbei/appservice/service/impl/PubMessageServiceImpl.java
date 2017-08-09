package com.longbei.appservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.dao.ArticleMapper;
import com.longbei.appservice.dao.PubMessageMapper;
import com.longbei.appservice.dao.SeminarMapper;
import com.longbei.appservice.entity.Article;
import com.longbei.appservice.entity.PubMessage;
import com.longbei.appservice.entity.Seminar;
import com.longbei.appservice.service.JPushService;
import com.longbei.appservice.service.PubMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private SeminarMapper seminarMapper;
    @Autowired
    private JPushService jPushService;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolExecutor;

    @Override
    public BaseResp insertPubMessage(PubMessage pubMessage) {
        BaseResp baseResp = new BaseResp();
        try {
            pubMessage.setCreatetime(new Date());
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
    public BaseResp<Page<PubMessage>> selectPubMessageList(PubMessage pubMessage, Integer pageNo, Integer pageSize) {
        BaseResp<Page<PubMessage>> baseResp = new BaseResp<>();
        Page<PubMessage> page = new Page<>(pageNo,pageSize);
        try {
            int totalCount = pubMessageMapper.selectCount(pubMessage);
            List<PubMessage> list = pubMessageMapper.selectList(pubMessage,pageSize * (pageNo - 1),pageSize);
            for (PubMessage pubMessage1 : list){
                intiTargetInfo(pubMessage1);
            }
            page.setTotalCount(totalCount);
            page.setList(list);
            baseResp.initCodeAndDesp();
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("select publish message list is error:",e);
        }
        return baseResp;
    }


    @Override
    public BaseResp<List<PubMessage>> selectPubMessageList(PubMessage pubMessage) {
        BaseResp<List<PubMessage>> baseResp = new BaseResp<>();
        try {
            List<PubMessage> list = pubMessageMapper.selectList(pubMessage,null,null);
            baseResp.initCodeAndDesp();
            baseResp.setData(list);
        } catch (Exception e) {
            logger.error("select pubmessage list no page is error:",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<PubMessage> selectPubMessage(String id) {
        BaseResp<PubMessage> baseResp = new BaseResp<>();

        try {
            PubMessage pubMessage = pubMessageMapper.selectByPrimaryKey(Integer.parseInt(id));
            intiTargetInfo(pubMessage);
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


    @Override
    public BaseResp publishMessage(String id, String msgstatus, Date publishtime) {

        BaseResp baseResp = new BaseResp();
        try {
            PubMessage pubMessage = new PubMessage();
            pubMessage.setId(Integer.parseInt(id));
            pubMessage.setMsgstatus(msgstatus);
            pubMessage.setPublishtime(publishtime);
            int res = pubMessageMapper.updateByPrimaryKeySelective(pubMessage);
            if (res > 0){
                //调用消息推送方法
                PubMessage pubMessage1 = pubMessageMapper.selectByPrimaryKey(Integer.parseInt(id));
                pushMsg(pubMessage1);
                baseResp.initCodeAndDesp();
            }
        } catch (NumberFormatException e) {
            logger.error("publish message id={} is error:",id,e);
        }
        return baseResp;
    }

    private BaseResp publishMessage(PubMessage oPubMessage) {
        BaseResp baseResp = new BaseResp();
        if(null == oPubMessage){
            return baseResp;
        }
        try {
            PubMessage pubMessage = new PubMessage();
            pubMessage.setId(oPubMessage.getId());
            pubMessage.setMsgstatus(oPubMessage.getMsgstatus());
            pubMessage.setPublishtime(oPubMessage.getPublishtime());
            int res = pubMessageMapper.updateByPrimaryKeySelective(pubMessage);
            if (res > 0){
                //调用消息推送方法
                pushMsg(oPubMessage);
                baseResp.initCodeAndDesp();
            }
        } catch (NumberFormatException e) {
            logger.error("publish message id={} is error:",oPubMessage.getId(),e);
        }
        return baseResp;
    }


    @Override
    public BaseResp autoPubMessage() {
        BaseResp baseResp = new BaseResp();
        Long time = new Date().getTime();
        Date sdate = new Date(time-2000);
        Date edate = new Date(time+2000);
        String starttime = DateUtils.formatDate(sdate,"yyyy-MM-dd HH:mm:ss");
        String endtime = DateUtils.formatDate(edate,"yyyy-MM-dd HH:mm:ss");
        List<PubMessage> list = pubMessageMapper.selectListByArea(starttime,endtime);
        if(null == list ||list.isEmpty()){
            return baseResp;
        }
        for (int i = 0; i < list.size(); i++) {
            final PubMessage puMessage = list.get(i);
            puMessage.setMsgstatus("3");
            publishMessage(puMessage);
            pushMsg(puMessage);
        }
        return baseResp.ok();
    }

    /**
     * 消息推送
     * @param pubMessage1
     */
    private void pushMsg(final PubMessage pubMessage1){
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Map<String,Object> map = new HashMap<>();
//                map.put("targetid",pubMessage1.getTargetid());
                map.put("msgtarget",pubMessage1.getMsgtarget());
                map.put("url", AppserviceConfig.articleurl+"?articleid="+pubMessage1.getTargetid());
                jPushService.pushMessageToAll("消息标示",
                        pubMessage1.getMsgtitle(),
                        pubMessage1.getMsgcontent(),
                        null, Constant.JPUSH_TAG_COUNT_1601,map);
            }
        });
    }


    private void intiTargetInfo(PubMessage pubMessage){
        switch (pubMessage.getMsgtarget()){
            case "1":
                Article article = articleMapper.selectByPrimaryKey(Integer.parseInt(pubMessage.getTargetid()+""));
                pubMessage.setArticle(article);
                break;
            case "2":
                Seminar seminar = seminarMapper.selectBySeminarId(pubMessage.getTargetid());
                pubMessage.setSeminar(seminar);
                break;
            default:
                break;
        }
    }




}

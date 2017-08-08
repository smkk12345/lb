package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.PubMessage;
import com.longbei.appservice.service.PubMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 消息推送
 *
 * @author luye
 * @create 2017-08-07 下午3:06
 **/
@RestController
@RequestMapping("api/pubmsg")
public class PubMessageApiController {

    private static Logger logger = LoggerFactory.getLogger(PubMessageApiController.class);

    @Autowired
    private PubMessageService pubMessageService;


    /**
     * 添加推送消息
     * @param pubMessage
     * @return
     */
    @RequestMapping(value = "insert",method = RequestMethod.POST)
    public BaseResp insertPubMessage(@RequestBody PubMessage pubMessage){
        BaseResp baseResp = new BaseResp();
        if (StringUtils.hasBlankParams(pubMessage.getMsgcontent(),pubMessage.getCreateuserid())){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            return baseResp;
        }
        try {
            baseResp = pubMessageService.insertPubMessage(pubMessage);
        } catch (Exception e) {
            logger.error("insert pub message is error:",e);
        }
        return baseResp;
    }


    /**
     * 获取推送消息列表
     * @param pubMessage
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "list",method = RequestMethod.POST)
    public BaseResp<Page<PubMessage>> selectPubMessageList(@RequestBody PubMessage pubMessage, Integer pageNo, Integer pageSize){
        BaseResp<Page<PubMessage>> baseResp = new BaseResp<>();
        try {
            baseResp = pubMessageService.selectPubMessageList(pubMessage,pageNo,pageSize);
        } catch (Exception e) {
            logger.error("select publish message list is error:",e);
        }
        return baseResp;
    }


    /**
     * 获取消息列表无分页 支持 msgcontent msgtarget msgstartus 赛选
     * @param pubMessage
     * @return
     */
    BaseResp<List<PubMessage>> selectPubMessageList(PubMessage pubMessage){
        return null;
    }


    /**
     * 获取消息明细
     * @param id
     * @return
     */
    @RequestMapping(value = "info",method = RequestMethod.POST)
    public BaseResp<PubMessage> selectPubMessage(String id){
        BaseResp<PubMessage> baseResp = new BaseResp<>();
        if (StringUtils.isBlank(id)){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            return baseResp;
        }
        try {
            baseResp = pubMessageService.selectPubMessage(id);
        } catch (Exception e) {
            logger.error("select pubmessage id={} is error:",id,e);
        }
        return baseResp;
    }

    /**
     * 更新消息
     * @param pubMessage
     * @return
     */
    @RequestMapping(value = "update",method = RequestMethod.POST)
    public BaseResp updatePubMessage(@RequestBody PubMessage pubMessage){
        BaseResp baseResp = new BaseResp();
        if (null == pubMessage.getId()){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            return baseResp;
        }
        try {
            baseResp = pubMessageService.updatePubMessage(pubMessage);
        } catch (Exception e) {
            logger.error("update publish message is error:",e);
        }
        return baseResp;
    }


    /**
     * 删除推送消息
     * @param id
     * @return
     */
    @RequestMapping(value = "del",method = RequestMethod.POST)
    public BaseResp deletePubMessage(String id){
        BaseResp baseResp = new BaseResp();
        if (StringUtils.isBlank(id)){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            return baseResp;
        }
        try {
            baseResp = pubMessageService.deletePubMessage(id);
        } catch (Exception e) {
            logger.error("delete publish message id={} is error:",id,e);
        }
        return baseResp;
    }


    /**
     * 发布消息
     * @param id
     * @return
     */
    @RequestMapping(value = "pub",method = RequestMethod.POST)
    public BaseResp publishMessage(String id, String msgstatus, String strpublishtime){
        BaseResp baseResp = new BaseResp();
        if (StringUtils.isBlank(id)){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            return baseResp;
        }
        try {
            Date publishtime = null;
            if (!StringUtils.isBlank(strpublishtime)){
                publishtime = DateUtils.parseDate(strpublishtime);
            }
            baseResp = pubMessageService.publishMessage(id,msgstatus,publishtime);
        } catch (Exception e) {
            logger.error("publish message id={} is error:",id,e);
        }
        return baseResp;
    }


    @RequestMapping(value = "autoPub",method = RequestMethod.POST)
    public BaseResp autoPubMessage(){
        BaseResp baseResp = new BaseResp();
        logger.info("autoPubMessage call and time = {}",DateUtils.getDate());
        try {
            baseResp = pubMessageService.autoPubMessage();
        } catch (Exception e) {
            logger.error("autoPub message is error:",e);
        }
        return baseResp;
    }


}

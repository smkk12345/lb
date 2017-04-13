package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.FriendAddAsk;
import com.longbei.appservice.service.FriendService;
import com.longbei.appservice.service.UserRelationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangyongzhi 17/3/3.
 */
@RestController
@RequestMapping(value="friend")
public class FriendController {
    private Logger logger = LoggerFactory.getLogger(FriendController.class);

    @Autowired
    private FriendService friendService;
    @Autowired
    private UserRelationService userRelationService;

    /**
     * 添加加为好友申请
     * @url http://ip:port/app_service/friend/addFriendAsk
     * @param userid 当前登录用户id
     * @param friendId 添加好友的id
     * @param source 来源
     * @param message 发送的消息
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="addFriendAsk")
    public BaseResp<Object> addFriendAsk(Long userid,Long friendId,FriendAddAsk.Source source,String message){
        logger.info("add friendAsk userId:{} friendId:{} source:{} message:{}",userid,friendId,source,message);
        BaseResp baseResp = new BaseResp<Object>();
        if(userid == null || friendId == null || userid.equals(friendId) || (StringUtils.isNotEmpty(message) && message.length() > FriendAddAsk.MessageContentMaxLength)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(source == null){
            source = FriendAddAsk.Source.unknown;
        }
        baseResp= friendService.addFriendAsk(userid,friendId,source,message);

        return baseResp;
    }

    /**
     * 回复用户信息
     * @url http://ip:port/app_service/friend/replyMessage
     * @param id 加为好友的请求id
     * @param userid 发送消息的用户id 当前登录用户id
     * @param message 回复的消息内容
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value="replyMessage")
    public BaseResp<Object> replyMessage(Long id,Long userid,String message){
        logger.info("reply friendAddAsk message id:{} userId:{} message:{}",id,userid,message);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(id == null || userid == null || StringUtils.isEmpty(message) || (StringUtils.isNotEmpty(message) && message.length() > FriendAddAsk.MessageContentMaxLength)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = friendService.replyMessage(id,userid,message);

        return baseResp;

    }

    /**
     * 获取添加好友的详细信息
     * @url http://ip:port/app_service/friend/getFriendAddAskDetail
     * @param id 消息的id
     * @param  userid 当前登录用户id
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value="getFriendAddAskDetail")
    public BaseResp<Object> getFriendAddAskDetail(Long id,Long userid){
        logger.info("see friendAddAsk detail id:{} userid:{}",id,userid);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(id == null || userid == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = friendService.getFriendAddAskDetail(id,userid);
        return baseResp;
    }

    /**
     * 更改添加好友的状态
     * @url http://ip:port/app_service/friend/updateFriendAddAskStatus
     * @param id 消息id
     * @param status 更改的状态 1.同意 2.拒绝
     * @param userid 当前登录用户id
     * @return
     */
    @SuppressWarnings({ "unchecked", "static-access" })
	@RequestMapping(value="updateFriendAddAskStatus")
    public BaseResp<Object> updateFriendAddAskStatus(Long id,Integer status,Long userid){
        logger.info("update FriendAddAsk Status id:{} status:{} userid:{}",id,status,userid);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(id== null || status == null || status == 0 || userid == null){
            return baseResp.fail("参数错误");
        }
        baseResp = friendService.updateFriendAddAskStatus(id,status,userid);

        return baseResp;
    }

    /**
     * 查询加好友请求列表
     * @url http://ip:port/app_service/friend/friendAddAskList
     * @param userid 用户id
     * @param startNum 开始下标
     * @param endNum 结束下标
     * @return
     */
    @SuppressWarnings({ "unchecked", "static-access" })
	@RequestMapping(value="friendAddAskList")
    public BaseResp<Object> friendAddAskList(Long userid,Integer startNum,Integer endNum){
        logger.info("see friendAddAsk list userId:{} startNum:{} endNum:{}",userid,startNum,endNum);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userid == null){
            return baseResp.fail("参数错误");
        }
        if(startNum == null){
            startNum = Integer.parseInt(Constant.DEFAULT_START_NO);
        }
        Integer pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        if(endNum != null && endNum > startNum){
            pageSize = endNum - startNum;
        }

        baseResp = friendService.friendAddAskList(userid,startNum,pageSize);

        return baseResp;
    }

    /**
     * 清空加好友的请求消息
     * @api http://ip:port/app_service/friend/clearFriendAsk
     * @param userid
     * @return
     */
    @RequestMapping(value="clearFriendAsk")
    public BaseResp<Object> clearFriendAsk(Long userid){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userid == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.friendService.clearFriendAsk(userid);
        return baseResp;
    }

}

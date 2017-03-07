package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.FriendAddAsk;
import com.longbei.appservice.service.FriendService;
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

    /**
     * 添加加为好友申请
     * @param userId 当前登录用户id
     * @param friendId 添加好友的id
     * @param source 来源
     * @param message 发送的消息
     * @return
     */
    @RequestMapping(value="addFriendAsk")
    public BaseResp<Object> addFriendAsk(Long userId,Long friendId,FriendAddAsk.Source source,String message){
        logger.info("add friendAsk userId:{} friendId:{} source:{} message:{}",userId,friendId,source,message);
        BaseResp baseResp = new BaseResp<Object>();
        if(userId == null || friendId == null || userId.equals(friendId)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(source == null){
            source = FriendAddAsk.Source.unknown;
        }
        baseResp= friendService.addFriendAsk(userId,friendId,source,message);

        return baseResp;
    }

    /**
     * 回复用户信息
     * @param id 加为好友的请求id
     * @param userId 发送消息的用户id 当前登录用户id
     * @param message 回复的消息内容
     * @return
     */
    @RequestMapping(value="replyMessage")
    public BaseResp<Object> replyMessage(Long id,Long userId,String message){
        logger.info("reply friendAddAsk message id:{} userId:{} message:{}",id,userId,message);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(id == null || userId == null || StringUtils.isEmpty(message)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = friendService.replyMessage(id,userId,message);

        return baseResp;

    }

    /**
     * 获取添加好友的详细信息
     * @param id 消息的id
     * @param  userId 当前登录用户id
     * @return
     */
    @RequestMapping(value="getFriendAddAskDetail")
    public BaseResp<Object> getFriendAddAskDetail(Long id,Long userId){
        logger.info("see friendAddAsk detail id:{} userId:{}",id,userId);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(id == null || userId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = friendService.getFriendAddAskDetail(id,userId);
        return baseResp;
    }

    /**
     * 更改添加好友的状态
     * @param id 消息id
     * @param status 更改的状态 1.同意 2.拒绝
     * @param userId 当前登录用户id
     * @return
     */
    @RequestMapping(value="updateFriendAddAskStatus")
    public BaseResp<Object> updateFriendAddAskStatus(Long id,Integer status,Long userId){
        logger.info("update FriendAddAsk Status id:{} status:{} userId:{}",id,status,userId);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(id== null || status == null || status == 0 || userId == null){
            return baseResp.fail("参数错误");
        }
        baseResp = friendService.updateFriendAddAskStatus(id,status,userId);

        return baseResp;
    }

    /**
     * 查询加好友请求列表
     * @param userId 用户id
     * @param startNo 开始下标
     * @param endNo 结束下标
     * @return
     */
    @RequestMapping(value="friendAddAskList")
    public BaseResp<Object> friendAddAskList(Long userId,Integer startNo,Integer endNo){
        logger.info("see friendAddAsk list userId:{} startNo:{} endNo:{}",userId,startNo,endNo);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userId == null){
            return baseResp.fail("参数错误");
        }
        if(startNo == null){
            startNo = Integer.parseInt(Constant.DEFAULT_START_NO);
        }
        Integer pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        if(endNo != null && endNo > startNo){
            pageSize = endNo - startNo;
        }

        baseResp = friendService.friendAddAskList(userId,startNo,pageSize);

        return baseResp;
    }
}

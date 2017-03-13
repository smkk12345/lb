package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.FriendAddAsk;

/**
 * Created by wangyongzhi 17/3/6.
 */
public interface FriendService extends BaseService{

    /**
     * 请求添加朋友
     * @param userId 当前登录用户id
     * @param friendId 添加的朋友id
     * @return
     */
    BaseResp addFriendAsk(Long userId, Long friendId,FriendAddAsk.Source source,String message);

    /**
     * 回复加好友信息
     * @param id
     * @param userId
     * @param message
     * @return
     */
    BaseResp<Object> replyMessage(Long id, Long userId, String message);

    /**
     * 获取好友添加详情
     * @param id
     * @param userId
     * @return
     */
    BaseResp<Object> getFriendAddAskDetail(Long id, Long userId);

    /**
     * 更改用户的请求加好友状态
     * @param id
     * @param status
     * @param userId
     * @return
     */
    BaseResp<Object> updateFriendAddAskStatus(Long id, Integer status, Long userId);

    /**
     * 添加好友列表
     * @param userId 用户id
     * @param startNo 开始下标
     * @param pageSize 每页条数
     * @return
     */
    BaseResp<Object> friendAddAskList(Long userId, Integer startNo, Integer pageSize);

    /**
     * 校验两个用户是否是朋友
     * @param userId 用户id
     * @param friendId 朋友id
     * @return
     */
    boolean checkIsFriend(Long userId,Long friendId);

    /**
     * 获取用户昵称 如果用户是好友,则返回当前用户设置的好友昵称,如果不是好友,则返回friend User的昵称
     * @param userId 当前登录用户
     * @param friendId 好友的id
     * @return
     */
    String getNickName(Long userId, Long friendId);
}

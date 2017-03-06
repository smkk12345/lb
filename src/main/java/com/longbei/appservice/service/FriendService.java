package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.FriendAddAsk;

/**
 * Created by wangyongzhi 17/3/6.
 */
public interface FriendService {

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
}

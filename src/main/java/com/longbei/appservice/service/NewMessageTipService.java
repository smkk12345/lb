package com.longbei.appservice.service;

import com.longbei.appservice.entity.NewMessageTip;

/**
 * Created by wangyongzhi 17/3/6.
 */
public interface NewMessageTipService {

    /**
     * 更改用户的新消息状态
     * @param userId 用户的id
     * @param messageType 操作的新消息类型
     * @param isRead 标记为是否已读 true代表已读(没有新消息),false代表有消息
     * @return
     */
    public boolean updateNewMessageTip(Long userId, NewMessageTip.MessageType messageType,boolean isRead);

    /**
     * 获取用户新消息提示
     * @param userId
     * @return
     */
    public NewMessageTip getUserNewMessageTip(Long userId);
}

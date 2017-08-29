package com.longbei.appservice.entity;

import java.io.Serializable;

/**
 * 用户的新消息提示
 * Created by wangyongzhi 17/3/6.
 */
public class NewMessageTip implements Serializable {

    public enum MessageType{
        friendAddAsk,//加好友的请求
    }

    private Long id;
    private Long userId;//用户id
    private boolean friendAddAsk;//加好友请求 true代表有新消息 false代表没有新消息

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 设置用户id
     * @return
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 是否有加好友的请求
     * @return
     */
    public boolean getFriendAddAsk() {
        return friendAddAsk;
    }

    /**
     * 是否有加好友的请求
     * @return
     */
    public void setFriendAddAsk(boolean friendAddAsk) {
        this.friendAddAsk = friendAddAsk;
    }

}

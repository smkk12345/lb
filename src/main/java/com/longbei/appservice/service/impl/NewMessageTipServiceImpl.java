package com.longbei.appservice.service.impl;

import com.longbei.appservice.dao.mongo.dao.NewMessageTipDao;
import com.longbei.appservice.entity.NewMessageTip;
import com.longbei.appservice.service.NewMessageTipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wangyongzhi 17/3/6.
 */
@Service("newMessageTipService")
public class NewMessageTipServiceImpl implements NewMessageTipService {
    @Autowired
    private NewMessageTipDao newMessageTipDao;

    /**
     * 更改用户的新消息状态
     * @param userId 用户的id
     * @param messageType 操作的新消息类型
     * @param isRead 标记为是否已读 true代表已读(没有新消息),false代表有消息
     * @return
     */
    @Override
    public boolean updateNewMessageTip(Long userId, NewMessageTip.MessageType messageType, boolean isRead) {
        if (userId == null || messageType == null) {
            return false;
        }
        return newMessageTipDao.updateNewMessageTip(userId,messageType,isRead);
    }

    /**
     * 获取用户的新消息提示
     * @param userId
     * @return
     */
    @Override
    public NewMessageTip getUserNewMessageTip(Long userId) {
        if(userId == null){
            return null;
        }
        return newMessageTipDao.getUserNewMessageTip(userId);
    }
}

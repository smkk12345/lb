package com.longbei.appservice.dao.mongo.dao;

import com.longbei.appservice.common.dao.BaseMongoDao;
import com.longbei.appservice.entity.NewMessageTip;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * Created by wangyongzhi 17/3/6.
 */
@Repository
public class NewMessageTipDao extends BaseMongoDao<NewMessageTip>{

    /**
     * 更改用户的新消息状态
     * @param userId 用户的id
     * @param messageType 操作的新消息类型
     * @param isRead 标记为是否已读 true代表已读(没有新消息),false代表有消息
     * @return
     */
    public boolean updateNewMessageTip(Long userId, NewMessageTip.MessageType messageType, boolean isRead) {
        Criteria criteria = Criteria.where("userId").is(userId);
        Query query = new Query(criteria);
        Update update = new Update();
        switch (messageType){
            case friendAddAsk:
                update.set("friendAddAsk",isRead);
                break;
        }
        mongoTemplate.upsert(query,update,NewMessageTip.class);
        return true;
    }

    /**
     * 获取用户的新消息提示
     * @param userId
     * @return
     */
    public NewMessageTip getUserNewMessageTip(Long userId) {
        Criteria criteria = Criteria.where("userId").is(userId);
        Query query = new Query(criteria);
        return mongoTemplate.findOne(query,NewMessageTip.class);
    }
}

package com.longbei.appservice.dao.mongo.dao;

import com.longbei.appservice.common.dao.BaseMongoDao;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.FriendAddAsk;
import net.sf.json.JSONArray;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


/**
 * Created by wangyongzhi 17/3/6.
 */
@Repository
public class FriendMongoDao extends BaseMongoDao<FriendAddAsk>{

    /**
     * 根据senderUserId 以及receiveUserId 查询添加好友记录
     * @param senderUserId
     * @param receiveUserId
     * @return
     */
    public FriendAddAsk findFriendAddAsk(Long senderUserId,Long receiveUserId){
        Criteria criteria = Criteria.where("senderUserId").is(senderUserId).and("receiveUserId").is(receiveUserId);
        Query query = new Query(criteria);
        return mongoTemplate.findOne(query,FriendAddAsk.class);
    }

    /**
     * 新增添加好友记录
     * @param newFriendAddAsk
     */
    public void addFriendAddAsk(FriendAddAsk newFriendAddAsk) {
        mongoTemplate.save(newFriendAddAsk);
    }

    /**
     * 更改添加好友请求的记录 状态
     * @param userId 用户id
     * @param friendId 请求添加的好友id
     * @param status 更改的状态
     * @param updateTimeFlag 是否更改添加好友的申请时间
     */
    public void updateFriendAddAskStatus(Long userId, Long friendId, Integer status, Boolean updateTimeFlag) {
        Criteria  criteria = Criteria.where("userId").is(userId).and("friendId").is(friendId);
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("status",status);
        if(updateTimeFlag != null && updateTimeFlag){
            update.set("createDate",new Date());
        }
        mongoTemplate.updateFirst(query,update,FriendAddAsk.class);
    }

    /**
     * 清空好友的加群申请列表
     * @param userid
     */
    public void clearFriendAsk(Long userid) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        query.addCriteria(criteria.orOperator(Criteria.where("senderUserId").is(userid),
                Criteria.where("receiveUserId").is(userid)));
        mongoTemplate.remove(query,FriendAddAsk.class);
    }

    /**
     * 根据id获取用户的加好友信息
     * @param id
     * @return
     */
    public FriendAddAsk findFriendAddAskById(Long id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query,FriendAddAsk.class);
    }

    /**
     * 回复信息
     * @param id 加好友的消息id
     * @param jsonArray 消息内容
     * @param flag 更新发送请求者的已读状态还是被请求者的已读状态 sender / receive
     * @param isRead 是否已读
     * @param status 接受的状态 1.通过 2.不通过
     */
    public void updateFriendAddAsk(Long id, JSONArray jsonArray,String flag,Boolean isRead,Integer status,Boolean updateCreateDate) {
        Criteria criteria = Criteria.where("id").is(id);
        Query query= new Query(criteria);
        Update update = new Update();
        if(jsonArray != null && jsonArray.size() > 0){
            update.set("message",jsonArray);
        }
        if(StringUtils.isNotEmpty(flag) && "sender".equals(flag)){
            update.set("senderIsRead",isRead);
        }else if(StringUtils.isNotEmpty(flag)){
            update.set("receiveIsRead",isRead);
        }
        if(status != null && (status == 1 || status == 2)){
            update.set("status",status);
        }
        if(updateCreateDate != null && updateCreateDate){
            update.set("createDate",new Date());
        }
        mongoTemplate.updateFirst(query,update,FriendAddAsk.class);
    }

    /**
     * 查询用户未读的请求消息
     * @param userId
     * @return
     */
    public int findUserUnReadAsk(Long userId) {
        Criteria criteria = new Criteria();
        Query query = new Query();
        query.addCriteria(criteria.orOperator(Criteria.where("senderUserId").is(userId).and("senderIsRead").is(false),
                Criteria.where("receiveUserId").is(userId).and("receiveIsRead").is(false)));
        return (int)mongoTemplate.count(query,FriendAddAsk.class);
    }

    /**
     * 添加好友列表
     * @param userId
     * @param isRead 是否已读 true代表已读 false代表未读
     * @param startNo
     * @param pageSize
     * @return
     */
    public List<FriendAddAsk> friendAddAskList(Long userId,Boolean isRead, Integer startNo, Integer pageSize) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        if(isRead != null){
            query.addCriteria(criteria.orOperator(Criteria.where("senderUserId").is(userId).and("senderIsRead").is(isRead),
                    Criteria.where("receiveUserId").is(userId).and("receiveIsRead").is(isRead)));
        }else{
            query.addCriteria(criteria.orOperator(Criteria.where("senderUserId").is(userId),
                    Criteria.where("receiveUserId").is(userId)));
        }

        query.with(new Sort(Sort.Direction.DESC,"createDate"));
        if(startNo != null && startNo > -1){
            query.skip(startNo);
        }
        if(pageSize != null && pageSize > 0){
            query.limit(pageSize);
        }
        return mongoTemplate.find(query,FriendAddAsk.class);
    }

}

package com.longbei.appservice.dao.mongo.dao;

import com.longbei.appservice.common.dao.BaseMongoDao;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.MsgRed;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 消息红点
 *
 * @author luye
 * @create 2017-08-04 下午4:13
 **/
@Repository
public class MsgRedMongDao extends BaseMongoDao<MsgRed>{


    public List<MsgRed> getMsgRed(String userid,String mtype){
        Criteria criteria = Criteria.where("userid").is(userid);
        if (!StringUtils.isBlank(mtype)){
            criteria.and("mtype").is(mtype);
        }
        Query query = new Query(criteria);
        List<MsgRed> msgReds = mongoTemplate.find(query,MsgRed.class);
        return msgReds;
    }


    public MsgRed getMsgRed(String userid,String mtype,String msgtype){
        Criteria criteria = Criteria.where("userid").is(userid)
        .and("mtype").is(mtype).and("msgtype").is(msgtype);
        Query query = new Query(criteria);
        MsgRed msgRed = mongoTemplate.findOne(query,MsgRed.class);
        return msgRed;
    }

    public void insertOrUpdateMsgRed(String userid,String mtype,String msgtype,String remark){
        Criteria criteria = Criteria.where("userid").is(userid)
                .and("mtype").is(mtype).and("msgtype").is(msgtype);
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("updatetime",new Date());
        update.set("remark",remark);
        mongoTemplate.upsert(query,update,MsgRed.class);
    }

    public void deleteMsgRed(String userid,String mtype,String msgtype){
        Criteria criteria = Criteria.where("userid").is(userid);
        if (StringUtils.isBlank(mtype)){
            criteria.and("mtype").is(mtype);
        }
        if (StringUtils.isBlank(msgtype)){
            criteria.and("msgtype").is(msgtype);
        }
        Query query = new Query(criteria);
        mongoTemplate.remove(query,MsgRed.class);
    }


}

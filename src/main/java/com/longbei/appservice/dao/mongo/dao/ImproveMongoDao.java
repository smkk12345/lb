package com.longbei.appservice.dao.mongo.dao;

import com.longbei.appservice.common.dao.BaseMongoDao;
import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.entity.ImproveLFD;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 进步mongo操作
 *
 * @author luye
 * @create 2017-02-20 上午11:06
 **/
@Repository
public class ImproveMongoDao extends BaseMongoDao<Improve>{


    public void saveImproveLfd(ImproveLFD improveLFD){
        Criteria criteria = Criteria.where("impid").is(improveLFD.getImpid())
                .and("userid").is(improveLFD.getUserid())
                .and("opttype").is(improveLFD.getOpttype());
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("createtime",improveLFD.getCreatetime());
        mongoTemplate.upsert(query,update,ImproveLFD.class);
    }


    public void removeImproveLfd(ImproveLFD improveLFD){
        Criteria criteria = Criteria.where("impid").is(improveLFD.getImpid())
                .and("userid").is(improveLFD.getUserid())
                .and("opttype").is(improveLFD.getOpttype());
        Query query = new Query(criteria);
        mongoTemplate.remove(query,ImproveLFD.class);
    }


    public List<ImproveLFD> selectImproveLfdList(String impid){
        Criteria criteria = Criteria.where("impid").is(impid);
        Query query = new Query(criteria);
        query.with(new Sort(Sort.Direction.DESC, "createtime"));
        query.limit(5);
        List<ImproveLFD> improveLFDs = mongoTemplate.find(query,ImproveLFD.class);
        return improveLFDs;
    }


    public Long selectCountImproveLFD(String impid){
        Criteria criteria = Criteria.where("impid").is(impid);
        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query,ImproveLFD.class);
        return count;
    }



}

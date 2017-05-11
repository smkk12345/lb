package com.longbei.appservice.dao.mongo.dao;

import com.longbei.appservice.common.dao.BaseMongoDao;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.entity.UserRelationChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by lixb on 2017/5/10.
 */
@Repository
public class UserRelationChangeDao extends BaseMongoDao<UserRelationChange> {

    private static Logger logger = LoggerFactory.getLogger(UserRelationChangeDao.class);
    /**
     * 增 删 改 差
     */

    public int save(String uid,String changeuid){
        int res = 0;
        UserRelationChange userRelationChange = new UserRelationChange();
        userRelationChange.setUid(uid);
        userRelationChange.setChangeuid(changeuid);
        userRelationChange.setUpdatetime(new Date());
        try {
            userRelationChange = save(userRelationChange);
        }catch (Exception e){
            logger.error("uid={},changeuid={}",uid,changeuid,e);
        }

        return res;
    }

    /**
     * 通过用户和时间查列表
     * @param uid
     * @param dateStr
     * @return
     */
    public List<UserRelationChange> getListByUid(long uid,String dateStr){
        Query query = new Query();
        Criteria criteria = new Criteria();
        try{
            Date date = DateUtils.formatDate(dateStr,"yyyy-MM-dd HH:mm:ss");
            query.addCriteria(criteria.orOperator(Criteria.where("uid").is(String.valueOf(uid))
                    ));
            criteria.and("updatetime").lt(date);
            query.with(new Sort(Sort.Direction.DESC,"updatetime"));
        }catch (Exception e){

        }
        return mongoTemplate.find(query,UserRelationChange.class);
    }

}

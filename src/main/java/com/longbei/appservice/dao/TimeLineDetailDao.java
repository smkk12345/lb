package com.longbei.appservice.dao;/**
 * Created by luye on 2017/1/23.
 */

import com.longbei.appservice.common.dao.BaseMongoDao;
import com.longbei.appservice.entity.ImproveCircle;
import com.longbei.appservice.entity.TimeLine;
import com.longbei.appservice.entity.TimeLineDetail;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 时间线详情mongo操作
 *
 * @author luye
 * @create 2017-01-23 上午9:38
 **/
@Repository
public class TimeLineDetailDao extends BaseMongoDao<TimeLineDetail>{


    public void updateImproveFileKey(String sourcekey,String pickey,String fliekey){
        Criteria criteria = Criteria.where("sourcekey").is(sourcekey);
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("fileKey",fliekey).set("photos",pickey);
        mongoTemplate.updateMulti(query,update,TimeLineDetail.class);
    }

    public List<TimeLineDetail> selectImproveList(String isrecommend,String brief,List<String> userids, Date lastdate, int pagesize){
        Criteria criteria = new Criteria();
        if (null != userids && userids.size() != 0){
            criteria.in(userids);
        }

        if (null != lastdate) {
            criteria.and("createdate").lt(lastdate);
        }
        return null;
    }

    public void deleteImprove(Long improveid,String userid){
        Query query = Query.query(Criteria.where("improveId").is(improveid));
        TimeLineDetail timeLineDetail = mongoTemplate.findOne(query, TimeLineDetail.class);
        if(timeLineDetail != null){
            Query deletequery = Query.query(Criteria.where("timeLineDetail.$id").is(timeLineDetail.getImproveId()));
            mongoTemplate.remove(deletequery, TimeLine.class);
        }
        mongoTemplate.remove(query, TimeLineDetail.class);
    }



}

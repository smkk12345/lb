package com.longbei.appservice.dao;/**
 * Created by luye on 2017/1/23.
 */

import com.longbei.appservice.common.dao.BaseMongoDao;
import com.longbei.appservice.entity.ImproveCircle;
import com.longbei.appservice.entity.TimeLine;
import com.longbei.appservice.entity.TimeLineDetail;
import org.springframework.data.domain.Sort;
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

    public List<TimeLineDetail> selectRecommendImproveList(String brief,List<String> userids, Date lastdate,
                                                  int pagesize){
        Criteria criteria = Criteria.where("isrecommend").is("1");
        if (null != userids && userids.size() != 0){
            criteria.in(userids);
        }
        if (!StringUtils.isEmpty(brief)){
            criteria.regex(brief);
        }
        if (null != lastdate) {
            criteria.and("createdate").lt(lastdate);
        }
        Query query = new Query(criteria);
        query.with(new Sort(Sort.Direction.DESC, "recommendtime"));
        query.limit(pagesize);
        List<TimeLineDetail> timeLineDetails = mongoTemplate.find(query,TimeLineDetail.class);
        return timeLineDetails;
    }

    public void updateRecommendImprove(String impid,String businesstype,String isrecommend){
        Criteria criteria = Criteria.where("impid").is(impid)
                .and("businesstype").is(businesstype);
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("isrecommend",isrecommend);
        update.set("recommendtime",new Date());
        mongoTemplate.updateMulti(query,update, TimeLineDetail.class);
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

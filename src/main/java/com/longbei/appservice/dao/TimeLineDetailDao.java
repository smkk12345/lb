package com.longbei.appservice.dao;/**
 * Created by luye on 2017/1/23.
 */

import com.longbei.appservice.common.dao.BaseMongoDao;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.TimeLine;
import com.longbei.appservice.entity.TimeLineDetail;
import com.longbei.appservice.entity.UserImproveStatistic;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    public Long selectRecommendImproveCount(String brief,List<String> userids, Date lastdate){
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
        Long count = mongoTemplate.count(query,TimeLineDetail.class);
        return count;
    }

    public List<TimeLineDetail> selectRecommendImproveList(String brief,List<String> userids, int startno,
                                                  int pagesize){
        Criteria criteria = Criteria.where("isrecommend").is("1");
        if (null != userids && userids.size() != 0){
            criteria.and("user.$id").in(userids);
        }
        if (!StringUtils.isEmpty(brief)){
            criteria.and("brief").regex(brief);
        }
        Query query = new Query(criteria);
        query.with(new Sort(Sort.Direction.DESC,"sort")).with(new Sort(Sort.Direction.DESC, "recommendtime"));
        query.skip(startno);
        query.limit(pagesize);
        System.out.println(query);
        List<TimeLineDetail> timeLineDetails = mongoTemplate.find(query,TimeLineDetail.class);
        return timeLineDetails;
    }

    public void updateRecommendImprove(List<Long> impids,String businesstype,String isrecommend){
        Criteria criteria = Criteria.where("improveId").in(impids)
                .and("businesstype").is(businesstype);
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("isrecommend",isrecommend);
        update.set("recommendtime",new Date());
        System.out.println(query);
        System.out.println(update);
        mongoTemplate.updateMulti(query,update, TimeLineDetail.class);
    }

    public void updateRecommendImproveSort(Long improveId,String businesstype,int sort){
        Criteria criteria = Criteria.where("improveId").in(improveId)
                .and("businesstype").is(businesstype);
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("sort",sort);
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

    /**
     * 获取用户从startDate 到 endDate之间发布的进步数量
     * @param startDate
     * @param endDate
     * @return
     */
    public List<UserImproveStatistic> getUserImproveStatistic(Date startDate, Date endDate) {
        List<UserImproveStatistic> resultList = new ArrayList<UserImproveStatistic>();
        try{
            String currentDay = DateUtils.formatDate(startDate);
            Criteria createtime = Criteria.where("createdate");
//            createtime.gte(startDate);
            createtime.lte(endDate);
            GroupOperation groupOperation = Aggregation.group("user").count().as("count");
            Aggregation agg = null;
            agg = Aggregation.newAggregation(TimeLineDetail.class,Aggregation.match(createtime),groupOperation);
            AggregationResults<Map> results =mongoTemplate.aggregate(agg,TimeLineDetail.class,Map.class);

            for(Map map : results.getMappedResults()){
                AppUserMongoEntity userMongoEntity = (AppUserMongoEntity) map.get("_id");
                if(userMongoEntity == null){
                    continue;
                }
                UserImproveStatistic userImproveStatistic = new UserImproveStatistic();
                userImproveStatistic.setUserid(userMongoEntity.getUserid());
                userImproveStatistic.setImprovecount((Integer) map.get("count"));
                userImproveStatistic.setCurrentday(currentDay);
                resultList.add(userImproveStatistic);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultList;
    }
}

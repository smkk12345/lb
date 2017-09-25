package com.longbei.appservice.dao;/**
 * Created by luye on 2017/1/23.
 */

import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.dao.BaseMongoDao;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.impl.UserCheckinDetailImpl;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 时间线详情mongo操作
 *
 * @author luye
 * @create 2017-01-23 上午9:38
 **/
@Repository
public class TimeLineDetailDao extends BaseMongoDao<TimeLineDetail>{
    private static Logger logger = LoggerFactory.getLogger(TimeLineDetailDao.class);
    public void updateImproveFileKey(String sourcekey,String pickey,
                                     String fliekey,String duration,String picattribute){
        Criteria criteria = Criteria.where("sourcekey").is(sourcekey);
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("fileKey",fliekey).set("photos",pickey).set("duration",duration).set("picattribute",picattribute);
        mongoTemplate.updateMulti(query,update,TimeLineDetail.class);
    }

    public void updateImproveLike(String businesstype,Long impid,Integer num){
        Criteria criteria = Criteria.where("businesstype").is(businesstype).and("improveId").is(impid);
        Query query = new Query(criteria);
        Update update = new Update();
        update.inc("likes",num);
        mongoTemplate.updateMulti(query,update,TimeLineDetail.class);
    }

    public void updateImproveFlower(String businesstype,Long impid,Integer num){
        Criteria criteria = Criteria.where("businesstype").is(businesstype).and("improveId").is(impid);
        Query query = new Query(criteria);
        Update update = new Update();
        update.inc("flowers",num);
        mongoTemplate.updateMulti(query,update,TimeLineDetail.class);
    }

    public Long selectRecommendImproveCount(String brief,List<String> userids, Date lastdate){
        Criteria criteria = Criteria.where("isrecommend").is("1");
        if (null != userids && userids.size() != 0){
            criteria.and("user.$id").in(userids);
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

    public TimeLineDetail select(Long improveId){
        Criteria criteria = Criteria.where("improveId").is(improveId);

        Query query = new Query(criteria);
        TimeLineDetail timeLineDetail = mongoTemplate.findOne(query,TimeLineDetail.class);
        return timeLineDetail;
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
//        System.out.println(query);
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
//        System.out.println(query);
//        System.out.println(update);
        mongoTemplate.updateMulti(query,update, TimeLineDetail.class);
    }

    /**
     * 获取进步列表（for话题）
     * @param businesstype 微进步关联的业务类型 0未关联 1目标 2榜 3圈子 4教室 5教室批复作业
     * @param isTopic isTopic 是否为话题 0否 1是
     * @param brief 微进步内容
     * @param userids 用户id
     * @param startNum 分页起始值
     * @param pageSize 每页显示条数
     * @auther IngaWu
     * @currentdate:2017年7月29日
     */
    public List<TimeLineDetail> selectImpTopicList(String topicId,String businesstype,String isTopic,String brief,List<String> userids, int startNum,
                                                           int pageSize){
        Criteria criteria = new Criteria();
        if(!StringUtils.isEmpty(topicId)){
            criteria.and("topicid").is(topicId);
        }
        if(!StringUtils.isEmpty(isTopic)){
            if ("0".equals(isTopic)) {
                criteria.and("istopic").ne("1");
            }else {
                criteria.and("istopic").is(isTopic);
            }
        }
        if(!StringUtils.isEmpty(businesstype)){
            criteria.and("businesstype").is(businesstype);
        }
        if (null != userids && userids.size() != 0){
            criteria.and("user.$id").in(userids);
        }
        if (!StringUtils.isEmpty(brief)){
            criteria.and("brief").regex(brief);
        }
        Query query = new Query(criteria);
        query.skip(startNum);
        query.limit(pageSize);
        System.out.println(query);
        List<TimeLineDetail> timeLineDetails = mongoTemplate.find(query,TimeLineDetail.class);
        return timeLineDetails;
    }


    public Long selectImpTopicListCount(String topicId,String businesstype,String isTopic,String brief,List<String> userids){
        Criteria criteria = new Criteria();
        if(!StringUtils.isEmpty(topicId)){
            criteria.and("topicid").is(topicId);
        }
        if(!StringUtils.isEmpty(isTopic)){
            if ("0".equals(isTopic)) {
                criteria.and("istopic").ne("1");
            }else {
                criteria.and("istopic").is(isTopic);
            }
        }
        if(!StringUtils.isEmpty(businesstype)){
            criteria.and("businesstype").is(businesstype);
        }
        if (null != userids && userids.size() != 0){
            criteria.and("user.$id").in(userids);
        }
        if (!StringUtils.isEmpty(brief)){
            criteria.and("brief").regex(brief);
        }
        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query,TimeLineDetail.class);
        return count;
    }

    public void updateImproveTopicStatus(List<Long> impids,String businesstype,String isTopic){
        Criteria criteria = new Criteria();
        if(null != impids && impids.size()!=0){
            criteria.and("improveId").in(impids);
        }
        if(!StringUtils.isEmpty(businesstype)){
            criteria.and("businesstype").is(businesstype);
        }
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("istopic",isTopic);
        System.out.println(query);
        System.out.println("update--------"+update);
        mongoTemplate.updateMulti(query,update, TimeLineDetail.class);
    }

    public void updateRecommendImproveSort(Long improveId,String businesstype,int sort){
        Criteria criteria = Criteria.where("improveId").is(improveId)
                .and("businesstype").is(businesstype);
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("sort",sort);
        mongoTemplate.updateMulti(query,update, TimeLineDetail.class);
    }

    public void deleteImproveByBusinessid(String businessid,String businesstype,String userid){
        Criteria criteria = Criteria.where("businessid").is(Long.parseLong(businessid))
                .and("businesstype").is(businesstype);
        Query query = new Query(criteria);
        List<TimeLineDetail> lists = mongoTemplate.find(query,TimeLineDetail.class);
        for (TimeLineDetail timeLineDetail : lists){
            deleteImprove(timeLineDetail.getImproveId(),userid);
        }
    }

    public void deleteImprove(Long improveid,String userid){
        Query query = Query.query(Criteria.where("improveId").is(improveid));
        TimeLineDetail timeLineDetail = mongoTemplate.findOne(query, TimeLineDetail.class);
        if(timeLineDetail != null){
            Query deletequery = Query.query(Criteria.where("timeLineDetail.$id").is(timeLineDetail.getId()));
            mongoTemplate.remove(deletequery, Constant.TIMELINE_IMPROVE_SQUARE_COLLECTION);
            mongoTemplate.remove(deletequery, Constant.TIMELINE_IMPROVE_SELF_COLLECTION);
            mongoTemplate.remove(deletequery, Constant.TIMELINE_IMPROVE_FRIEND_COLLECTION);
            mongoTemplate.remove(deletequery, Constant.TIMELINE_IMPROVE_ATTR_COLLECTION);
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
            createtime.gte(startDate);
            createtime.lte(endDate);
            GroupOperation groupOperation = Aggregation.group("user").count().as("count");
            Aggregation agg = null;
            agg = Aggregation.newAggregation(TimeLineDetail.class,Aggregation.match(createtime),groupOperation);
            AggregationResults<Map> results =mongoTemplate.aggregate(agg,TimeLineDetail.class,Map.class);

            for(Map map : results.getMappedResults()){
                AppUserMongoEntity userMongoEntity = null;
                try{
                    userMongoEntity = (AppUserMongoEntity) map.get("_id");
                }catch (Exception e){
//                    logger.error("getUserImproveStatistic map.get(_id)={}",
//                            JSONObject.fromObject(map.get("_id")).toString(),e);
                    userMongoEntity = new AppUserMongoEntity();
                    LinkedHashMap map1 = (LinkedHashMap)map.get("_id");
                    String userid = (String) map1.get("_id");
                    userMongoEntity.setUserid(userid);
//                    continue;
                }

                if(userMongoEntity == null||StringUtils.isEmpty(userMongoEntity.getUserid())){
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

    public TimeLineDetail selectTimeLineByImpid (Long impid){
        Query query = Query.query(Criteria.where("improveId").is(impid));
        TimeLineDetail timeLineDetail = mongoTemplate.findOne(query, TimeLineDetail.class);
        return timeLineDetail;
    }
}

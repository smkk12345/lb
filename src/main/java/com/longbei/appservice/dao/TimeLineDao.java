package com.longbei.appservice.dao;/**
 * Created by luye on 2017/1/23.
 */

import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.dao.BaseMongoDao;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.entity.TimeLine;
import com.longbei.appservice.entity.TimeLineDetail;
import com.mongodb.WriteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 时间线mongo操作
 *
 * @author luye
 * @create 2017-01-23 上午9:37
 **/
@Repository
public class TimeLineDao extends BaseMongoDao<TimeLine>{

    private static Logger logger = LoggerFactory.getLogger(TimeLineDao.class);

    public void save(TimeLine timeLine,String collectName){
        mongoTemplate.save(timeLine,collectName);
    }

    public void insertList(List<TimeLine> timeLineList,String collectName){
        if(timeLineList == null || timeLineList.size() == 0){
            return ;
        }
        mongoTemplate.insert(timeLineList,collectName);
    }

    public List<TimeLine> selectTimeListByUserAndType(String userid,String ptype,
                                                      String timelinetype, Date lastdate,
                                                      int pagesize,int ispublic){
        if (Constant.TIMELINE_IMPROVE_SQUARE.equals(timelinetype)){
            userid = Constant.SQUARE_USER_ID;
        }
        Criteria criteria = Criteria.where("userid").is(userid);
        if (Constant.TIMELINE_IMPROVE_SELF.equals(timelinetype)){
            criteria.and("ispublic").gte(ispublic);
        }
        if (!StringUtils.isEmpty(ptype) && !"-1".equals(ptype)){
            criteria.and("ptype").is(ptype);
        }
        if (null != lastdate) {
            criteria.and("createdate").lt(lastdate);
        }
        Query query = new Query(criteria);
        query.with(new Sort(Sort.Direction.DESC, "createdate"));
        query.limit(pagesize);
        List<TimeLine> timeLines = new ArrayList<>();
        switch (timelinetype){
            case Constant.TIMELINE_IMPROVE_SQUARE:
                timeLines = mongoTemplate.find(query,TimeLine.class,Constant.TIMELINE_IMPROVE_SQUARE_COLLECTION);
                break;
            case Constant.TIMELINE_IMPROVE_SELF:
                timeLines = mongoTemplate.find(query,TimeLine.class,Constant.TIMELINE_IMPROVE_SELF_COLLECTION);
                break;
            case Constant.TIMELINE_IMPROVE_ALL:
                timeLines = mongoTemplate.find(query,TimeLine.class,Constant.TIMELINE_IMPROVE_ALL_COLLECTION);
                break;
            case Constant.TIMELINE_IMPROVE_FRIEND:
                timeLines = mongoTemplate.find(query,TimeLine.class,Constant.TIMELINE_IMPROVE_FRIEND_COLLECTION);
                break;
            case Constant.TIMELINE_IMPROVE_ATTR:
                timeLines = mongoTemplate.find(query,TimeLine.class,Constant.TIMELINE_IMPROVE_ATTR_COLLECTION);
                break;
            case Constant.TIMELINE_IMPROVE_ACQ:
                timeLines = mongoTemplate.find(query,TimeLine.class,Constant.TIMELINE_IMPROVE_ACQ_COLLECTION);
                break;
            default:
                timeLines = mongoTemplate.find(query,TimeLine.class,Constant.TIMELINE_IMPROVE_SQUARE_COLLECTION);
                break;
        }
        return timeLines;
    }

    public void clearDirtyData(String timelinetype,String id){
        Query deletequery = Query.query(Criteria.where("_id").is(id));
        switch (timelinetype){
            case Constant.TIMELINE_IMPROVE_SQUARE:
                mongoTemplate.remove(deletequery, Constant.TIMELINE_IMPROVE_SQUARE_COLLECTION);
                break;
            case Constant.TIMELINE_IMPROVE_SELF:
                mongoTemplate.remove(deletequery, Constant.TIMELINE_IMPROVE_SELF_COLLECTION);
                break;
            case Constant.TIMELINE_IMPROVE_FRIEND:
                mongoTemplate.remove(deletequery, Constant.TIMELINE_IMPROVE_FRIEND_COLLECTION);
                break;
            case Constant.TIMELINE_IMPROVE_ATTR:
                mongoTemplate.remove(deletequery, Constant.TIMELINE_IMPROVE_ATTR_COLLECTION);
                break;
            default:
                break;
        }
    }
    
    /**
	 * @author yinxc
	 * 获取我的进步列表当天的时间线
	 * 2017年2月24日
	 * return_type
	 * TimeLineDao
	 */
    public List<TimeLine> selectTimeListByUserAndTypeDate(String userid, String timelinetype, Date searchDate, Date lastdate, int pagesize){
        if (Constant.TIMELINE_IMPROVE_SQUARE.equals(timelinetype)){
            userid = Constant.SQUARE_USER_ID;
        }
        Criteria criteria = Criteria.where("userid").is(userid);
        if (!StringUtils.isEmpty(searchDate)) {
            Date start = DateUtils.getDateStart(searchDate);
            if (null != lastdate) {
                criteria.and("createdate").gte(start).lt(lastdate);
            } else {
                Date end = DateUtils.getDateEnd(searchDate);
                criteria.and("createdate").gte(start).lte(end);
            }
        }
        Query query = new Query(criteria);
        query.with(new Sort(Sort.Direction.DESC, "createdate"));
        query.limit(pagesize);
        List<TimeLine> timeLines = mongoTemplate.find(query,TimeLine.class,Constant.TIMELINE_IMPROVE_SELF_COLLECTION);
        return timeLines;
    }

    public void disticntData(String collectionName){

        int start = 0;
        int delete = 0;
        int pagesize = 1;
        boolean isend = true;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null;
        try {
            startDate = simpleDateFormat.parse("2017-06-30 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        while (isend){
            try {

                Criteria qc = Criteria.where("createdate").lt(startDate);
                Query query = new Query(qc);
                query.with(new Sort(Sort.Direction.DESC, "createdate"));
                query.skip(start);
                query.limit(pagesize);
                TimeLine timeLine = mongoTemplate.findOne(query,TimeLine.class,collectionName);
                start++;
                if (null == timeLine){
                    return;
                }
                if ((timeLine.getCreatedate()).getTime()
                        < simpleDateFormat.parse("2017-06-23 00:00:00").getTime()){
                    return;
                }
                Criteria criteria = Criteria.where("_id").ne(timeLine.getId());
                if (null == timeLine.getTimeLineDetail()){
                    Criteria cd = Criteria.where("_id").is(timeLine.getId());
                    Query q = new Query(cd);
                    q.limit(1);
                    mongoTemplate.remove(q,collectionName);
                    delete++;
                    continue;
                }
                criteria.and("userid").is(timeLine.getUserid());
                criteria.and("timeLineDetail.$id").is(timeLine.getTimeLineDetail().getId());
                Query query1 = new Query(criteria);
                WriteResult w = mongoTemplate.remove(query1,collectionName);
                if (w.isUpdateOfExisting()){
                    delete++;
                }
                logger.info(collectionName+"-"+timeLine.getCreatedate()+"-num:"+start);
                logger.info(collectionName+"-del-num:"+delete);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



    public void distinctDetail(){
        int start = 0;
        int delete = 0;
        int pagesize = 1;
        boolean isend = true;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null;
        try {
            startDate = simpleDateFormat.parse("2017-06-30 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        while (isend){
            try {

                Criteria qc = Criteria.where("createdate").lt(startDate);
                Query query = new Query(qc);
                query.with(new Sort(Sort.Direction.DESC, "createdate"));
                query.skip(start);
                query.limit(pagesize);
                TimeLineDetail timeLine = mongoTemplate.findOne(query,TimeLineDetail.class);
                start++;
                if (null == timeLine){
                    return;
                }
                if ((timeLine.getCreatedate()).getTime()
                        < simpleDateFormat.parse("2017-06-23 00:00:00").getTime()){
                    return;
                }
                Criteria criteria = Criteria.where("_id").ne(timeLine.getId());
                criteria.and("improveId").is(timeLine.getImproveId());
                Query query1 = new Query(criteria);
                WriteResult w = mongoTemplate.remove(query1,TimeLineDetail.class);
                if (w.isUpdateOfExisting()){
                    delete++;
                }
                logger.info("timelinedetail-"+timeLine.getCreatedate()+"-num:"+start);
                logger.info("timelinedetail-del-num:"+delete);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void distinctAllData(){

        distinctDetail();

        disticntData(Constant.TIMELINE_IMPROVE_SQUARE_COLLECTION);
        disticntData(Constant.TIMELINE_IMPROVE_SELF_COLLECTION);
//        disticntData(Constant.TIMELINE_IMPROVE_ACQ_COLLECTION);
        disticntData(Constant.TIMELINE_IMPROVE_ALL_COLLECTION);
        disticntData(Constant.TIMELINE_IMPROVE_ATTR_COLLECTION);
        disticntData(Constant.TIMELINE_IMPROVE_FRIEND_COLLECTION);
    }



}

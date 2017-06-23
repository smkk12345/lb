package com.longbei.appservice.dao;/**
 * Created by luye on 2017/1/23.
 */

import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.dao.BaseMongoDao;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.entity.TimeLine;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

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

    public void save(TimeLine timeLine,String collectName){
        mongoTemplate.save(timeLine,collectName);
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

}

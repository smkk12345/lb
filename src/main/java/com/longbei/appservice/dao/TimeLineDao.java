package com.longbei.appservice.dao;/**
 * Created by luye on 2017/1/23.
 */

import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.dao.BaseMongoDao;
import com.longbei.appservice.entity.TimeLine;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

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

    public List<TimeLine> selectTimeListByUserAndType(String userid, String timelinetype, Date lastdate, int pagesize){
        if (Constant.TIMELINE_IMPROVE_SQUARE.equals(timelinetype)){
            userid = Constant.SQUARE_USER_ID;
        }
        Criteria criteria = Criteria.where("userid").is(userid).and("ctype").is(timelinetype);
        if (null != lastdate) {
            criteria.and("createdate").lt(lastdate);
        }
        Query query = new Query(criteria);
        query.with(new Sort(Sort.Direction.DESC, "createdate"));
        query.limit(pagesize);
        List<TimeLine> timeLines = super.find(query);
        return timeLines;
    }

}

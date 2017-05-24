package com.longbei.appservice.dao.mongo.dao;

import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.dao.BaseMongoDao;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.entity.ImproveLFD;
import com.longbei.appservice.entity.ImproveLFDDetail;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
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

    /**
     * 保存 赞 花 钻 明细
     * @param improveLFD
     * @author luye
     *  private String businesstype;
     *  private Long businessid;
     */
    public void saveImproveLfd(ImproveLFD improveLFD,String businessid,String businesstype){
        Criteria criteria = Criteria.where("impid").is(improveLFD.getImpid())
                .and("userid").is(improveLFD.getUserid())
                .and("opttype").is(improveLFD.getOpttype())
                .and("avatar").is(improveLFD.getAvatar());
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("createtime",improveLFD.getCreatetime());
        ImproveLFDDetail improveLFDDetail = new ImproveLFDDetail();
        improveLFDDetail.setUserid(improveLFD.getUserid());
        improveLFDDetail.setImpid(improveLFD.getImpid());
        improveLFDDetail.setOpttype(improveLFD.getOpttype());
        improveLFDDetail.setCreatetime(improveLFD.getCreatetime());
        improveLFDDetail.setBusinessid(businessid);
        improveLFDDetail.setBusinesstype(businesstype);
        mongoTemplate.save(improveLFDDetail);

        Criteria criteria1 = Criteria.where("impid").is(improveLFD.getImpid())
                .and("userid").is(improveLFD.getUserid());
        Query query1 = new Query(criteria1);

        ImproveLFD improveLFD1 = mongoTemplate.findOne(query1,ImproveLFD.class);
        if (null == improveLFD1){
            update.set("opttype",improveLFD.getOpttype());
        } else {
            if (Constant.MONGO_IMPROVE_LFD_OPT_LIKE.equals(improveLFD1.getOpttype())){
                update.set("opttype",improveLFD.getOpttype());
            }
        }

        mongoTemplate.upsert(query1,update,ImproveLFD.class);
        Criteria removecriteria = Criteria.where("impid").is(improveLFD.getImpid());
        Query removequery = new Query(removecriteria);
        Long count = mongoTemplate.count(removequery,ImproveLFD.class);
        if (count > Constant.DEFAULT_IMPROVE_ALL_DETAIL_LIST_NUM + 5){
            removequery.with(new Sort(Sort.Direction.DESC, "createtime"));
            removequery.skip(Constant.DEFAULT_IMPROVE_ALL_DETAIL_LIST_NUM + 5);
            List<ImproveLFD> list = mongoTemplate.find(removequery,ImproveLFD.class);
            if (null != list && list.size() != 0){
                for (ImproveLFD improveLFD2 : list){
                    Criteria cr = Criteria.where("id").is(improveLFD2.getId());
                    Query q = new Query(cr);
                    mongoTemplate.remove(q,ImproveLFD.class);
                }
            }
        }

    }

    /**
     * 删除 赞 花 钻明细
     * @param improveLFD
     * @author luye
     */
    public void removeImproveLfd(ImproveLFD improveLFD){
        Criteria criteria = Criteria.where("impid").is(improveLFD.getImpid())
                .and("userid").is(improveLFD.getUserid())
                .and("opttype").is(improveLFD.getOpttype());
        Query query = new Query(criteria);
        mongoTemplate.remove(query,ImproveLFD.class);
        mongoTemplate.remove(query,ImproveLFDDetail.class);
    }


    /**
     * 赞和花简略信息
     * @param impid
     * @return
     * @author luye
     */
    public List<ImproveLFD> selectImproveLfdList(String impid){
        Criteria criteria = Criteria.where("impid").is(impid);
        Query query = new Query(criteria);
        query.with(new Sort(Sort.Direction.DESC, "createtime"));
        query.limit(6);
        List<ImproveLFD> improveLFDs = mongoTemplate.find(query,ImproveLFD.class);
        return improveLFDs;
    }

    /**
     * 查询 赞，花，钻 总数
     * @param impid
     * @return
     * @author luye
     */
    public Long selectTotalCountImproveLFD(String impid){
        DBObject object = new BasicDBObject("impid",impid);
        return Long.parseLong(mongoTemplate.getCollection("improveLFDDetail").distinct("userid",object).size()+"");
//        return group.getRawResults().
////        Dist
//        Long count = mongoTemplate.count(query,ImproveLFDDetail.class);
////        return count;
    }

    /**
     * 统计进步的总花 赞 钻石
     * @param businessid
     * @param businesstype
     * @return
     */
    public Long selectTotal(String businessid,String businesstype,String otype){
        Criteria criteria = Criteria.where("businesstype").is(businesstype);
        if(!StringUtils.isBlank(businessid)){
            criteria.and("businessid").is(businessid);
        }
        criteria.and("opttype").is(otype);
        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query,ImproveLFDDetail.class);
        return count;
    }

    /**
     * 查询 赞数
     * @param impid
     * @param optype 0 赞 1 花  2 钻
     * @return
     * @author luye
     */
    public Long selectCountImproveLF(String impid,String optype){
        Criteria criteria = Criteria.where("impid").is(impid).and("opttype").is(optype);
        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query,ImproveLFDDetail.class);
        return count;
    }




    /**
     *  是否做过操作
     * @param impid 进步id
     * @param userid 用户id
     * @param opttype 操作类型  点赞，送花，送钻
     * @return
     * @author luye
     */
    public boolean exits(String impid,String userid,String opttype){
        Criteria criteria = Criteria.where("impid").is(impid)
                .and("userid").is(userid)
                .and("opttype").is(opttype);
        Query query = new Query(criteria);
        boolean isexits = mongoTemplate.exists(query,ImproveLFDDetail.class);
        return isexits;
    }






}

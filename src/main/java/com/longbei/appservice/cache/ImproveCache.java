package com.longbei.appservice.cache;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.constant.RedisCacheNames;
import com.longbei.appservice.dao.TimeLineDetailDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.entity.TimeLineDetail;
import com.longbei.appservice.service.ImproveService;
import com.longbei.appservice.service.UserRelationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 进步相关缓存
 *
 * @author luye
 * @create 2017-08-30 下午4:42
 **/
@Service
public class ImproveCache {

    private static Logger logger = LoggerFactory.getLogger(ImproveCache.class);

    @Autowired
    private TimeLineDetailDao timeLineDetailDao;
    @Autowired
    private UserRelationService userRelationService;
    @Autowired
    private ImproveService improveService;

    @Cacheable(cacheNames = RedisCacheNames._SYS_RECOMMAEND,
            key = "#startno +'&'+ #pagesize")
    public List<Improve> selectRecommendImproveList
            (Integer startno, Integer pagesize) throws Exception{
        List<Improve> improves = new ArrayList<>();
        try {
            List<TimeLineDetail> timeLineDetails = timeLineDetailDao.selectRecommendImproveList
                    (null,null,startno,pagesize);

            if (null != timeLineDetails && timeLineDetails.size() != 0){
                for (int i = 0 ; i < timeLineDetails.size() ; i++){
                    TimeLineDetail timeLineDetail = timeLineDetails.get(i);
                    Improve improve = new Improve();
                    improve.setImpid(timeLineDetail.getImproveId());
                    improve.setBrief(timeLineDetail.getBrief());
                    improve.setPickey(timeLineDetail.getPhotos());
                    improve.setFilekey(timeLineDetail.getFileKey());
                    improve.setSourcekey(timeLineDetail.getSourcekey());
                    improve.setBusinessid(timeLineDetail.getBusinessid());
                    improve.setBusinesstype(timeLineDetail.getBusinesstype());
                    improve.setItype(timeLineDetail.getItype());
                    improve.setCreatetime(timeLineDetail.getCreatedate());
//                    improve.setAppUserMongoEntity(timeLineDetail.getUser());
                    AppUserMongoEntity user = timeLineDetail.getUser();
                    improve.setAppUserMongoEntity(user);
                    //初始化 赞 花 数量
//                    initImproveLikeAndFlower(improve);
                    improve.setFlowers(timeLineDetail.getFlowers());
                    improve.setLikes(improveService.getLikeFromRedis(String.valueOf(improve.getImpid()),
                            String.valueOf(improve.getBusinessid()),improve.getBusinesstype()));
                    improve.setIspublic("2");
                    improves.add(improve);
                }
            }
        } catch (Exception e) {
            logger.error("select recommend list is error:",e);
            throw e;
        }
        return improves;
    }


}

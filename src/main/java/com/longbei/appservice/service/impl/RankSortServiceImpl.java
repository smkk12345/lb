package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.redis.SpringJedisDao;
import com.longbei.appservice.service.RankSortService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wangyongzhi 17/3/14.
 */
@Service("rankSortService")
public class RankSortServiceImpl extends BaseServiceImpl implements RankSortService {
    public static final double flowerWeight = 10;//送花 占的权重

    private Logger logger = LoggerFactory.getLogger(RankSortServiceImpl.class);

    @Autowired
    private SpringJedisDao springJedisDao;

    /**
     * 榜中点赞,送花时,更新用户在榜中的排名分值
     * @param rankId 榜id
     * @param userId 用户id
     * @param operationType 操作类型 点赞/送花/取消点赞
     * @param num 送花的数量,默认是1
     * @return
     */
    @Override
    public boolean updateRankSortScore(Long rankId, Long userId, Constant.OperationType operationType, Integer num) {
        try{
            if(rankId == null || userId == null || operationType == null){
                return false;
            }
            if(num == null){
                num = 1;
            }
            //给该用户加分值
            double score = 0.0;
            switch (operationType){
                case like:
                    score = 1.0;
                    break;
                case flower:
                    score = 1.0 * 10;
                    break;
                case cancleLike://取消赞
                    score = -1.0;
            }
            double newScore = springJedisDao.zIncrby(Constant.REDIS_RANK_SORT+rankId,userId+"",score);
            if(newScore > score){
                return true;
            }
        }catch(Exception e){
            logger.error("rank sort updateRankSortScore error rankId:{} userId:{} operationType:{} num:{}",rankId,userId,operationType,num);
            printException(e);
        }
        return false;
    }


}

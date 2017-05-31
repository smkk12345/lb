package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.RankMapper;
import com.longbei.appservice.dao.RankMembersMapper;
import com.longbei.appservice.dao.redis.SpringJedisDao;
import com.longbei.appservice.entity.Rank;
import com.longbei.appservice.entity.RankMembers;
import com.longbei.appservice.service.ImproveService;
import com.longbei.appservice.service.RankService;
import com.longbei.appservice.service.RankSortService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by wangyongzhi 17/3/14.
 */
@Service("rankSortService")
public class RankSortServiceImpl extends BaseServiceImpl implements RankSortService {
    public static final double flowerWeight = 10;//送花 占的权重

    private Logger logger = LoggerFactory.getLogger(RankSortServiceImpl.class);

    @Autowired
    private SpringJedisDao springJedisDao;
    @Autowired
    private RankMapper rankMapper;
    @Autowired
    private RankMembersMapper rankMembersMapper;
    @Autowired
    private RankService rankService;
    @Autowired
    private ImproveService improveService;

    /**
     * 榜中点赞,送花时,更新用户在榜中的排名分值
     * @param rankId 榜id
     * @param userId 用户id
     * @param operationType 操作类型 点赞/送花/取消点赞
     * @param num 送花的数量,默认是1 num可为负数,例如在榜中删除进步时,需要将该进步中的花影响的排名撤回,则需要将num写成负数
     * @return
     */
    @Override
    public boolean updateRankSortScore(Long rankId, Long userId, Constant.OperationType operationType, Integer num) {
        try{
            if(rankId == null || userId == null || operationType == null){
                return false;
            }
            Rank rank = this.rankMapper.selectRankByRankid(rankId);
            if(rank == null || !"1".equals(rank.getIsfinish())){
                return false;
            }
            if(num == null){
                num = 1;
            }
            //给该用户加分值
            double score = 0.0;
            switch (operationType){
                case like:
                    score = 1.0*rank.getLikescore();
                    break;
                case flower:
                    score = 1.0 * rank.getFlowerscore();
                    break;
                case cancleLike://取消赞
                    score = -1.0 * rank.getLikescore();
                    break;
            }
            score = score * num;
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

    /**
     * 查看榜单是否结束,如果已结束,则做发奖等操作
     * @param rank
     * @return
     */
    @Transactional
    @Override
    public boolean checkRankEnd(Rank rank) {
        try{
            if(rank == null){
                return false;
            }
            //如果时间已经到了,则往redis中放入一个标识 标识已结束,正在计算排名
            if(!"1".equals(rank.getIsfinish()) || ("1".equals(rank.getIsfinish()) && rank.getEndtime().getTime() > new Date().getTime())){
                return true;
            }
            //标识活动刚刚结束
            //1.校验redis中是否有活动结束标识
            String redisEndFlag = springJedisDao.get(Constant.REDIS_RANK_END+rank.getId());
            if(StringUtils.isNotEmpty(redisEndFlag)){
                return true;
            }
            springJedisDao.set(Constant.REDIS_RANK_END+rank.getId(),"end",4);

            //2.从redis中同步用户的排名
            Set<String> rankSortSet = this.springJedisDao.zRevrange(Constant.REDIS_RANK_SORT+rank.getRankid(),0,-1);
            int i = 1;
            Integer minImproveNum = Integer.parseInt(rank.getMinimprovenum());

            Map<String,Object> map =new HashMap<String,Object>();
            map.put("rankId",rank.getRankid());
            for(String userId:rankSortSet){
                map.put("userId",userId);
                map.put("sortNum",i);

                int row = this.rankMembersMapper.updateRank(map);
                i++;
            }

            //3.机审过滤未满足条件的榜单成员 修改机审状态为通过 机审条件,只审核是否满足总条数
            Map<String,Object> updateMap = new HashMap<String,Object>();
            updateMap.put("rankId",rank.getRankid());
            updateMap.put("minImproveNum",rank.getMinimprovenum());
            updateMap.put("status","1");
            updateMap.put("checkresult","未满足进步条数");
            updateMap.put("type","less");
            int updateRow = this.rankMembersMapper.instanceRankMember(updateMap);
            if("0".equals(rank.getIscheck())){//不需要人工审核
                updateMap.put("status","3");
                updateMap.put("type","greater");
                updateMap.remove("checkresult");
//                int awardcount = getRankAwardCount(String.valueOf(rankMembers.getRankid()));
                int updateRow1 = this.rankMembersMapper.instanceRankMember(updateMap);
            }

            //4.如果是不需要人工审核,则修改rankMember的中奖状态以及通知中奖用户 并将用户获得的什么奖插入imp_award
            if("0".equals(rank.getIscheck())){
                this.rankService.submitRankMemberCheckResult(rank,false,true);
                //发送通知消息给参榜的所有用户,发奖里面已经包含了发送系统通知,所以此处可以不用再次发送消息
//                this.rankService.rankEndNotice(rank);
            }

            //5.更改rank的活动标识为已结束
            Rank updateRank = new Rank();
            updateRank.setRankid(rank.getRankid());
            if("0".equals(rank.getIscheck())){//不需要人工审核
                updateRank.setIsfinish("5");
            }else{//需要人工审核
                int count = rankMembersMapper.selectpasscount(rank.getRankid());
                if (count == 0){
                    updateRank.setIsfinish("5");
                }else {
                    updateRank.setIsfinish("2");
                }
            }
            //缓存花，赞到快照中
            int updateRankMemberRow = rankMembersMapper.updateSortSource(rank.getRankid());

            updateRank.setIsrecommend("0");//榜单结束去掉推荐属性
            int updateRankRow = this.rankMapper.updateSymbolByRankId(updateRank);

            //6.从redis中删除该标识 以及从redis中删除该榜单的排名
            this.springJedisDao.del(Constant.REDIS_RANK_SORT+rank.getRankid());
            this.springJedisDao.del(Constant.REDIS_RANK_END+ rank.getRankid());

            return true;
        }catch (Exception e){
            logger.error("check rank end error rankId:{}",rank.getId());
            printExceptionAndRollBackTransaction(e);
        }
        return true;
    }


}

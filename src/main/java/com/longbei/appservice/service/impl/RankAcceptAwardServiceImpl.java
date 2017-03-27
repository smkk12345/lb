package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.dao.AwardMapper;
import com.longbei.appservice.dao.RankAcceptAwardMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.Award;
import com.longbei.appservice.entity.RankAcceptAward;
import com.longbei.appservice.service.RankAcceptAwardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 奖品领取
 *
 * @author luye
 * @create 2017-03-21 下午3:03
 **/
@Service
public class RankAcceptAwardServiceImpl implements RankAcceptAwardService{

    private static Logger logger = LoggerFactory.getLogger(RankAcceptAwardServiceImpl.class);


    @Autowired
    private RankAcceptAwardMapper rankAcceptAwardMapper;
    @Autowired
    private AwardMapper awardMapper;
    @Autowired
    private UserMongoDao userMongoDao;

    @Override
    public boolean insertAcceptAwardInfoBatch(List<RankAcceptAward> rankAcceptAwards) {
        int res = 0;
        try {
            res = rankAcceptAwardMapper.insertBatch(rankAcceptAwards);
        } catch (Exception e) {
            logger.error("insert batch rank accept award is error:",e);
        }
        return res > 0;
    }

    @Override
    public BaseResp<Page<RankAcceptAward>> selectRankAccepteAwardList(RankAcceptAward rankAcceptAward,
                                                                      Integer pageno, Integer pagesize) {

        BaseResp<Page<RankAcceptAward>> baseResp = new BaseResp<>();
        if (null == rankAcceptAward){
            return baseResp;
        }
        Page<RankAcceptAward> page = new Page<>(pageno,pagesize);

        try {
            if (!StringUtils.isEmpty(rankAcceptAward.getAwardtitle())){
                Award award = new Award();
                award.setAwardtitle(rankAcceptAward.getAwardtitle());
                List<Award> awards = awardMapper.selectAwardList(award,null,null);
                rankAcceptAward.setAwards(awards);
            }
            int totalcount = rankAcceptAwardMapper.selectCount(rankAcceptAward);
            List<RankAcceptAward> rankAcceptAwards = rankAcceptAwardMapper.selectList(rankAcceptAward,
                    pagesize*(pageno-1),pagesize);
            for (RankAcceptAward rankAcceptAward1 : rankAcceptAwards){
                rankAcceptAward1.setAppUserMongoEntity(userMongoDao.getAppUser
                        (String.valueOf(rankAcceptAward1.getUserid())));
            }
            page.setTotalCount(totalcount);
            page.setList(rankAcceptAwards);
            baseResp = BaseResp.ok();
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("select rank accept award list is error:",e);
        }
        return baseResp;
    }


    @Override
    public BaseResp<RankAcceptAward> selectRankAcceptAwardDetail(String rankid,String userid) {
        BaseResp<RankAcceptAward> baseResp = new BaseResp<>();
        try {
            RankAcceptAward acceptAward = rankAcceptAwardMapper.selectByRankIdAndUserid(rankid,
                    userid);
            if (null != acceptAward){
                Award award = awardMapper.selectByPrimaryKey(acceptAward.getAwardid());
                if (null != award){
                    acceptAward.setAwardtitle(award.getAwardtitle());
                }
            }
            baseResp = BaseResp.ok();
            baseResp.setData(acceptAward);
        } catch (Exception e) {
            logger.error("select rankacceptaward detail  rankid={} userid={} is error:",
                    rankid,userid,e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> updateRankAcceptAward(RankAcceptAward rankAcceptAward) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            int res = rankAcceptAwardMapper.updateByRankidAndUseridSelective(rankAcceptAward);
            if (res > 0){
                baseResp = BaseResp.ok();
            }
        } catch (Exception e) {
            logger.error("update rankacceptaward rankid={} userid={} is error:",
                    rankAcceptAward.getRankid(),rankAcceptAward.getUserid(),e);
        }
        return baseResp;
    }
}

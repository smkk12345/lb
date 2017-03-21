package com.longbei.appservice.service.impl;

import com.longbei.appservice.dao.RankAcceptAwardMapper;
import com.longbei.appservice.entity.RankAcceptAward;
import com.longbei.appservice.service.RankAcceptAwardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.dao.RankCardMapper;
import com.longbei.appservice.entity.RankCard;
import com.longbei.appservice.service.RankCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 榜主名片
 *
 * @author luye
 * @create 2017-03-02 上午10:13
 **/
@Service
public class RankCardServiceImpl implements RankCardService {

    private static Logger logger = LoggerFactory.getLogger(RankCardServiceImpl.class);

    @Autowired
    private RankCardMapper rankCardMapper;

    @Override
    public BaseResp insertRankAdmin(RankCard rankCard) {
        BaseResp baseResp = new BaseResp();
        try {
            rankCard.setCreatetime(new Date());
            rankCard.setUpdatetime(new Date());
            int res = rankCardMapper.insertSelective(rankCard);
            if (res > 0){
                baseResp = BaseResp.ok();
            }
        } catch (Exception e) {
            logger.error("insert rank admin card is error:",e);
        }
        return baseResp;
    }


    @Override
    public BaseResp updateRankAdmin(RankCard rankCard) {
        BaseResp baseResp = new BaseResp();
        if (null == rankCard){
            return baseResp;
        }
        try {
            rankCard.setUpdatetime(new Date());
            int res = rankCardMapper.updateByPrimaryKeySelective(rankCard);
            if (res > 0){
                baseResp = BaseResp.ok();
            }
        } catch (Exception e) {
            logger.error("update rank admin card cardid={} is error:",rankCard.getId(),e);
        }
        return baseResp;
    }


    @Override
    public BaseResp deleteRankAdmin(String rankadminid) {
        BaseResp baseResp = new BaseResp();
        if (org.springframework.util.StringUtils.isEmpty(rankadminid)){
            return baseResp;
        }
        try {
            int res = rankCardMapper.deleteByPrimaryKey(Integer.parseInt(rankadminid));
            if (res > 0){
                baseResp = BaseResp.ok();
            }
        } catch (Exception e) {
            logger.error("delete rank admin card cardid={} is error:",rankadminid,e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<RankCard> selectRankAdmin(String rankadminid) {
        BaseResp<RankCard> baseResp = new BaseResp();
        if (org.springframework.util.StringUtils.isEmpty(rankadminid)){
            return baseResp;
        }
        try {
            RankCard rankCard = rankCardMapper.selectByPrimaryKey(Integer.parseInt(rankadminid));
            baseResp = BaseResp.ok();
            baseResp.setData(rankCard);
        } catch (Exception e) {
            logger.error("select rank admin card cardid={} is error:",rankadminid,e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Page<RankCard>> selectRankAminListWithPage(RankCard rankCard, int pageno, int pagesize) {
        BaseResp<Page<RankCard>> baseResp = new BaseResp();
        Page<RankCard> page = new Page<>(pageno,pagesize);
        try {
            int totalcount = rankCardMapper.selectCount(rankCard);
            List<RankCard> rankCards = rankCardMapper.selectList(rankCard,pagesize*(pageno - 1),pagesize);
            page.setTotalCount(totalcount);
            page.setList(rankCards);
            baseResp = BaseResp.ok();
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("select rank card list with page is error:",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<List<RankCard>> selectRankAdminList(RankCard rankCard) {
        BaseResp<List<RankCard>> baseResp = new BaseResp();
        try {
            List<RankCard> rankCards = rankCardMapper.selectList(rankCard,null,null);
            baseResp = BaseResp.ok();
            baseResp.setData(rankCards);
        } catch (Exception e) {
            logger.error("select rank card list is error:",e);
        }
        return baseResp;
    }
}

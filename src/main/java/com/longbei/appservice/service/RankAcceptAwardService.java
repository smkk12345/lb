package com.longbei.appservice.service;

import com.longbei.appservice.entity.RankAcceptAward;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

/**
 * 奖品领取
 *
 * @author luye
 * @create 2017-03-21 下午3:00
 **/
public interface RankAcceptAwardService {


    /**
     * 批量添加领奖信息
     * @param rankAcceptAwards
     * @return
     */
    boolean insertAcceptAwardInfoBatch(List<RankAcceptAward> rankAcceptAwards);




}

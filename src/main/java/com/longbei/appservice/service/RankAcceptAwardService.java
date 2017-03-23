package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
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


    /**
     * 获取榜单领奖列表
     * @param rankAcceptAward
     * @param pageno
     * @param pagesize
     * @return
     */
    BaseResp<Page<RankAcceptAward>> selectRankAccepteAwardList(RankAcceptAward rankAcceptAward,Integer pageno,Integer pagesize);


    /**
     * 获取领奖详细信息
     * @return
     */
    BaseResp<RankAcceptAward> selectRankAcceptAwardDetail(String rankid,String userid);


    /**
     * 更新领奖信息 （收货信息，物流信息，对应状态等操作）
     * @param rankAcceptAward
     * @return
     */
    BaseResp<Object> updateRankAcceptAward(RankAcceptAward rankAcceptAward);




}

package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.RankAcceptAward;
import java.util.Date;
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
     * 获取榜单领奖列表数量
     * @param rankAcceptAward
     * @return
     */
    BaseResp<Integer> selectRankAcceptAwardListNum(RankAcceptAward rankAcceptAward);

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

    /**
     * 查询系统自动确认收货
     * @param currentDate
     * @return
     */
    List<RankAcceptAward> selectAutoConfirmReceiptRankAward(Date currentDate);

    /**
     * 更改rankAcceptAward的状态 自动确认收货
     * @param currentDate
     * @return
     */
    int updateRankAwardStatus(Date currentDate);

    /**
     * 查看用户的榜单获奖列表
     * @param userid
     * @param friendid 当前登录者id
     * @param startNum
     * @param pageSize
     * @return
     */
    BaseResp<Object> userRankAcceptAwardList(long userid, long friendid, Integer startNum, Integer pageSize);

    /**
     * 获取用户的榜单获奖总数
     * @param userid
     * @return
     */
    Integer userRankAcceptAwardCount(Long userid, String ispublic);
}

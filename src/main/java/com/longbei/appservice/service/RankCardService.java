package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.RankCard;

import java.util.List;

/**
 * 榜主名片操作接口
 *
 * @author luye
 * @create 2017-03-02 上午10:12
 **/
public interface RankCardService {


    /**
     * 添加榜主名片
     * @param rankCard
     * @return
     */
    BaseResp insertRankAdmin(RankCard rankCard);


    /**
     * 更新榜主名单
     * @param rankCard
     * @return
     */
    BaseResp updateRankAdmin(RankCard rankCard);

    /**
     * 删除榜主名片
     * @param rankadminid
     * @return
     */
    BaseResp deleteRankAdmin(String rankadminid);

    /**
     * 获取榜主名片详情
     * @param rankadminid
     * @return
     */
    BaseResp<RankCard> selectRankAdmin(String rankadminid);

    /**
     * 获取榜主名片列表（分页）
     * @param rankCard
     * @param pageno
     * @param pagesize
     * @return
     */
    BaseResp<Page<RankCard>> selectRankAminListWithPage(RankCard rankCard, int pageno, int pagesize);

    /**
     * 获取榜主名片列表
     * @param rankCard
     * @return
     */
    BaseResp<List<RankCard>> selectRankAdminList(RankCard rankCard);




}

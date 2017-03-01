package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.Rank;
import com.longbei.appservice.entity.RankCheckDetail;
import com.longbei.appservice.entity.RankImage;

import java.util.Date;
import java.util.List;

/**
 * 榜单操作接口
 *
 * @author luye
 * @create 2017-01-20 下午3:25
 **/
public interface RankService {

    /**
     * 添加榜单接口
     * @return
     */
    boolean insertRank(RankImage rankImage);


    /**
     * 编辑榜单
     * @return
     */
    boolean updateRankImage(RankImage rankImage);


    BaseResp<RankImage> selectRankImage(String rankimageid);


    BaseResp publishRankImage(String rankimageid);

    Page<RankImage> selectRankImageList(RankImage rankImage,int pageno, int pagesize);

    /**
     * 获取榜单列表 正式的
     * @param rank
     * @param pageno
     * @param pagesize
     * @return
     */
    Page<Rank> selectRankList(Rank rank, int pageno, int pagesize);

    boolean deleteRankImage(String rankimageid);


    BaseResp checkRankImage(RankCheckDetail rankCheckDetail);





}

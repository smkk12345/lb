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
public interface RankService extends BaseService{

    /**
     * 添加榜单接口
     * @return
     */
    boolean insertRank(RankImage rankImage);

    /**
     * 更新榜单 审核状态，发布专题，删除等操作
     * @param rankImage
     * @return
     */
    boolean updateRankSymbol(RankImage rankImage);

    /**
     * 编辑榜单
     * @return
     */
    boolean updateRankImage(RankImage rankImage);


    BaseResp<RankImage> selectRankImage(String rankimageid);


    BaseResp publishRankImage(String  rankImageid);

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


    /**
     * @Title: updateSponsornumAndSponsormoney
     * @Description: 更新赞助的统计人数和统计龙币数量
     * @param @param userid 赞助人
     * @param @param bid榜单
     * @auther IngaWu
     * @currentdate:2017年2月27日
     */
    boolean updateSponsornumAndSponsormoney(long rankid);

    BaseResp checkRankImage(RankCheckDetail rankCheckDetail);

    BaseResp<Object> selectRankByRankid(long rankid);

    Rank selectByRankid(long rankid);

    /**
     * 用户加入榜单
     * @param userId 用户id
     * @param rankId 榜单id
     * @param codeword 口令
     * @return
     */
    BaseResp<Object> insrtRankMember(Long userId, Long rankId, String codeword);
}

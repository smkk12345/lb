package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.Rank;
import com.longbei.appservice.entity.RankCheckDetail;
import com.longbei.appservice.entity.RankImage;
import com.longbei.appservice.entity.RankMembers;

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
     * @author luye
     */
    boolean insertRank(RankImage rankImage);

    /**
     * 更新榜单 审核状态，发布专题，删除等操作
     * @param rankImage
     * @return
     * @author luye
     */
    boolean updateRankImageSymbol(RankImage rankImage);

    /**
     * 更新榜单 结束 关闭
     * @param rank
     * @return
     * @author luye
     */
    boolean updateRankSymbol(Rank rank);


    /**
     * 编辑榜单
     * @return
     */
    boolean updateRankImage(RankImage rankImage);

    /**
     * 获取榜单详情 非线上
     * @param rankimageid
     * @return
     * @author luye
     */
    BaseResp<RankImage> selectRankImage(String rankimageid);

    /**
     * 发布榜单
     * @param rankImageid
     * @return
     * @author luye
     */
    BaseResp publishRankImage(String  rankImageid);

    /**
     * 获取非线上榜单列表
     * @param rankImage
     * @param pageno
     * @param pagesize
     * @return
     * @author luye
     */
    Page<RankImage> selectRankImageList(RankImage rankImage,int pageno, int pagesize);

    /**
     * 获取榜单列表 正式的
     * @param rank
     * @param pageno
     * @param pagesize
     * @return
     * @author luye
     */
    Page<Rank> selectRankList(Rank rank, int pageno, int pagesize);

    /**
     * 删除榜单 非线上
     * @param rankimageid
     * @return
     * @author luye
     */
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

    /**
     * 审核榜单
     * @param rankCheckDetail
     * @return
     * @author luye
     */
    BaseResp checkRankImage(RankCheckDetail rankCheckDetail);

    BaseResp<Object> selectRankByRankid(long rankid);

    Rank selectByRankid(long rankid);

    /**
     * 获取榜单详情 线上
     * @param rankid
     * @return
     * @author luye
     */
    BaseResp<Rank> selectRankDetailByRankid(String rankid);


    BaseResp<Page<RankMembers>> selectRankMemberList(RankMembers rankMembers,int pageNo,int pageSize);

}

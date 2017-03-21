package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
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
public interface RankService extends BaseService{

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
    Page<Rank> selectRankList(Rank rank, int pageno, int pagesize,Boolean showAward);

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
     * 用户加入榜单
     * @param userId 用户id
     * @param rankId 榜单id
     * @param codeword 口令
     * @return
     */
    BaseResp<Object> insertRankMember(Long userId, Long rankId, String codeword);

    /**
     * 获取榜单详情 线上
     * @param rankid
     * @param queryCreateUser 是否查询创建榜单用户的信息
     * @param queryAward 是否查询榜单奖品信息
     * @return
     * @author luye
     */
    BaseResp<Rank> selectRankDetailByRankid(String rankid,Boolean queryCreateUser,Boolean queryAward);

    /**
     * 获取成员列表 pc
     * @param rankMembers
     * @param pageNo
     * @param pageSize
     * @return
     */
    BaseResp<Page<RankMembers>> selectRankMemberList(RankMembers rankMembers,Integer pageNo,Integer pageSize);

    /**
     * 获取成员列表 待审核 pc
     * @param rankMembers
     * @param pageNo
     * @param pageSize
     * @return
     */
    BaseResp<Page<RankMembers>> selectRankMemberWaitCheckList(RankMembers rankMembers,Integer pageNo,Integer pageSize);



    /**
     * 获取榜单成员详细信息
     * @param rankid
     * @param userid
     * @return
     */
    BaseResp<RankMembers> selectRankMemberInfo(String rankid,String userid);

    /**
     * 获取成员列表 app
     * @param rankid
     * @param startNo
     * @param pageSize
     * @return
     */
    BaseResp<List<RankMembers>> selectRankMemberListForApp(String rankid,Integer startNo,Integer pageSize);

    /**
     * 退榜
     * @param userId
     * @param rankId
     * @return
     */
    BaseResp<Object> removeRankMember(Long userId, Long rankId);


    /**
     * 下榜，下榜再不能参加
     * @param rankMembers
     * @return
     * @author luye
     */
    BaseResp<Object> removeRankMember(RankMembers rankMembers);

    /**
     * 设置，取消达人
     * @param rankMembers
     * @return
     */
    BaseResp<Object> setIsfishionman(RankMembers rankMembers);

    /**
     * 更新榜单成员审核状态
     * @param rankMembers
     * @return
     */
    BaseResp<Object> updateRankMemberCheckStatus(RankMembers rankMembers);


    /**
     * 批量处理用户的参榜申请
     * @param userIds 用户id 数组
     * @param rankId 榜单id
     * @param status 要处理的结果
     * @return
     */
    BaseResp<Object> auditRankMember(Long[] userIds, Long rankId, Integer status);


    /**
     * 提价榜单成员审核结果
     * @param rankid
     * @return
     */
    BaseResp<Object> submitRankMemberCheckResultPreview(String rankid);

    /**
     * 改变榜单isfinish状态
     * @param rank
     * @return
     */
    BaseResp<Object> submitRankMemberCheckResult(Rank rank);

    /**
     * 获取成员列表 预览
     * @param rankMembers
     * @param pageNo
     * @param pageSize
     * @return
     */
    BaseResp<Page<RankMembers>> rankMemberCheckResultPreview(RankMembers rankMembers,Integer pageNo,Integer pageSize);
    /**
     * 查询用户在榜单中的排名
     * @param rankId 榜单id
     * @param userId 用户id
     * @return
     */
    BaseResp<Object> ownRankSort(Long rankId, Long userId);

    /**
     * 榜单的排名
     * @param rankId 榜单id
     * @param sortType 排序的类型
     * @param startNum 开始下标
     * @param pageSize 每页条数
     * @return
     */
    BaseResp<Object> rankMemberSort(Long rankId, Constant.SortType sortType, Integer startNum, Integer pageSize);

    /**
     * 获取榜中的达人
     * @param rankId 榜单id
     * @param startNum
     * @param pageSize
     * @return
     */
    BaseResp<Object> selectFashionMan(Long rankId, Integer startNum, Integer pageSize);

    /**
     * 用户领奖
     * @param userId 用户id
     * @param rankId 榜单id
     * @return
     */
    BaseResp<Object> acceptAward(Long userId, Long rankId);

    /**
     * 获取榜单的获奖公示
     * @param startNum
     * @param pageSize
     * @return
     */
    BaseResp<Object> rankAwardList(Integer startNum, Integer pageSize);

    /**
     * 获取榜单获奖详情
     * @param rankid
     * @return
     */
    BaseResp<Object> rankAwardDetail(Long rankid);

    /**
     * 通过各种条件 查询榜 列表
     * @param rankTitle
     * @param pType
     * @param rankscope
     * @param lastRankId
     * @param pageSize
     * @return
     */
    BaseResp<Object> selectRankListByCondition(String rankTitle, String pType, String rankscope, Long lastRankId, Integer pageSize,Boolean showAward);

    /**
     * 查询和自己相关的榜单
     * @param searchType 1.我参与的 2.我关注的 3.我创建的
     * @param startNum
     * @param pageSize
     * @return
     */
    BaseResp<Object> selectownRank(Long userId,Integer searchType, Integer startNum, Integer pageSize);

    /**
     * 查询榜单奖品列表
     * @param rankId
     * @return
     */
    BaseResp<Object> selectRankAward(Long rankId);
}

package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.entity.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    BaseResp<String> selectOwnRankIdsList(String userid);

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
     * 获取榜单列表 （带人数、评论数排序）
     * @param rank
     * @param pageno
     * @param pagesize
     * @return
     * @author IngaWu
     */
    Page<Rank> selectRankList2(Rank rank, int pageno, int pagesize,String orderByInvolved);
    /**
     * 获取榜单列表 推荐的 针对app
     * @param startNo
     * @param pageSize
     * @return
     * @author lixb
     */
    BaseResp<List<Rank>> selectRankListForApp(Integer startNo,Integer pageSize);

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
     * @param rankId
     * @param queryCreateUser 是否查询创建榜单用户的信息
     * @param queryAward 是否查询榜单奖品信息
     * @return
     * @author luye
     */
    BaseResp<Rank> selectRankDetailByRankid(Long userId,String rankId,Boolean queryCreateUser,Boolean queryAward);

    /**
     * 获取成员列表 pc
     * @param rankMembers
     * @param pageNo
     * @param pageSize
     * @return
     */
    BaseResp<Page<RankMembers>> selectRankMemberList(RankMembers rankMembers,Integer pageNo,Integer pageSize);

    /**
     * 获取所有榜中的成员
     * @param userInfo
     * @param pageno
     * @param pagesize
     * @return
     */
    BaseResp<Page<RankMembers>> selectRankAllMemberList(UserInfo userInfo,Integer pageno,Integer pagesize);


    /**
     * 查询榜单达人列表
     * @param rankId
     * @param pageno
     * @param pagesize
     * @return
     */
    BaseResp<Page<RankMembers>> selectRankFashionManList(String rankId,Integer pageno,Integer pagesize);

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
     * 榜中单条进步下榜或删除
     * @param status 0：未处理 1： 删除微进步    2： 下榜微进步  3： 通过其他方式已处理  4: 已忽略
     * @param userid
     * @param rankid
     * @param improveid
     * @return
     */
    BaseResp<Object> updateStatus(String status,String userid,String rankid,String improveid);

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
     * 关闭榜单
     * @param rankid
     * @return
     */
    BaseResp<Object> closeRank(String rankid);

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
     * @param isUpdateRank 是否需要更新榜单
     * @return
     */
    BaseResp<Object> submitRankMemberCheckResult(Rank rank,boolean isUpdateRank);

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
     * @param currentUserid 当前登录用户id
     * @return
     */
    BaseResp<Object> ownRankSort(Long rankId, Long userId,Long currentUserid);

    /**
     * 榜单的排名
     * @param rankId 榜单id
     * @param sortType 排序的类型
     * @param startNum 开始下标
     * @param pageSize 每页条数
     * @return
     */
    BaseResp<Object> rankMemberSort(Long userId,Long rankId, Integer sortType, Integer startNum, Integer pageSize);

    /**
     * 获取榜中的达人
     * @param rankId 榜单id
     * @param startNum
     * @param pageSize
     * @return
     */
    BaseResp<Object> selectFashionMan(Long userId,Long rankId, Integer startNum, Integer pageSize);

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
    BaseResp<Object> rankAwardList(Long userId,Integer startNum, Integer pageSize);

    /**
     * 查询单条榜单的获奖公示
     * @param rankid
     * @return
     */
    BaseResp<Object> onlyRankAward(Long rankid);

    /**
     * 获取榜单获奖详情
     * @param rankid
     * @return
     */
    BaseResp<Object> rankAwardDetail(Long rankid,Long userid);

    /**
     * 通过各种条件 查询榜 列表
     * @param rankTitle
     * @param pType
     * @param rankscope
     * @param lastDate
     * @param pageSize
     * @return
     */
    BaseResp<Object> selectRankListByCondition(Long userid,String rankTitle,String codeword, String pType, String rankscope,Integer status,
                                               String lastDate,Integer startNo, Integer pageSize,Boolean showAward);

    /**
     * 查询和自己相关的榜单
     * @param searchType 1.我参与的 2.我关注的 3.我创建的
     * @param startNum
     * @param pageSize
     * @return
     */
    BaseResp<Object> selectownRank(Long userId,Integer searchType,
                                   Integer startNum, Integer pageSize,String opttype);

    /**
     * 查询榜单奖品列表
     * @param rankId
     * @return
     */
    BaseResp<List<RankAwardRelease>> selectRankAward(Long rankId);

    /**
     * 获取领奖列表
     * @param rankMembers
     * @param pageno
     * @param pagesize
     * @return
     */
    BaseResp<Page<RankMembers>> selectRankAcceptAwardListPage(RankMembers rankMembers,Integer pageno,Integer pagesize);

    /**
     * 领取实物礼品
     * @param userId 用户id
     * @param rankId 榜单id
     * @param userAddressId 收货地址id
     * @return
     */
    BaseResp<Object> acceptRealAward(Long userId, Long rankId, Integer userAddressId);

    /**
     * 查看单个用户在榜中的信息
     * @param userid
     * @param currentUserId
     * @return
     */
    BaseResp<Object> selectRankMebmerDetail(Long userid,Long rankId, Long currentUserId);

    /**
     * 查询中奖的用户
     * @return
     */
    BaseResp<Object> selectWinningRankAward(Long userid);

    /**
     * 通知关注榜单的用户 榜单已开始
     * @param rank
     * @return
     */
    BaseResp<Object> noticeFollowRankUser(Rank rank);

    /**
     * 将开始的榜单状态置为已开始
     * @param currentDate
     * @return
     */
    BaseResp<Object> handleStartRank(Date currentDate);

    /**
     * 榜单奖品 自动确认收货
     * @param currentDate
     * @return
     */
    BaseResp<Object> rankAwardConfirmReceipt(Date currentDate);

    /**
     * 查询榜单地区
     * @return
     */
    BaseResp<Object> selectRankArea();

    /**
     * 用户手动确认收货
     * @param userid
     * @param rankId
     * @return
     */
    BaseResp<Object> userRankAwardConfirmReceipt(Long userid, Long rankId);

    /**
     * 结束榜单
     * @param currentTime
     * @return
     */
    BaseResp<Object> endRank(Date currentTime);

    /**
     * 榜单结束 通知用户榜结束 包含给中奖用户发消息 给未中奖用户发消息
     * 该接口将不再校验榜单是否已结束,所以调用该接口前请确认该榜单已结束
     * @param rankId
     * @return
     */
    BaseResp<Object> rankEndNotice(Long rankId);

    /**
     * 榜单结束 通知用户榜结束 包含给中奖用户发消息 给未中奖用户发消息
     * 该接口将不再校验榜单是否已结束,所以调用该接口前请确认该榜单已结束
     * @param rank
     * @return
     */
    BaseResp<Object> rankEndNotice(Rank rank);

    /**
     * 删除进步时,1.修改用户在榜中发布的进步总条数,
     *          2.删除用户获得的赞和花影响的排名
     * @param rankId
     * @param userId
     * @param likes
     * @param flowers
     * @return
     */
    BaseResp<Object> updateUserRankMemberByImprove(Long rankId,Long userId,int likes,int flowers);

    /**
     * 发布榜单
     * @param currentDateTime 系统当前时间
     * @return
     */
    BaseResp<Object> publishRank(Date currentDateTime);

    /**
     * 获取榜单获奖的用户
     * @param rankid 榜单id
     * @param startNum
     * @param pageSize
     * @return
     */
    BaseResp<Object> getWinningRankAwardUser(Long rankid,Long userid, Integer startNum, Integer pageSize);

    /**
     * 获取用户在榜中的排名
     * @param userId
     * @param businessId
     * @return
     */
    Map<String,Object> getUserSortNumAndImproveCount(Long userId, Long businessId);

    /**
     * 获取榜单审核记录
     * @param rankid
     * @return
     */
    BaseResp<List<RankCheckDetail>> selectRankCheckDetailList(String rankid);

    /**
     * 更改榜单的加榜验证 或 公告
     * @param rankId
     * @param userid 当前登录用户id
     * @param needConfirm 加榜是否需要验证 该参数不可与notice参数同事传入
     * @param notice 公告内容
     * @param noticeUser 更改公告是否需要通知用户
     * @return
     */
    BaseResp<Object> updateRankInfo(Long rankId, Long userid, Boolean needConfirm, String notice, Boolean noticeUser);

    BaseResp<Object> insertNotice(Rank rank,String noticetype);

    /**
     * 查询榜主名片详情
     * @param rankCardId
     * @return
     */
    BaseResp<Object> rankCardDetail(String rankCardId);


    /**
     * 获取榜单中进步数
     * @param rankid
     * @return
     */
    BaseResp<Integer> getRankImproveCount(String rankid);

}

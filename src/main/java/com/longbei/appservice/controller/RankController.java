package com.longbei.appservice.controller;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.entity.Rank;
import com.longbei.appservice.entity.RankAwardRelease;
import com.longbei.appservice.entity.RankImage;
import com.longbei.appservice.service.RankAcceptAwardService;
import com.longbei.appservice.service.RankService;
import com.longbei.appservice.service.RankSortService;
import com.longbei.appservice.service.UserBusinessConcernService;
import com.thoughtworks.xstream.io.binary.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 榜单操作
 *
 * @author luye
 * @create 2017-01-21 上午11:44
 **/
@RestController
@RequestMapping(value = "rank")
public class RankController {

    private static Logger logger = LoggerFactory.getLogger(RankController.class);

    @Autowired
    private RankService rankService;
    @Autowired
    private RankAcceptAwardService rankAcceptAwardService;
    @Autowired
    private UserBusinessConcernService userBusinessConcernService;
    /**
     * 用户 参榜
     * @url http://ip:port/app_service/rank/insertRankMember
     * @param userid 用户id
     * @param rankId 榜单id
     * @param codeword 口令
     * @return
     */
    @RequestMapping(value="insertRankMember")
    public BaseResp<Object> insertRankMember(Long userid,Long rankId,String codeword){
        logger.info("userid={},rankId={},codeword={}",userid,rankId,codeword);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(userid == null || rankId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.rankService.insertRankMember(userid,rankId,codeword);
        return baseResp;
    }

    /**
     * 退榜
     * @url http://ip:port/app_service/rank/removeRankMember
     * @param userid 用户id
     * @param rankId 榜单id
     * @return
     */
    @RequestMapping(value="removeRankMember")
    public BaseResp<Object> removeRankMember(Long userid,Long rankId){
        logger.info("userid={},rankId={}",userid,rankId);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userid == null || rankId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.rankService.removeRankMember(userid,rankId);

        return baseResp;
    }

    /**
     * 审核用户的参榜申请
     * @url http://ip:port/app_service/rank/auditRankMember
     * @param userIds 用户id 数组
     * @param rankId 榜单id
     * @param status 状态 1.同意 2.拒绝
     * @return
     */
    @RequestMapping(value="auditRankMember")
    public BaseResp<Object> auditRankMember(Long[] userIds,Long rankId,Integer status){
        logger.info("userIds={},rankId={},status={}", JSON.toJSONString(userIds),rankId,status);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(userIds == null || userIds.length < 1 || rankId == null || status == null || (status != 1 && status != 2)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.rankService.auditRankMember(userIds,rankId,status);
        return baseResp;
    }

    /**
     * 查询自己在榜单中的排名
     * @url http://ip:port/app_service/rank/ownRankSort
     * @param rankId
     * @param userid 查询榜单中用户排名的用户id
     * @param currentUserid 当前登录用户id
     * @return
     */
    @RequestMapping(value="ownRankSort")
    public BaseResp<Object> ownRankSort(Long rankId,Long userid,Long currentUserid){
        logger.info("rankId={},userid={}",rankId,userid);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(rankId == null || userid == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(Constant.VISITOR_UID.equals(userid+"")){
            return baseResp;
        }
        baseResp = this.rankService.ownRankSort(rankId,userid,currentUserid);

        return baseResp;
    }

    /**
     * 获取整个榜单的排名
     * @url http://ip:port/app_service/rank/rankMemberSort
     * @param userid 当前登录用户的id 如果当前用户未登录,可不传该参数
     * @param rankId 榜单id
     * @param sortType 排序的方式 0:综合排序 1:赞 2:花
     * @param startNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value="rankMemberSort")
    public BaseResp<Object> rankMemberSort(Long userid,Long rankId,Integer sortType,Integer startNum,Integer pageSize){
        logger.info("rankId={},sortType={},startNum={},pageSize={}",rankId,sortType,startNum,pageSize);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(rankId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(sortType == null){//默认综合排序
            sortType = 0;
        }
        if(startNum == null || startNum < 1){
            startNum = Integer.parseInt(Constant.DEFAULT_START_NO);
        }
        if(pageSize == null){
            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }
        baseResp = this.rankService.rankMemberSort(userid,rankId,sortType,startNum,pageSize);
        return baseResp;
    }

    /**
     * 获取龙榜列表
     * @url http://ip:port/app_service/rank/selectRankList
     * @param userid 当前登录用户id
     * @param rankTitle 榜单名称
     * @param pType 十全十美分类
     * @param rankscope 地区
     * @param status 状态筛选 0.推荐 1.进行中 2.将开始 3.已结束
     * @param lastDate 最后一个榜单的时间 lastDate是当status != 0时,需要传该参数
     * @param searchByCodeword 根据入榜口令搜索榜单 可不传 传入的值是0/1 1代表按照榜口令搜索榜 0代表按照榜名称搜索榜
     * @param startNum 开始的下标 startNo是当status == 0时,需要传该参数
     * @param pageSize
     * @return
     */
    @RequestMapping(value="selectRankList")
    public BaseResp<Object> selectRankList(Long userid,String rankTitle,String pType,String rankscope,Integer status,String lastDate,String searchByCodeword,Integer startNum,Integer pageSize){
        logger.info("userid={},rankTitle={},pType={},rankscope={},status={},lastDate={},searchByCodeword={},startNum={},pageSize={}",userid,rankTitle,pType,rankscope,status,lastDate,searchByCodeword,startNum,pageSize);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(startNum == null || startNum < 0){
            startNum = Integer.parseInt(Constant.DEFAULT_START_NO);
        }
        if(pageSize == null || pageSize < 0){
            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }
        if(status == null){
            status = -1;
        }
        String codeword = null;
        if(StringUtils.isNotEmpty(searchByCodeword) && "1".equals(searchByCodeword)){
            codeword = rankTitle;
            rankTitle = null;
        }
        baseResp = this.rankService.selectRankListByCondition(userid,rankTitle,codeword,pType,rankscope,status,lastDate,startNum,pageSize,true);
        baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        return baseResp;
    }

    /**
     * 获取用户的榜单id列表
     * http://ip:port/app_service/rank/selectOwnRankList
     * @param userid
     * @return
     */
    @RequestMapping(value="selectOwnRankList")
    public BaseResp<String> selectOwnRankIdsList(String userid){
        logger.info("userid={}",userid);
        BaseResp<String> baseResp = new BaseResp<String>();
        if(StringUtils.isBlank(userid)){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.rankService.selectOwnRankIdsList(userid);
        baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        return baseResp;
    }
    /**
     * 获取榜单详情
     * @url http://ip:port/app_service/rank/rankDetail
     * @param rankId
     * @return
     */
    @RequestMapping(value="rankDetail")
    public BaseResp<Rank> rankDetail(Long userid,String rankId){
        logger.info("userid={},rankId={}",userid,rankId);
        BaseResp<Rank> baseResp = new BaseResp<Rank>();
        if(StringUtils.isEmpty(rankId) || userid == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }

        baseResp = this.rankService.selectRankDetailByRankid(userid,rankId,true,true);
        if(ResultUtil.isSuccess(baseResp)){
            baseResp.getExpandData().put("shareurl", AppserviceConfig.h5_share_rank_detail);
        }
        return baseResp;
    }

    /**
     * 获取榜单的奖品列表
     * @url http://ip:port/app_service/rank/selectRankAward
     * @param rankId
     * @return
     */
    @RequestMapping(value="selectRankAward")
    public BaseResp<List<RankAwardRelease>> selectRankAward(Long rankId){
        logger.info("rankId={}",rankId);
        BaseResp<List<RankAwardRelease>> baseResp = new BaseResp<List<RankAwardRelease>>();
        if(rankId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.rankService.selectRankAward(rankId);
        return baseResp;
    }

    /**
     * 查询榜单中的达人
     * @url http://ip:port/app_service/rank/selectFashionMan
     * @param rankId 榜单id
     * @param startNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value="selectFashionMan")
    public BaseResp<Object> selectFashionMan(Long userid,Long rankId,Integer startNum,Integer pageSize){
        logger.info("userid={},rankId={},startNum={},pageSize={}",userid,rankId,startNum,pageSize);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(rankId == null || userid == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if (startNum == null || startNum < 0){
            startNum =Integer.parseInt(Constant.DEFAULT_START_NO);
        }
        if(null == pageSize){
            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }

        baseResp = this.rankService.selectFashionMan(userid,rankId,startNum,pageSize);
        return baseResp;
    }

    /**
     * 用户领奖
     * @url http://ip:port/app_service/rank/acceptAward
     * @param userid 用户id
     * @param rankId 榜单id
     * @return
     */
    @RequestMapping(value="acceptAward")
    public BaseResp<Object> acceptAward(Long userid,Long rankId){
        logger.info("userid={},rankId={}",userid,rankId);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userid == null || rankId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.rankService.acceptAward(userid,rankId);
        return baseResp;
    }

    /**
     * 领取实物奖品
     * @url http://ip:port/app_service/acceptRealAard
     * @param userid 用户id
     * @param rankId 榜单id
     * @param addressId 收货地址id
     * @return
     */
    @RequestMapping(value="acceptRealAard")
    public BaseResp<Object> acceptRealAard(Long userid,Long rankId,Integer addressId){
        logger.info("userid={},rankId={},addressId={}",userid,rankId,addressId);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userid == null || rankId == null || addressId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp= this.rankService.acceptRealAward(userid,rankId,addressId);
        return baseResp;
    }

    /**
     * 实物领奖 用户手动确认收货
     * @url http://ip:port/app_service/rankAwardConfirmReceipt
     * @param userid 用户id
     * @param rankId 榜单id
     * @return
     */
    @RequestMapping(value="rankAwardConfirmReceipt")
    public BaseResp<Object> rankAwardConfirmReceipt(Long userid,Long rankId){
        logger.info("userid={},rankId={}",userid,rankId);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userid == null || rankId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.rankService.userRankAwardConfirmReceipt(userid,rankId);
        return baseResp;
    }

    /**
     * 查询获奖公示
     * @url http://ip:port/app_service/rank/rankAwardList
     * @param userid 当前登录用户id 如果未登录,可不传该参数
     * @param startNum 开始下标
     * @param pageSize 结束下标
     * @return
     */
    @RequestMapping(value="rankAwardList")
    public BaseResp<Object> rankAwardList(Long userid,Integer startNum,Integer pageSize){
        logger.info("startNum={},pageSize={}",startNum,pageSize);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(startNum == null || startNum < 0){
            startNum = Integer.parseInt(Constant.DEFAULT_START_NO);
        }
        if(null == pageSize){
            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }
        baseResp = this.rankService.rankAwardList(userid,startNum,pageSize);
        return baseResp;
    }

    /**
     * 查询单个榜单的获奖公示
     * @url http://ip:port/app_service/rank/onlyRankAward
     * @param rankid
     * @return
     */
    @RequestMapping(value="onlyRankAward")
    public BaseResp<Object> onlyRankAward(Long rankid){
        logger.info("rankid={}",rankid);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(rankid == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.rankService.onlyRankAward(rankid);
        return baseResp;
    }

    /**
     * 获取榜单的中奖用户列表
     * @url http://ip:port/app_service/rank/getWinningRankAwardUser
     * @param rankid
     * @param userid 用户id
     * @param startNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value="getWinningRankAwardUser")
    public BaseResp<Object> getWinningRankAwardUser(Long rankid,Long userid,Integer startNum,Integer pageSize){
        logger.info("rankid={},userid={},startNum={},pageSize={}",rankid,userid,startNum,pageSize);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(rankid == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        startNum = (startNum == null || startNum < 0)?Integer.parseInt(Constant.DEFAULT_START_NO):startNum;
        if(null == pageSize){
            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }
        baseResp = this.rankService.getWinningRankAwardUser(rankid,userid,startNum,pageSize);
        return baseResp;
    }

    /**
     * 查看榜单获奖详情
     * @url http://ip:port/app_service/rank/rankAwardDetail
     * @param rankid
     * @return
     */
    @RequestMapping(value="rankAwardDetail")
    public BaseResp<Object> rankAwardDetail(Long rankid,Long userid){
        logger.info("rankid={},userid={}",rankid,userid);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(rankid == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.rankService.rankAwardDetail(rankid,userid);
        if(ResultUtil.isSuccess(baseResp)){
            baseResp.getExpandData().put("shareurl",AppserviceConfig.h5_share_rank_award);
        }
        return baseResp;
    }

    /**
     * 查询我的龙榜列表
     * @url http://ip:port/app_service/rank/selectOwnRank
     * @param userid
     * @param searchType 1.我参与的 2.我关注的 3.我创建的
     * @param opttype 可以不传，如果要获取我参见的未结束的榜请传 1
     * @param startNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value="selectOwnRank")
    public BaseResp<Object> selectOwnRank(Long userid,Integer searchType,String opttype,
                                          Integer startNum,Integer pageSize){
        logger.info("userid={},searchType={},startNum={},pageSize={}",userid,searchType,startNum,pageSize);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userid == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(searchType == null){
            searchType = 1;
        }
        if(startNum == null || startNum < 0){
            startNum = Integer.parseInt(Constant.DEFAULT_START_NO);
        }
        if(null == pageSize){
            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }
        baseResp = this.rankService.selectownRank(userid,searchType,startNum,pageSize,opttype);
        return baseResp;
    }

    /**
     * 添加关注
     * @url http://ip:port/app_service/rank/insertUserBusinessConcern
     * @param userid 用户id
     * @param businessType 关注的类型1 目标 2 榜单 3 圈子 4 教室
     * @param businessId 关注的类型id
     * @return
     */
    @RequestMapping(value="insertUserBusinessConcern")
    public BaseResp<Object> insertUserBusinessConcern(Long userid,Integer businessType,Long businessId){
        logger.info("userid={},businessType={},businessId={}",userid,businessType,businessId);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userid == null || businessType == null || businessId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.userBusinessConcernService.insertUserBusinessConcern(userid,businessType,businessId);
        return baseResp;
    }

    /**
     * 取消关注
     * @url http://ip:port/app_service/rank/deleteUserBusinessConcern
     * @param userid
     * @param businessType 关注的类型 1 目标 2 榜单 3 圈子 4 教室
     * @param businessId 关注的类型id
     * @return
     */
    @RequestMapping(value="deleteUserBusinessConcern")
    public BaseResp<Object> deleteUserBusinessConcern(Long userid,Integer businessType,Long businessId){
        logger.info("userid={},businessType={},businessId={}",userid,businessType,businessId);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userid == null || businessType == null || businessId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.userBusinessConcernService.deleteUserBusinessConcern(userid,businessType,businessId);
        return baseResp;
    }

    /**
     * 查看单个用户在榜中的信息
     * @url http://ip:port/app_service/rank/selectRankMemberDetail
     * @param userid 用户id
     * @param rankId 榜单id
     * @param currentUserId 当前登录用户id
     * @return
     */
    @RequestMapping(value="selectRankMemberDetail")
    public BaseResp<Object> selectRankMemberDetail(Long userid,Long rankId,Long currentUserId){
        logger.info("userid={},rankId={},currentUserId={}",userid,rankId,currentUserId);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userid == null || currentUserId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.rankService.selectRankMebmerDetail(userid,rankId,currentUserId);
        if(ResultUtil.isSuccess(baseResp)){
            baseResp.getExpandData().put("shareurl",AppserviceConfig.h5_share_rank_improve);
        }
        return baseResp;
    }

    /**
     * 查询中奖的用户和奖项
     * @url http://ip:port/app_service/rank/selectWinningRankAward
     * @param userid 当前登录用户的id 如果用户未登录,该参数可不传
     * @return
     */
    @RequestMapping(value="selectWinningRankAward")
    public BaseResp<Object> selectWinningRankAward(Long userid){
        BaseResp<Object> baseResp = this.rankService.selectWinningRankAward(userid);
        return baseResp;
    }

    /**
     * 查询榜单的地区
     * @url http://ip:port/app_service/rank/selectRankArea
     * @return
     */
    @RequestMapping(value="selectRankArea")
    public BaseResp<Object> selectRankArea(){
        return this.rankService.selectRankArea();
    }

//    /**
//     * url: http://ip:port/app_service/rank/insert
//     * method:  POST
//     * 添加榜单
//     * @param ranktitle 榜单标题
//     * @param rankdetail 榜单简介
//     * @param ranklimite  榜单限制人数
//     * @param rankscope 榜单范围
//     * @param rankphotos 榜单图片
//     * @param rankrate  榜单中奖率
//     * @param starttime 开始时间
//     * @param endtime  结束时间
//     * @param areaname 地域名字
//     * @param createuserid 创建人id
//     * @param ranktype 榜单类型
//     * @param ispublic 是否公开
//     * @param rankcateid 榜单类型
//     * @param likescore 赞的分数
//     * @param flowerscore 花的分数
//     * @param diamondscore 砖石的分数
//     * @param codeword  入榜口令
//     * @param ptype  十全十美类型
//     * @param sourcetype  来源类型
//     * @param companyname  公司名字
//     * @param companyphotos 公司图片
//     * @param companybrief 公司简介
//     * @return
//     * @author:luye
//     * @date: 2017/2/4
//     */
//    @ResponseBody
//    @RequestMapping(value = "insert",method = RequestMethod.POST)
//    public BaseResp<Object> insertRank(RankImage rankImage){
//        boolean issuccess = false;
//        try {
//            issuccess = rankService.insertRank(rankImage);
//            if(issuccess){
//                return BaseResp.ok(Constant.RTNINFO_SYS_50);
//            }
//        } catch (Exception e) {
//            logger.error("insert rank is error:{}",e);
//        }
//        return BaseResp.fail(Constant.RTNINFO_SYS_51);
//    }

    /**
     * url: http://ip:port/app_service/rank/selectRankListForApp
     * @ 首页推荐的龙榜列表
     * @param startNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "selectRankListForApp")
    public BaseResp<List<Rank>> selectRankListForApp(Integer startNum, Integer pageSize){
        logger.info("startNum={},pageSize={}",startNum,pageSize);
        BaseResp<List<Rank>> baseResp = new BaseResp<>();
        if(startNum == null || startNum < 0){
            startNum = Integer.parseInt(Constant.DEFAULT_START_NO);
        }
        if(null == pageSize){
            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }

        try {
            baseResp = rankService.selectRankListForApp(startNum,pageSize);
        } catch (Exception e) {
            logger.error("select rank list for adminservice is error:",e);
        }
        return baseResp;
    }

    /**
     * 查询用户榜单获奖列表
     * @url http://ip:port/app_service/rank/userRankAcceptAwardList
     * @param userid 用户id
     * @param startNum
     */
    @RequestMapping(value="userRankAcceptAwardList")
    public BaseResp<Object> userRankAcceptAwardList(Long userid,Integer startNum,Integer pageSize){
        logger.info("userid={},startNum={},pageSize={}",userid,startNum,pageSize);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userid == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        startNum = (startNum == null|| startNum < 0)?Integer.parseInt(Constant.DEFAULT_START_NO):startNum;
        if(null == pageSize){
             pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }
        baseResp = this.rankAcceptAwardService.userRankAcceptAwardList(userid,startNum,pageSize);
        return baseResp;
    }

    /**
     *
     * 更改榜单的加榜验证 或 公告
     * @param rankId
     * @param userid 当前登录用户id
     * @param needConfirm 加榜是否需要验证 该参数不可与notice参数同事传入
     * @param notice 公告内容
     * @param noticeUser 更改公告是否需要通知用户
     * @return
     */
    @RequestMapping(value="updateRankInfo")
    public BaseResp<Object> updateRankInfo(Long rankId,Long userid,Boolean needConfirm,String notice,Boolean noticeUser){
        logger.info("rankId={},userid={},needConfirm={},notice={},noticeUser={}",rankId,userid,needConfirm,notice,noticeUser);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(rankId == null || userid == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.rankService.updateRankInfo(rankId,userid,needConfirm,notice,noticeUser);
        return baseResp;
    }

}

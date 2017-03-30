package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.Rank;
import com.longbei.appservice.entity.RankImage;
import com.longbei.appservice.service.RankService;
import com.longbei.appservice.service.RankSortService;
import com.longbei.appservice.service.UserBusinessConcernService;
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
    private RankSortService rankSortService;
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
     * @param userid
     * @return
     */
    @RequestMapping(value="ownRankSort")
    public BaseResp<Object> ownRankSort(Long rankId,Long userid){
        BaseResp<Object> baseResp = new BaseResp<>();
        if(rankId == null || userid == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.rankService.ownRankSort(rankId,userid);

        return baseResp;
    }

    /**
     * 获取整个榜单的排名
     * @url http://ip:port/app_service/rank/rankMemberSort
     * @param rankId 榜单id
     * @param sortType 排序的方式 0:综合排序 1:赞 2:花
     * @param startNum
     * @param endNum
     * @return
     */
    @RequestMapping(value="rankMemberSort")
    public BaseResp<Object> rankMemberSort(Long rankId,Integer sortType,Integer startNum,Integer endNum){
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
        Integer pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        if(endNum != null && endNum > startNum){
            pageSize = endNum - startNum;
        }
        baseResp = this.rankService.rankMemberSort(rankId,sortType,startNum,pageSize);
        return baseResp;
    }

    /**
     * 获取龙榜列表
     * @url http://ip:port/app_service/rank/selectRankList
     * @param rankTitle 榜单名称
     * @param pType 十全十美分类
     * @param rankscope 地区
     * @param status 状态筛选 0.推荐 1.进行中 2.将开始 3.已结束
     * @param lastRankId 最后一个榜单id
     * @param pageSize
     * @return
     */
    @RequestMapping(value="selectRankList")
    public BaseResp<Object> selectRankList(String rankTitle,String pType,String rankscope,Integer status,Long lastRankId,Integer pageSize){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(pageSize == null || pageSize < 0){
            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }
        if(status == null){
            status = 0;
        }
        baseResp = this.rankService.selectRankListByCondition(rankTitle,pType,rankscope,status,lastRankId,pageSize,true);
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
        BaseResp<Rank> baseResp = new BaseResp<Rank>();
        if(StringUtils.isEmpty(rankId) || userid == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }

        baseResp = this.rankService.selectRankDetailByRankid(userid,rankId,true,true);
        return baseResp;
    }

    /**
     * 获取榜单的奖品列表
     * @url http://ip:port/app_service/rank/selectRankAward
     * @param rankId
     * @return
     */
    @RequestMapping(value="selectRankAward")
    public BaseResp<Object> selectRankAward(Long rankId){
        BaseResp<Object> baseResp = new BaseResp<Object>();
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
     * @param endNum
     * @return
     */
    @RequestMapping(value="selectFashionMan")
    public BaseResp<Object> selectFashionMan(Long userid,Long rankId,Integer startNum,Integer endNum){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(rankId == null || userid == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if (startNum == null || startNum < 0){
            startNum =Integer.parseInt(Constant.DEFAULT_START_NO);
        }
        Integer pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        if(endNum != null && endNum > startNum){
            pageSize = endNum - startNum;
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
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userid == null || rankId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.rankService.acceptAward(userid,rankId);
        return baseResp;
    }

    /**
     * 领取实物奖品
     * @param userid 用户id
     * @param rankId 榜单id
     * @param addressId 收货地址id
     * @return
     */
    @RequestMapping(value="acceptRealAard")
    public BaseResp<Object> acceptRealAard(Long userid,Long rankId,Integer addressId){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userid == null || rankId == null || addressId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp= this.rankService.acceptRealAward(userid,rankId,addressId);
        return baseResp;
    }

    /**
     * 查询获奖公示
     * @url http://ip:port/app_service/rank/rankAwardList
     * @param startNum 开始下标
     * @param endNum 结束下标
     * @return
     */
    @RequestMapping(value="rankAwardList")
    public BaseResp<Object> rankAwardList(Integer startNum,Integer endNum){
        BaseResp<Object> baseResp = new BaseResp<>();
        if(startNum == null || startNum < 0){
            startNum = Integer.parseInt(Constant.DEFAULT_START_NO);
        }
        Integer pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        if(endNum != null && endNum > startNum){
            pageSize = endNum - startNum;
        }
        baseResp = this.rankService.rankAwardList(startNum,pageSize);
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
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(rankid == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.rankService.rankAwardDetail(rankid,userid);
        return baseResp;
    }

    /**
     * 查询我的龙榜列表
     * @url http://ip:port/app_service/rank/selectOwnRank
     * @param userid
     * @param searchType 1.我参与的 2.我关注的 3.我创建的
     * @param startNum
     * @param endNum
     * @return
     */
    @RequestMapping(value="selectOwnRank")
    public BaseResp<Object> selectOwnRank(Long userid,Integer searchType,Integer startNum,Integer endNum){
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
        Integer pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        if(endNum != null && endNum > startNum){
            pageSize = endNum - startNum;
        }
        baseResp = this.rankService.selectownRank(userid,searchType,startNum,pageSize);
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
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userid == null || currentUserId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.rankService.selectRankMebmerDetail(userid,rankId,currentUserId);
        return baseResp;
    }

    /**
     * 查询中奖的用户和奖项
     * @url @url http://ip:port/app_service/rank/selectWinningRankAward
     * @return
     */
    @RequestMapping(value="selectWinningRankAward")
    public BaseResp<Object> selectWinningRankAward(){
        BaseResp<Object> baseResp = this.rankService.selectWinningRankAward();
        return baseResp;
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
     * url: http://ip:port/app_service/rank/selectRankList
     * @ 首页推荐的龙榜列表
     * @param pageno
     * @param pagesize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "selectRankListForApp")
    public BaseResp<List<Rank>> selectRankListForApp(String pageno, String pagesize){
        BaseResp<List<Rank>> baseResp = new BaseResp<>();
        if(StringUtils.isBlank(pageno)){
            pageno = Constant.DEFAULT_START_NO;
        }
        if(StringUtils.isBlank(pagesize)){
            pagesize = Constant.DEFAULT_PAGE_SIZE;
        }
        try {
            Rank r = new Rank();
            r.setIsrecommend("1");
            baseResp = rankService.selectRankListForApp(r,Integer.parseInt(pageno),Integer.parseInt(pagesize),true);
        } catch (Exception e) {
            logger.error("select rank list for adminservice is error:",e);
        }
        return baseResp;
    }


}

package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.entity.Rank;
import com.longbei.appservice.entity.RankImage;
import com.longbei.appservice.service.RankService;
import com.longbei.appservice.service.RankSortService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 用户 参榜
     * @url http://ip:port/app_service/rank/insertRankMember
     * @param userId 用户id
     * @param rankId 榜单id
     * @param codeword 口令
     * @return
     */
    @RequestMapping(value="insertRankMember")
    public BaseResp<Object> insertRankMember(Long userId,Long rankId,String codeword){
        BaseResp<Object> baseResp = new BaseResp<>();
        if(userId == null || rankId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.rankService.insertRankMember(userId,rankId,codeword);
        return baseResp;
    }

    /**
     * 退榜
     * @url http://ip:port/app_service/rank/removeRankMember
     * @param userId 用户id
     * @param rankId 榜单id
     * @return
     */
    @RequestMapping(value="removeRankMember")
    public BaseResp<Object> removeRankMember(Long userId,Long rankId){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userId == null || rankId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.rankService.removeRankMember(userId,rankId);

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
     * @param userId
     * @return
     */
    @RequestMapping(value="ownRankSort")
    public BaseResp<Object> ownRankSort(Long rankId,Long userId){
        BaseResp<Object> baseResp = new BaseResp<>();
        if(rankId == null || userId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.rankService.ownRankSort(rankId,userId);

        return baseResp;
    }

    /**
     * 获取整个榜单的排名
     * @param rankId 榜单id
     * @param sortType 排序的方式 comprehensive:综合排序 likes:赞 flower:花
     * @param startNum
     * @param endNum
     * @return
     */
    @RequestMapping(value="rankMemberSort")
    public BaseResp<Object> rankMemberSort(Long rankId,Constant.SortType sortType,Integer startNum,Integer endNum){
        BaseResp<Object> baseResp = new BaseResp<>();
        if(rankId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(sortType == null){//默认综合排序
            sortType = Constant.SortType.comprehensive;
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


}

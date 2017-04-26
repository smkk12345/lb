package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.ImpAllDetail;
import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.entity.Rank;
import com.longbei.appservice.service.CommentMongoService;
import com.longbei.appservice.service.ImproveService;
import com.longbei.appservice.service.RankService;
import com.netflix.discovery.converters.Auto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangyongzhi 17/4/19.
 */
@RestController
@RequestMapping(value = "api/rankShare",produces = "application/json")
public class RankShareController {

    private Logger logger = LoggerFactory.getLogger(RankShareController.class);

    @Autowired
    private RankService rankService;
    @Autowired
    private ImproveService improveService;
    @Autowired
    private CommentMongoService commentMongoService;

    /**
     * 获取榜单详情
     * @url http://ip:port/app_service/api/rankShare/rankDetail
     * @param rankId
     * @return
     */
    @RequestMapping(value="rankDetail")
    public BaseResp<Rank> rankDetail(String rankId){
        BaseResp<Rank> baseResp = new BaseResp<Rank>();
        if(StringUtils.isEmpty(rankId)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }

        baseResp = this.rankService.selectRankDetailByRankid(null,rankId,true,true);
        return baseResp;
    }

    /**
     * 查询榜单中的达人
     * @url http://ip:port/app_service/api/rankShare/selectFashionMan
     * @param rankId 榜单id
     * @return
     */
    @RequestMapping(value="selectFashionMan")
    public BaseResp<Object> selectFashionMan(Long rankId){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(rankId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        Integer startNum =startNum =Integer.parseInt(Constant.DEFAULT_START_NO);
        Integer pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        baseResp = this.rankService.selectFashionMan(null,rankId,startNum,pageSize);
        return baseResp;
    }

    /**
     * 获取整个榜单的排名
     * @url http://ip:port/app_service/api/rankShare/rankMemberSort
     * @param rankId 榜单id
     * @param sortType 排序的方式 0:综合排序 1:赞 2:花
     * @return
     */
    @RequestMapping(value="rankMemberSort")
    public BaseResp<Object> rankMemberSort(Long rankId,Integer sortType){
        BaseResp<Object> baseResp = new BaseResp<>();
        if(rankId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(sortType == null){//默认综合排序
            sortType = 0;
        }
        Integer startNum = Integer.parseInt(Constant.DEFAULT_START_NO);
        Integer pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        baseResp = this.rankService.rankMemberSort(rankId,sortType,startNum,pageSize);
        return baseResp;
    }

    /**
     * 查看榜单获奖详情
     * @url http://ip:port/app_service/api/rankShare/rankAwardDetail
     * @param rankid
     * @return
     */
    @RequestMapping(value="rankAwardDetail")
    public BaseResp<Object> rankAwardDetail(Long rankid){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(rankid == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.rankService.rankAwardDetail(rankid,null);
        return baseResp;
    }

    /**
     * 查看单个用户在榜中的信息
     * @url http://ip:port/app_service/api/rankShare/selectRankMemberDetail
     * @param userid 用户id
     * @param rankId 榜单id
     * @return
     */
    @RequestMapping(value="selectRankMemberDetail")
    public BaseResp<Object> selectRankMemberDetail(Long userid,Long rankId){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userid == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.rankService.selectRankMebmerDetail(userid,rankId,null);
        return baseResp;
    }

    /**
     * 进步详情
     * @url http://ip:port/app_service/api/rankShare/improveDetail
     * @param impid 进步id
     * @param  businesstype 进步的类型 0.独立进步 1.目标 2.榜 3.圈子 4.教室 5.教室批复作业
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @RequestMapping(value = "improveDetail")
    public BaseResp select(String impid,String businesstype) {
        BaseResp<Improve> baseResp = new BaseResp<Improve>();
        if (StringUtils.hasBlankParams(impid,businesstype)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp =improveService.select(null, impid, businesstype, null);
            Improve improve = baseResp.getData();
            Long userid = improve.getUserid();
            Long businessid = improve.getBusinessid();
            //查看该用户在榜中发布的所有进步数量 以及 排名
            Map<String,Object> resultMap = this.rankService.getUserSortNumAndImproveCount(userid,businessid);
            baseResp.setExpandData(resultMap);
            return baseResp;
        } catch (Exception e) {
            logger.error("get improve detail  is error impid={} msg={}", impid, e);
        }
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_01,Constant.RTNINFO_SYS_01);
    }

    /**
     * @Title: http://ip:port/appservice/api/rankShare/commentList
     * @Description: 查看最新评论列表
     * @param @param businessid  各类型对应的id
     * @param @param businesstype  类型    0 零散进步评论   1 目标进步评论    2 榜评论  3圈子评论 4 教室评论
     * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
     * @auther yxc
     * @currentdate:2017年1月22日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/commentList")
    public BaseResp<Object> commentList(String impid,String businesstype) {
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(impid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = commentMongoService.selectCommentListByItypeidAndFriendid(null, impid, businesstype, "", 0, 15);
        } catch (Exception e) {
            logger.error("commentList businessid = {}, businesstype = {}", impid, businesstype, e);
        }
        return baseResp;
    }

    /**
     * 获取 赞，花，钻 列表
     * @url: http://ip:port/appservice/api/rankShare/lfdlist
     * @param impid    进步id
     * @param opttype  操作类型 0 -- 赞列表 1 -- 送花列表  2--送钻列表
     * @return
     * @author luye
     */
    @RequestMapping(value = "lfdlist")
    @ResponseBody
    public BaseResp<List<ImpAllDetail>> getImproveLFDList(String impid, String opttype) {
        BaseResp<List<ImpAllDetail>> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(impid, opttype)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = improveService.selectImproveLFDList(impid, opttype,15, null);
        } catch (Exception e) {
            logger.error("get improve all detail list is error:{}", e);
        }
        return baseResp;
    }

    /**
     * 查询单个用户在榜单中发布的进步列表
     * @url: http://ip:port/appservice/api/rankShare/selectListInRank
     * @param userid
     * @param rankid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "selectListInRank")
    public BaseResp selectListInRank(String userid, String rankid) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if (StringUtils.hasBlankParams(userid, rankid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        logger.info("inprove select userid={},impid={}", userid);
        try {
            return improveService.selectListInRank(null,userid,rankid, Constant.IMPROVE_RANK_TYPE,0,15);
        } catch (Exception e) {
            logger.error("get improve detail  is error userid={},impid={} ", userid, e);
        }
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_01,Constant.RTNINFO_SYS_01);
    }

    /**
     * url: http://ip:port/app_service/api/rankShare/rank/list method: POST 获取进步列表(榜中)
     * @param rankid   榜单id
     * @param sift 筛选类型 （ 0 - 全部 1 - 关注 2 - 好友 3 - 熟人）
     * @param sorttype 排序类型（ 0 - 成员动态 1 - 热度 2 - 时间）
     * @return
     * @author:luye
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @RequestMapping(value = "rank/list")
    public BaseResp selectRankImproveList(String rankid, String sorttype, String sift) {
        if (StringUtils.hasBlankParams(rankid, sorttype, sift)) {
            return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        List<Improve> improves = null;
        try {
            improves = improveService.selectRankImproveList(null, rankid, sift, sorttype,0,15,null);
        } catch (Exception e) {
            logger.error("select rank improve list is error:{}", e);
        }
        if (null == improves) {
            return new BaseResp(Constant.STATUS_SYS_43, Constant.RTNINFO_SYS_43);
        }
        BaseResp<List<Improve>> baseres = BaseResp.ok(Constant.RTNINFO_SYS_44);
        baseres.setData(improves);
        return baseres;
    }
}

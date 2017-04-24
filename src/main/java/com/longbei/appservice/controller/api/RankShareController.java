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
     * @param startNum
     * @param endNum
     * @return
     */
    @RequestMapping(value="selectFashionMan")
    public BaseResp<Object> selectFashionMan(Long userid,Long rankId,Integer startNum,Integer endNum){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(rankId == null){
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
     * 获取整个榜单的排名
     * @url http://ip:port/app_service/api/rankShare/rankMemberSort
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
     * 查看榜单获奖详情
     * @url http://ip:port/app_service/api/rankShare/rankAwardDetail
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
     * 查看单个用户在榜中的信息
     * @url http://ip:port/app_service/api/rankShare/selectRankMemberDetail
     * @param userid 用户id
     * @param rankId 榜单id
     * @param currentUserId 当前登录用户id
     * @return
     */
    @RequestMapping(value="selectRankMemberDetail")
    public BaseResp<Object> selectRankMemberDetail(Long userid,Long rankId,Long currentUserId){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userid == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.rankService.selectRankMebmerDetail(userid,rankId,currentUserId);
        return baseResp;
    }

    /**
     * 进步详情
     * @url http://ip:port/app_service/api/rankShare/improveDetail
     * @param userid
     * @param impid
     * @param businessid
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @RequestMapping(value = "improveDetail")
    public BaseResp select(String userid, String impid, String businessid) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if (StringUtils.hasBlankParams(userid, impid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp =improveService.select(userid, impid, "2", businessid);
            //查看该用户在榜中发布的所有进步数量 以及 排名
            Map<String,Object> resultMap = this.rankService.getUserSortNumAndImproveCount(Long.parseLong(userid),Long.parseLong(businessid));
            baseResp.setExpandData(resultMap);
            return baseResp;
        } catch (Exception e) {
            logger.error("get improve detail  is error userid={},impid={} ", userid, impid, e);
        }
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_01,Constant.RTNINFO_SYS_01);
    }

    /**
     * @Title: http://ip:port/appservice/api/rankShare/commentList
     * @Description: 查看最新评论列表
     * @param @param userid   当前访问者商户id
     * @param @param businessid  各类型对应的id
     * @param @param businesstype  类型    0 零散进步评论   1 目标进步评论    2 榜评论  3圈子评论 4 教室评论
     * @param @param startNo
     * @param @param pageSize
     * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
     * @auther yxc
     * @currentdate:2017年1月22日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/commentList")
    @ResponseBody
    public BaseResp<Object> commentList(String userid, String improveId,int startNo, int pageSize) {
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(userid, improveId)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = commentMongoService.selectCommentListByItypeidAndFriendid(userid, improveId, "2", startNo, pageSize);
        } catch (Exception e) {
            logger.error("commentList businessid = {}, businesstype = {}", improveId, "2", e);
        }
        return baseResp;
    }

    /**
     * 获取 赞，花，钻 列表
     * @url: http://ip:port/appservice/api/rankShare/lfdlist
     * @param impid    进步id
     * @param opttype  操作类型 0 -- 赞列表 1 -- 送花列表  2--送钻列表
     * @param pagesize 获取条数
     * @param lastdate 最后一条时间 （初次获取可以为null）
     * @return
     * @author luye
     */
    @RequestMapping(value = "lfdlist")
    @ResponseBody
    public BaseResp<List<ImpAllDetail>> getImproveLFDList(String impid, String opttype, String pagesize, String lastdate) {
        BaseResp<List<ImpAllDetail>> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(impid, opttype, pagesize)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        Date tempLastDate = null;
        if(StringUtils.isNotEmpty(lastdate)){
            tempLastDate = new Date(Long.parseLong(lastdate));
        }
        try {
            baseResp = improveService.selectImproveLFDList(impid, opttype,Integer.parseInt(pagesize), tempLastDate);
        } catch (Exception e) {
            logger.error("get improve all detail list is error:{}", e);
        }
        return baseResp;
    }

    /**
     * 查询单个用户在榜单中发布的进步列表
     * @url: http://ip:port/appservice/api/rankShare/selectListInRank
     * @param curuserid
     * @param userid
     * @param rankid
     * @param startno
     * @param pagesize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "selectListInRank")
    public BaseResp selectListInRank(String curuserid,String userid, String rankid, Integer startno,Integer pagesize) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if (StringUtils.hasBlankParams(userid, rankid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        if(null == startno){
            startno = 0;
        }
        if(null == pagesize){
            pagesize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }
        logger.info("inprove select userid={},impid={}", userid);
        try {
            return improveService.selectListInRank(curuserid,userid,rankid, Constant.IMPROVE_RANK_TYPE,
                    startno,pagesize);
        } catch (Exception e) {
            logger.error("get improve detail  is error userid={},impid={} ", userid, e);
        }
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_01,Constant.RTNINFO_SYS_01);
    }

    /**
     * url: http://ip:port/app_service/api/rankShare/rank/list method: POST 获取进步列表(榜中)
     *
     * @param userid   用户id
     * @param rankid   榜单id
     * @param sift 筛选类型 （ 0 - 全部 1 - 关注 2 - 好友 3 - 熟人）
     * @param sorttype 排序类型（ 0 - 成员动态 1 - 热度 2 - 时间）
     * @param pageSize 页面显示条数
     * @param lastdate 最后一条时间 在 sorttype=0 时使用
     * @return
     * @author:luye
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = "rank/list")
    public BaseResp selectRankImproveList(String userid, String rankid, String sorttype, String sift, String startNo,
                                          String pageSize,String lastdate) {
        if (StringUtils.hasBlankParams(rankid, sorttype, sift)) {
            return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        if ("0".equals(sorttype)){
            if (StringUtils.isBlank(lastdate)){
                lastdate = DateUtils.formatDateTime1(new Date());
//                return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
            }
        }
        if (StringUtils.isBlank(startNo)) {
            startNo = Constant.DEFAULT_START_NO;
        }
        if (StringUtils.isBlank(pageSize)) {
            pageSize = Constant.DEFAULT_PAGE_SIZE;
        }
        List<Improve> improves = null;
        try {
            improves = improveService.selectRankImproveList(userid, rankid, sift, sorttype, Integer.parseInt(startNo),
                    Integer.parseInt(pageSize),lastdate);

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

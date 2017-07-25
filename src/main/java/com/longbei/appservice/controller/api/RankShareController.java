package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.common.web.JsonDateValueProcessor;
import com.longbei.appservice.common.web.JsonLongValueProcessor;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.ClassroomMembersService;
import com.longbei.appservice.service.CommentMongoService;
import com.longbei.appservice.service.GoalService;
import com.longbei.appservice.service.ImproveService;
import com.longbei.appservice.service.RankService;
import com.netflix.discovery.converters.Auto;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangyongzhi 17/4/19.
 */
@Controller
@RequestMapping(value = "api/rankShare",produces = "application/json")
public class RankShareController {

    private Logger logger = LoggerFactory.getLogger(RankShareController.class);

    @Autowired
    private RankService rankService;
    @Autowired
    private ImproveService improveService;
    @Autowired
    private CommentMongoService commentMongoService;
    @Autowired
    private GoalService goalService;
    @Autowired
    private ClassroomMembersService classroomMembersService;

    /**
     * 获取榜单详情
     * @url http://ip:port/app_service/api/rankShare/rankDetail
     * @param rankId
     * @return
     */
    @RequestMapping(value="rankDetail")
    public void rankDetail(String rankId, HttpServletResponse response,String callback){
        logger.info("rankId = {}", rankId);
        BaseResp<Rank> baseResp = new BaseResp<Rank>();
        if(StringUtils.isEmpty(rankId)){
            try{
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
                PrintWriter out = response.getWriter();
                response.setContentType("text/javascript;charset=UTF-8");
                out.print(callback+"("+JSONObject.fromObject(baseResp).toString()+")");
                out.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            return;
        }

        baseResp = this.rankService.selectRankDetailByRankid(null,rankId,true,true);

        try{
            PrintWriter out = response.getWriter();
            response.setContentType("text/javascript;charset=UTF-8");
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.registerJsonValueProcessor(Long.class,new JsonLongValueProcessor());
            jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
            JSONObject jsonObject = JSONObject.fromObject(baseResp,jsonConfig);
            out.print(callback+"("+jsonObject.toString()+")");
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
//        return baseResp;
    }

    /**
     * 查询榜单中的达人
     * @url http://ip:port/app_service/api/rankShare/selectFashionMan
     * @param rankId 榜单id
     * @return
     */
    @RequestMapping(value="selectFashionMan")
    @ResponseBody
    public BaseResp<Object> selectFashionMan(Long rankId){
        logger.info("rankId={}",rankId);
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
     * @param userid 当前登录用户id
     * @param rankId 榜单id
     * @param sortType 排序的方式 0:综合排序 1:赞 2:花
     * @return
     */
    @RequestMapping(value="rankMemberSort")
    @ResponseBody
    public BaseResp<Object> rankMemberSort(Long userid,Long rankId,Integer sortType){
        logger.info("userid={},rankId={},sortType={}",userid,rankId,sortType);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(rankId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(sortType == null){//默认综合排序
            sortType = 0;
        }
        Integer startNum = Integer.parseInt(Constant.DEFAULT_START_NO);
        Integer pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        baseResp = this.rankService.rankMemberSort(userid,rankId,sortType,startNum,pageSize);
        return baseResp;
    }

    /**
     * 查看榜单获奖详情
     * @url http://ip:port/app_service/api/rankShare/rankAwardDetail
     * @param rankid
     * @return
     */
    @RequestMapping(value="rankAwardDetail")
    @ResponseBody
    public BaseResp<Object> rankAwardDetail(Long rankid){
        logger.info("rankid={}",rankid);
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
     * @param businesstype 类型    0 零散进步   1 目标进步    2 榜中  3圈子中进步 4 教室
     * @return
     */
    @SuppressWarnings("unchecked")
	@ResponseBody
    @RequestMapping(value="selectRankMemberDetail")
    public BaseResp<Object> selectRankMemberDetail(Long userid,Long rankId, String businesstype){
        logger.info("userid={},rankId={}",userid,rankId);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userid == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(StringUtils.isBlank(businesstype)){
        	businesstype = "2";
        }
        if("2".equals(businesstype)){
        	baseResp = this.rankService.selectRankMebmerDetail(userid,rankId,null);
        }
        if("4".equals(businesstype)){
        	baseResp = this.classroomMembersService.selectRoomMemberDetail(rankId, userid, null);
        }
        return baseResp;
    }

    /**
     * 进步详情
     * @url http://ip:port/app_service/api/rankShare/improveDetail
     * @param impid 进步id
     * @param  businesstype 进步的类型 0.独立进步 1.目标 2.榜 3.圈子 4.教室 5.教室批复作业
     * @return
     */
    @ResponseBody
    @SuppressWarnings({"rawtypes", "unchecked"})
    @RequestMapping(value = "improveDetail")
    public BaseResp select(String impid,String businesstype,String businessid) {
        logger.info("impid={},businesstype={},businessid={}",impid,businesstype,businessid);
        BaseResp<Improve> baseResp = new BaseResp<Improve>();
        if (StringUtils.hasBlankParams(impid,businesstype)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp =improveService.select(null, impid, businesstype, businessid);
            if(!"0".equals(businesstype) && !"5".equals(businesstype)){
                Improve improve = baseResp.getData();
                Long userid = improve.getUserid();
                //查看该用户在榜中发布的所有进步数量 以及 排名
                Map<String,Object> resultMap = this.rankService.getUserSortNumAndImproveCount(userid,Long.parseLong(businessid));
                baseResp.setExpandData(resultMap);
            }
            return baseResp;
        } catch (Exception e) {
            logger.error("get improve detail  is error impid={} msg={}", impid, e);
        }
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_01,Constant.RTNINFO_SYS_01);
    }

    /**
     * @Title: http://ip:port/appservice/api/rankShare/commentList
     * @Description: 查看最新评论列表
     * @param businessid  各类型对应的id
     * @param businesstype  类型  0 零散进步评论   1 目标进步评论    2 榜评论  3圈子评论 4 教室评论
     * 										10：榜中微进步  11 圈子中微进步  12 教室中微进步
     * @param impid 进步id
     * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
     * @auther yxc
     * @currentdate:2017年1月22日
     */
    @ResponseBody
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/commentList")
    public BaseResp<Object> commentList(String impid,String businesstype,String businessid) {
        logger.info("impid={},businesstype={},businessid={}",impid,businesstype,businessid);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(impid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = commentMongoService.selectCommentListByItypeidAndFriendid(null, businessid, businesstype, impid, null, 15);
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
        logger.info("impid={},opttype={}",impid,opttype);
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
     * @param businesstype 类型    0 零散进步   1 目标进步    2 榜中  3圈子中进步 4 教室
     * @return
     */
	@ResponseBody
    @RequestMapping(value = "selectListInRank")
    public BaseResp selectListInRank(String userid, String rankid, String businesstype) {
        logger.info("userid={},rankid={}",userid,rankid);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if (StringUtils.hasBlankParams(userid, rankid, businesstype)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        logger.info("inprove select userid={},impid={}", userid);
        try {
        	if("2".equals(businesstype)){
        		return improveService.selectListInRank(null,userid,rankid, Constant.IMPROVE_RANK_TYPE,0,15);
        	}
        	if("4".equals(businesstype)){
        		return improveService.selectListInRank(null,userid,rankid, Constant.IMPROVE_CLASSROOM_TYPE,0,15);
        	}
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
    @ResponseBody
    public BaseResp selectRankImproveList(String rankid, String sorttype, String sift) {
        logger.info("rankid={},sorttype={},sift={}",rankid,sorttype,sift);
        if (StringUtils.hasBlankParams(rankid, sorttype, sift)) {
            return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        BaseResp<List<Improve>> improves = new BaseResp<>();
            try {
                improves = improveService.selectRankImproveList(null, rankid, sift, sorttype,0,15,null);
            } catch (Exception e) {
            logger.error("select rank improve list is error:{}", e);
        }
            if (null == improves.getData()) {
            return improves.initCodeAndDesp(Constant.STATUS_SYS_43, Constant.RTNINFO_SYS_43);
        }
            improves.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_44);
            return improves;
    }

    /**
     * @Title: http://ip:port/app_service/api/rankShare/getGoalDetail
     * @Description: 获取目标详情
     * @param @param goalid 目标id
     * @auther yinxc
     * @currentdate:2017年4月8日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "goalDetail")
    @ResponseBody
    public BaseResp<UserGoal> goalDetail(String goalid){
        logger.info("getGoalDetail goalid = {}", goalid);
        BaseResp<UserGoal> baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(goalid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try{
            baseResp = goalService.selectUserGoal(null, Long.parseLong(goalid));
        }catch(Exception e){
            logger.error("getGoalDetail goalid = {}", goalid, e);
        }
        return  baseResp;
    }

    /**
     * 获取目标中的进步
     * @url http://ip:port/app_service/api/rankShare/goal/list
     * @param goalid   目标id
     * @author:luye
     * @date 2017/2/4
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = "goal/list")
    public BaseResp selectGoalImproveList(String goalid) {
        logger.info("goalid = {}",goalid);
        if (StringUtils.hasBlankParams(goalid)) {
            return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        String startNum = Constant.DEFAULT_START_NO;
        String pageSize = Constant.DEFAULT_PAGE_SIZE;
        List<Improve> improves = null;
        try {
            improves = improveService.selectGoalImproveList(null, goalid, null, Integer.parseInt(startNum),
                    Integer.parseInt(pageSize));
        } catch (Exception e) {
            logger.error("select goal improve list is error:{}", e);
        }
        if (null == improves) {
            return new BaseResp(Constant.STATUS_SYS_43, Constant.RTNINFO_SYS_43);
        }
        BaseResp<List<Improve>> baseres = BaseResp.ok(Constant.RTNINFO_SYS_44);
        baseres.setData(improves);
        return baseres;
    }

    /**
     * 榜主名片
     * @param rankCardId
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "rankCard")
    @ResponseBody
    public String rankCard(String rankCardId,HttpServletRequest request){
        logger.info("rankCard rankCardId = {}", rankCardId);
        BaseResp<Object> baseResp = new BaseResp<>();
        String callback = request.getParameter("callback");
        if(StringUtils.hasBlankParams(rankCardId)){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            return callback + "("+ JSONObject.fromObject(baseResp).toString()+")";
        }
        try{
            baseResp = rankService.rankCardDetail(rankCardId);
        }catch(Exception e){
            logger.error("rankCard rankCardId = {}", rankCardId, e);
        }
        String jsonObjectStr = JSONObject.fromObject(baseResp).toString();
        return callback + "("+jsonObjectStr+")";
    }

}

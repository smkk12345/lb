package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.common.web.BaseController;
import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.entity.UserGoal;
import com.longbei.appservice.service.GoalService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by smkk on 17/2/10.
 * 关联部分  发进步更新目标中icount条数 更新更新时间
 * 删除进步更新条数 更新主进步
 */
@RestController
@RequestMapping(value = "/goal")
public class GoalController extends BaseController {

    @Autowired
    private GoalService goalService;
    private static Logger logger = LoggerFactory.getLogger(GoalController.class);

    /**
     * @url http://ip:port/app_service/goal/insert
     * @param userid 用户id
     * @param goaltag  目标名称
     * @param ptype 十全十美类别
     * @param ispublic 可见程度 0 私密 1 好友可见 2 全部可见
     * @param needwarn 0 不需要提醒 1 需要提醒
     * @param warntime 提醒时间
     * @param week  提醒周   逗号隔开
     * @return
     */
    @RequestMapping(value = "insert")
    public BaseResp<Object> insertGoal(String userid,
                                       String goaltag,
                                       String ptype,
                                       String ispublic,
                                       String needwarn,
                                       String warntime,
                                       String week){
        BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(userid,goaltag,ptype,ispublic)){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        logger.info("goalService insert userid={},goaltag={}," +
                "ptype={},ispublic={},needwarn={},warntime={},week={}",userid,goaltag,ptype,ispublic,needwarn,warntime,week);
        long lUserid = Long.parseLong(userid);
        try{
            if(StringUtils.isBlank(warntime)){
                warntime = "";
            }
            if(StringUtils.isBlank(week)){
                week = "";
            }
            baseResp = goalService.insert(lUserid,goaltag,ptype,ispublic,needwarn,warntime,week);
        }catch (Exception e){
            logger.error("goalService.insert error and msg={}",e);
        }
        return baseResp;
    }

    /**
     * @url http://ip:port/app_service/goal/list
     * 获取用户所有的目标进步列表
     * @param userid startNum,endNum
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "list")
    public BaseResp<Object> list(String userid,Integer startNum,Integer pageSize){
        BaseResp<Object> baseResp = new BaseResp<>();
        logger.info("goal list userid={},startNum={},pageSize={}",userid,startNum,pageSize);
        if(StringUtils.isBlank(userid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try{
            baseResp = goalService.list(Long.parseLong(userid),startNum,pageSize);
        }catch(Exception e){
            logger.error("goalService.list error and msg={}",e);
        }
        return  baseResp;
    }
    
    /**
     * @url http://ip:port/app_service/goal/goallist
     * 获取单个目标进步列表
     * @param userid 
     * @param goalid 目标id
     * @param startNum
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "goallist")
    public BaseResp<List<Improve>> list(String userid, String goalid, Integer startNum,Integer pageSize){
        BaseResp<List<Improve>> baseResp = new BaseResp<>();
        logger.info("goal list userid={},startNum={},pageSize={}",userid,startNum,pageSize);
        if(StringUtils.hasBlankParams(userid, goalid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(null == pageSize){
            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }
        try{
            baseResp = goalService.selectListByGoalid(Long.parseLong(userid), Long.parseLong(goalid), startNum, pageSize);
        }catch(Exception e){
            logger.error("goalService.list error and msg={}",e);
        }
        return  baseResp;
    }
    
    /**
     * @Title: http://ip:port/app_service/goal/userGoalList
     * @Description: 获取目标列表
     * @param @param userid 当前登录id
     * @param @param startNum,endNum
     * @param @param 正确返回 code 0，验证码不对，参数错误，未知错误返回相应状态码
     * @return  
     * @auther yinxc
     * @currentdate:2017年4月7日
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "userGoalList")
    public BaseResp<List<UserGoal>> userGoalList(String userid,Integer startNum,Integer pageSize){
        BaseResp<List<UserGoal>> baseResp = new BaseResp<>();
        logger.info("user goal list userid={},startNum={},endNum={}",userid,startNum,pageSize);
        if(StringUtils.isBlank(userid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(startNum == null || startNum < 0){
            startNum = Integer.parseInt(Constant.DEFAULT_START_NO);
        }
        if(null == pageSize){
            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }
        try{
            baseResp = goalService.selectUserGoalList(Long.parseLong(userid),startNum,pageSize);
        }catch(Exception e){
            logger.error("goalService.list error and msg={}",e);
        }
        return  baseResp;
    }
    
    /**
     * @Title: http://ip:port/app_service/goal/getGoalDetail
     * @Description: 获取目标详情
     * @param @param userid 当前登录id
     * @param @param goalid 目标id
     * @param @param 正确返回 code 0，验证码不对，参数错误，未知错误返回相应状态码
     * @return  
     * @auther yinxc
     * @currentdate:2017年4月8日
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "getGoalDetail")
    public BaseResp<UserGoal> getGoalDetail(String userid, String goalid){
        BaseResp<UserGoal> baseResp = new BaseResp<>();
        logger.info("getGoalDetail userid = {}, goalid = {}", userid, goalid);
        if(StringUtils.hasBlankParams(userid, goalid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try{
            baseResp = goalService.selectUserGoal(Long.parseLong(userid), Long.parseLong(goalid));
        }catch(Exception e){
            logger.error("getGoalDetail userid = {}, goalid = {}", userid, goalid, e);
        }
        return  baseResp;
    }

    /**
     * @url http://ip:port/app_service/goal/updateTtitle
     * 修改目标名称
     * @param goalid
     * @param goaltag
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value="updateTtitle")
    public BaseResp<UserGoal> updateTitle(String goalid,String goaltag){
        BaseResp<UserGoal> baseResp = new BaseResp<>();
        logger.info("updateTtitle goalid = {}, title = {}", goalid, goaltag);
        if(StringUtils.hasBlankParams(goalid,goaltag)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = goalService.updateTitle(Long.parseLong(goalid),goaltag);
        return baseResp;
    }

    /**
     * 删除目标
     * @param goalid 目标id
     * @param userid
     * @param gtype 0:不删除目标进步   1：删除目标进步
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value="delGoal")
	public BaseResp<Object> delGoal(String goalid,String userid, String gtype){
        BaseResp<Object> baseResp = new BaseResp<>();
        logger.info("delGoal goalid = {}, userid = {}", goalid, userid);
        if(StringUtils.hasBlankParams(goalid, userid, gtype)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = goalService.delGoal(Long.parseLong(goalid),Long.parseLong(userid), gtype);
        return baseResp;
    }


}

package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.common.web.BaseController;
import com.longbei.appservice.service.GoalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by smkk on 17/2/10.
 */
@Controller
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
    @ResponseBody
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
            baseResp = goalService.insert(lUserid,goaltag,ptype,ispublic,needwarn,warntime,week);
        }catch (Exception e){
            logger.error("goalService.insert error and msg={}",e);
        }
        return baseResp;
    }

    /**
     *
     * @param userid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "list",method = RequestMethod.POST)
    public BaseResp<Object> list(String userid,Integer startNum,Integer endNum){
        BaseResp<Object> baseResp = new BaseResp<>();
        try{

        }catch(Exception e){
            logger.error("goalService.list error and msg={}",e);
        }
        return  baseResp;
    }


}

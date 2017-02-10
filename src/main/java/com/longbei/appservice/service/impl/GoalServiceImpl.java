package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.ImproveGoalMapper;
import com.longbei.appservice.dao.UserGoalMapper;
import com.longbei.appservice.entity.UserGoal;
import com.longbei.appservice.service.GoalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by smkk on 17/2/10.
 */
@Service
public class GoalServiceImpl implements GoalService {

    @Autowired
    private UserGoalMapper userGoalMapper;
    @Autowired
    private ImproveGoalMapper improveGoalMapper;

    private static Logger logger = LoggerFactory.getLogger(GoalServiceImpl.class);

    @Override
    public BaseResp<Object> insert(long userid, String goaltag, String ptype, String ispublic, String needwarn, String warntime, String week) {
        BaseResp<Object> baseResp = new BaseResp<>();
        UserGoal userGoal = new UserGoal();
        userGoal.setUserid(userid);
        userGoal.setGoaltag(goaltag);
        userGoal.setPtype(ptype);
        userGoal.setCreatetime(new Date());
        userGoal.setIsdel("0");
        userGoal.setIspublic(ispublic);
        userGoal.setNeedwarn(needwarn);
        userGoal.setWarntime(warntime);
        userGoal.setWeek(week);
        try{
            int n = userGoalMapper.insert(userGoal);
            if(n == 1){
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
            }
        }catch (Exception e){
            logger.error("userGoalMapper.insert error and msg = {}",e);
        }
        return baseResp;
    }

    /**
     * 目标进步列表
     * @param userid
     * @param startNum
     * @param endNum
     * @return
     */
    @Override
    public BaseResp<Object> list(long userid, int startNum, int endNum) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
           // List<Improve> goalList = improveGoalMapper.selectByUserId(userid,startNum,endNum);
        }catch (Exception e){
            logger.error("list error and msg={}",e);
        }
        return baseResp;
    }
}

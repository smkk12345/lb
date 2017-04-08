package com.longbei.appservice.service;

import java.util.List;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.entity.UserGoal;

/**
 * Created by smkk on 17/2/10.
 */
public interface GoalService {
    /**
     * 新增目标
     * @param userid
     * @param goaltag
     * @param ptype
     * @param ispublic
     * @param needwarn
     * @param warntime
     * @param week
     * @return
     */
    BaseResp<Object> insert(long userid,
                            String goaltag,
                            String ptype,
                            String ispublic,
                            String needwarn,
                            String warntime,
                            String week);

    /**
     * 获取用户目标列表
     * @param userid
     * @param startNum
     * @param endNum
     * @return
     */
    BaseResp<Object> list(long userid,int startNum,int endNum);
    
    /**
     * 单个目标进步列表
     * @param userid
     * @param goalid 目标id
     * @param startNum
     * @param endNum
     * @return
     */
    BaseResp<List<Improve>> selectListByGoalid(long userid, long goalid, int startNum, int endNum);
    
    /**
     * 获取目标列表
     * @param userid
     * @param startNum
     * @param endNum
     * @return
     */
    BaseResp<List<UserGoal>> selectUserGoalList(long userid, int startNum, int endNum);
    
    /**
     * 获取目标详情
     * @param userid
     * @param goalid 目标id
     * @return
     */
    BaseResp<UserGoal> selectUserGoal(long userid, long goalid);

    /**
     * 更新目标title
     * @param goalId
     * @param title
     * @return
     */
    BaseResp<UserGoal> updateTitle(long goalId, String title);

    /**
     * 删除目标 同时更新目标中所有微进步的状态
     * @param goalId
     * @param userid
     * @param gtype 0:不删除目标进步   1：删除目标进步
     * @return
     */
    BaseResp<Object> delGoal(long goalId, long userid, String gtype);

}

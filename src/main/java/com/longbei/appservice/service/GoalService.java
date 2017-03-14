package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;

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
     * 更新目标title
     * @param goalId
     * @param title
     * @return
     */
    BaseResp<Object> updateTitle(long goalId, String title);

    /**
     * 删除目标 同时更新目标中所有微进步的状态
     * @param goalId
     * @param userid
     * @return
     */
    BaseResp<Object> delGoal(long goalId,long userid);

}

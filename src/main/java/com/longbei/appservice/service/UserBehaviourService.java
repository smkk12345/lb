package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserInfo;

/**
 * 用户行为接口   可能包含统计  每日操作
 * Created by smkk on 17/2/7.
 */
public interface UserBehaviourService {
    /**
     * 是否可以做更多操作
     * operateType  0 签到  1 发进步 2 点赞 ...
     * @param userid
     * @param operateType
     * @return
     */
    BaseResp<Object> canOperateMore(long userid,UserInfo userInfo, String operateType);
    /**
     * 是否升级
     * @param userid
     * @return
     */
    BaseResp<Object> levelUp(long userid,int iPoint,String pType);
    /**
     * 是否升级
     * @param userInfo
     * @return
     */
    BaseResp<Object> levelUp(UserInfo userInfo,int iPoint,String pType);


}

package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.entity.UserInfo;
import net.sf.json.JSONObject;

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
     * userInfo operateType 操作类型(Constant_point)   十全十美类型
     * return point  impicon  status
     * @return
     */
    BaseResp<Object> pointChange(UserInfo userInfo,String operateType,String pType,String origin,long impid,long friendid);

    /**
     * 用户特权
     * 通过用户信息判断用户是否拥有该权限
     * operateType  添加榜
     *  public static final String USER_PRIVILEGE_ADD_CIRCLE = "0";//
     *  public static final String USER_PRIVILEGE_ADD_RANK = "1";//
     *  public static final String USER_PRIVILEGE_ADD_CLASSROOM = "2";//
     */
    BaseResp<Object> hasPrivilege(UserInfo userInfo, Constant.PrivilegeType privilegeType,Object o);

    /**
     * 用户信息统计
     * @param userSumType  类型
     * @param userid 当前用户id
     * @param improve 对象
     * @param count 数量
     * @return
     */
    BaseResp<Object> userSumInfo(Constant.UserSumType userSumType,
                                 long userid, Improve improve, int count);


    /**
     * @Title: updateUserPLDetailToplevel
     * @Description: 更新是否最高级
     * @param userid
     * @param @return
     * @return boolean 返回类型
     * @auther IngaWu
     * @currentdate:2017年5月3日
     */
    boolean updateUserPLDetailToplevel(long userid,String pType);


}

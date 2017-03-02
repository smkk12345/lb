package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;

/**
 * Created by wangyongzhi on 17/2/28.
 */
public interface CircleService {

    /**
     * 根据圈子名称查询相关圈子
     * @param circleName 圈子名称
     * @return
     */
    BaseResp<Object> relevantCircle(String circleName,Integer startNo,Integer pageSize);

    /**
     * 新建兴趣圈
     * @param circleTitle
     * @param circlephotos
     * @param circlebrief
     * @return
     */
    BaseResp<Object> insertCircle(String userId,String circleTitle, String circlephotos, String circlebrief,
                                  Integer circleinvoloed,String ptype,Boolean ispublic,Boolean needconfirm,Boolean creategoup);

    /**
     * 校验兴趣圈名是否可用
     * @param circleTitle
     * @return
     */
    boolean checkCircleTitle(String circleTitle);

    /**
     * 更改兴趣圈 信息
     * @param circleId
     * @param userId
     * @param circlephotos
     * @param circlebrief
     * @return
     */
    BaseResp<Object> updateCircleInfo(Integer circleId, String userId, String circlephotos, String circlebrief,String circleNotice);

    /**
     * 查询兴趣圈 成员
     * @param circleId
     * @param startNo
     * @param pageSize
     * @param flag 是否查询成员在兴趣圈中获得的赞和花的数量
     * @return
     */
    BaseResp<Object> selectCircleMember(Long circleId,String username, Integer startNo, Integer pageSize,boolean flag);

    /**
     * 加入兴趣圈
     * @param circleId
     * @param userId
     * @return
     */
    BaseResp<Object> insertCircleMember(Long circleId, String userId);

    /**
     * 退出圈子
     * @param circleId
     * @param userId
     * @return
     */
    BaseResp<Object> removeCircleMembers(Long circleId, String userId);

    /**
     * 查询兴趣圈某个成员的信息
     * @param circleId
     * @param userId
     * @return
     */
    BaseResp<Object> circleMemberDetail(Long circleId, Long userId);

    /**
     * 查询兴趣圈 详情
     * @param circleId 兴趣圈id
     * @return
     */
    BaseResp<Object> circleDetail(Long circleId);

    /**
     * 圈主审核用户的加圈子请求
     * @param userId
     * @param circleMembersId
     * @param confirmFlag
     * @return
     */
    BaseResp<Object> confirmInsertCircleMember(Long userId, Integer circleMembersId, Boolean confirmFlag);
}

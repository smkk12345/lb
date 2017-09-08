package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wangyongzhi 17/3/8.
 */
public interface GroupService {
    /**
     * 新建群组
     * @param userIds
     * @return
     */
    BaseResp<Object> createGroup(String[] userIds,String mainGroupUserId,Integer type,Long typeId,String groupNam,Boolean needConfirm);

    /**
     * 更新群组信息
     * @param userId 用户id
     * @param groupId 群组id
     * @param groupName 新的群组名称
     * @param needConfirm 成员进群是否需要验证
     * @param notice 群公告
     * @return
     */
    BaseResp<Object> updateGroupInfo(Long userId, String groupId, String groupName,Boolean needConfirm,String notice);

    /**
     * 更改用户在群组里的昵称
     * @param userId
     * @param groupId
     * @param nickName
     * @return
     */
    BaseResp<Object> updateGroupMemberNickName(Long userId, String groupId, String nickName);

    /**
     * 申请加群
     * @param userId
     * @param invitationUserId
     * @param groupId
     * @param remark
     * @return
     */
    BaseResp<Object> insertGroupMember(String[] userId, Long invitationUserId, String groupId, String remark);

    /**
     * 批量处理群组成员的状态
     * @param userIds
     * @param groupId
     * @param currentUserId
     * @return
     */
    BaseResp<Object> batchUpdateGroupMember(String[] userIds, String groupId, Long currentUserId,Integer status);

    /**
     * 退出群组
     * @param userIds 用户id
     * @param groupId 群组id
     * @param currentUserId 当前登录用户id
     * @return
     */
    BaseResp<Object> quietGroup(String[] userIds, String groupId, Long currentUserId);

    /**
     * 查询群组成员list
     * @param groupId 群组id
     * @param userId 用户id
     * @param status 查询的状态
     * @return
     */
    BaseResp<Object> groupMemberList(String groupId, Long userId, Integer status,String keyword,Boolean noQueryCurrentUser,Integer startNum,Integer pageSize);

    /**
     * 解散群
     * @param userId
     * @param groupId
     * @return
     */
    BaseResp<Object> dismissGroup(Long userId, String groupId);

    /**
     * 更新群组 群主id
     * @param userId
     * @param currentUserId
     * @param groupId
     * @return
     */
    BaseResp<Object> updateGroupMainUser(Long userId, Long currentUserId, String groupId);

    /**
     * 查询用户所在的群列表
     * @param userId 用户id
     * @param startNum
     * @param pageSize
     * @return
     */
    BaseResp<Object> goupListByUser(Long userId, Integer startNum, Integer pageSize,Date updateTime);

    /**
     * 查询群组
     * @param keyword userid
     * @return
     */
    BaseResp<Object> searchGroup(Long userid,String keyword,Integer startNum,Integer pageSize);

    /**
     * 更新用户群组的其他信息
     * @param userid 用户id
     * @param groupId 群组id
     * @param topStatus 群组是否置顶 0.不置顶 1.置顶
     * @param disturbStatus 是否消息免打扰 0.关闭消息免打扰 1.开启消息免打扰
     * @return
     */
    BaseResp<Object> updateUserGroupOtherInfo(Long userid, Long groupId, Integer topStatus, Integer disturbStatus);

    /**
     * 查询群组的详情
     * @param groupId
     * @param userid
     * @return
     */
    BaseResp<Object> groupDetail(Long groupId, Long userid);

    /**
     * 查询用户在群组中的信息
     * @param userid
     * @param groupId
     * @return
     */
    BaseResp<Object> groupMemberDetail(Long userid, Long groupId);

    /**
     * 查询群组成员的id
     * @param groupId
     * @return
     */
    BaseResp<Object> selectGroupMemberIdList(Long groupId);

    /**
     * 批量更新用户的群昵称
     * @param map
     * @return
     */
    int batchUpdateGroupMemberNickName(Map<String, Object> map);

    /**
     * 批量创建群主
     * @param mainGroupUserid
     * @param groupname
     * @return
     */
    BaseResp<List<Map<String,Object>>> batchCreateGroup(Long mainGroupUserid, String gradeid, String groupname,Long managerid);

    /**
     * 加入叶圣陶的群组 当groupid群满的时候,新建一个群 并把该用户加入该群
     * @param userid
     * @param groupid
     * @param groupuserid
     * @param groupname
     * @return
     */
    BaseResp<Object> joinYSTInstitutionGroup(Long userid, Long groupid, Long groupuserid, String groupname);
}

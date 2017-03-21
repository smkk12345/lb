package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangyongzhi 17/3/8.
 */
@RestController
@RequestMapping(value = "group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    /**
     * 新建群组
     * @url http://ip:port/app_service/group/createGroup
     * @param userIds 新建群时,加入的用户id
     * @param mainGroupUserId 群主用户id
     * @param type 群组关联的类型 1.榜 2.兴趣圈 3.教室
     * @param typeId 群组关联的类型id
     * @param groupName 群组名称
     * @return
     */
    @RequestMapping(value="createGroup")
    public BaseResp<Object> createGroup(String userIds,String mainGroupUserId,Integer type,Long typeId,String groupName,Boolean needConfirm){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(mainGroupUserId == null || StringUtils.isEmpty(groupName) || groupName.length() > 30){
            return baseResp.fail("参数错误");
        }
        String[] userIdss = null;
        if(StringUtils.isNotEmpty(userIds)){
            userIdss = userIds.split(",");
        }
        baseResp = groupService.createGroup(userIdss,mainGroupUserId,type,typeId,groupName,needConfirm);
        return baseResp;
    }

    /**
     * 更新群组名称
     * @url http://ip:port/app_service/group/updateGroupName
     * @param userId 当前用户id
     * @param groupId 群组id
     * @param groupName 群组名称
     * @return
     */
    @RequestMapping(value="updateGroupName")
    public BaseResp<Object> updateGroupName(Long userId,String groupId,String groupName){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userId == null || StringUtils.hasBlankParams(groupId,groupName)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }

        baseResp = this.groupService.updateGroupInfo(userId,groupId,groupName,null,null);
        return baseResp;
    }

    /**
     * 更改成员加入群组 是否需要验证
     * @url http://ip:port/app_service/group/updateGroupNeedConfirm
     * @param userId 当前登录用户id
     * @param groupId 群组id
     * @param needConfirm 是否需要验证 true 代表加群需要验证 false代表加群不需要验证
     * @return
     */
    @RequestMapping("updateGroupNeedConfirm")
    public BaseResp<Object> updateGroupNeedConfirm(Long userId,String groupId,Boolean needConfirm){
        BaseResp<Object> baseResp = new BaseResp<>();
        if(userId == null || StringUtils.isEmpty(groupId) || needConfirm == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.groupService.updateGroupInfo(userId,groupId,null,needConfirm,null);

        return baseResp;
    }

    /**
     * 更改群公告
     * @url http://ip:port/app_service/group/updateGroupNotice
     * @param userId 用户id
     * @param groupId 群组id
     * @param notice 公告
     * @return
     */
    public BaseResp<Object> updateGroupNotice(Long userId,String groupId,String notice){
        BaseResp<Object> baseResp = new BaseResp<>();
        if(userId == null || StringUtils.hasBlankParams(groupId,notice) || notice.length() > 100){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.groupService.updateGroupInfo(userId,groupId,null,null,notice);
        return baseResp;
    }

    /**
     * 更改成员 群昵称
     * @url http://ip:port/app_service/group/updateGroupMemberNickName
     * @param userId
     * @param groupId
     * @param nickName
     * @return
     */
    @RequestMapping(value="updateGroupMemberNickName")
    public BaseResp<Object> updateGroupMemberNickName(Long userId,String groupId,String nickName){
        BaseResp<Object> baseResp = new BaseResp<>();
        if(userId == null || StringUtils.hasBlankParams(groupId,nickName)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp= groupService.updateGroupMemberNickName(userId,groupId,nickName);

        return baseResp;
    }

    /**
     * 加群 用户主动加群,成员邀请好友加群,以及群主邀请好友加群 都是调用该接口
     * @url http://ip:port/app_service/group/insertGroupMember
     * @param userIds 用户id 加群人的id 被邀请人的id
     * @param invitationUserId 邀请人id 如果没有邀请人,则可不传
     * @param groupId 群组id
     * @param remark 附加信息
     * @return
     */
    @RequestMapping(value="insertGroupMember")
    public BaseResp<Object> insertGroupMember(String[] userIds,Long invitationUserId,String groupId,String remark){
        BaseResp<Object> baseResp = new BaseResp<>();
        if(userIds == null || StringUtils.isEmpty(groupId)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.groupService.insertGroupMember(userIds,invitationUserId,groupId,remark);
        return baseResp;
    }

    /**
     * 批量审核群成员的加群申请
     * @url http://ip:port/app_service/group/batchUpdateGroupMember
     * @param userIds
     * @param groupId 群组id
     * @param currentUserId 当前登录用户id
     * @param status 群成员状态 1.同意 2.拒绝
     * @return
     */
    @RequestMapping(value="batchUpdateGroupMember")
    public BaseResp<Object> batchUpdateGroupMember(String[] userIds,String groupId,Long currentUserId,Integer status){
        BaseResp<Object> baseResp = new BaseResp<>();
        if(userIds == null || userIds.length < 1 || currentUserId == null || StringUtils.isEmpty(groupId) || status == null || status > 2 || status < 1){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.groupService.batchUpdateGroupMember(userIds,groupId,currentUserId,status);
        return baseResp;
    }

    /**
     * 退群
     * @url http://ip:port/app_service/group/quietGroup
     * @param userIds 退出群组的用户id
     * @param groupId 群组id
     * @param currentUserId 当前登录用户
     * @return
     */
    @RequestMapping(value="quietGroup")
    public BaseResp<Object> quietGroup(String[] userIds,String groupId,Long currentUserId){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userIds == null || userIds.length < 1 || currentUserId == null || StringUtils.isEmpty(groupId)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.groupService.quietGroup(userIds,groupId,currentUserId);
        return baseResp;
    }

    /**
     * 查询群成员
     * @url http://ip:port/app_service/group/groupMemberList
     * @param groupId 群组id
     * @param userId 用户id
     * @param status 查询群组成员的状态 1.代表查询加群成功的,已经在群中的群成员 0.代表查询待群主审核的群成员(仅限群主查询)
     * @return
     */
    @RequestMapping(value="groupMemberList")
    public BaseResp<Object> groupMemberList(String groupId,Long userId,Integer status){
        BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.isEmpty(groupId) || userId == null || status == null || (status != 0 && status != 1)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.groupService.groupMemberList(groupId,userId,status);
        return baseResp;
    }

    /**
     * 解散群组
     * @url http://ip:port/app_service/group/dismissGroup
     * @param userId 用户id
     * @param groupId 群组id
     * @return
     */
    @RequestMapping(value="dismissGroup")
    public BaseResp<Object> dismissGroup(Long userId,String groupId){
        BaseResp<Object> baseResp = new BaseResp<>();
        if(userId == null || StringUtils.isEmpty(groupId)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.groupService.dismissGroup(userId,groupId);
        return baseResp;
    }

    /**
     * 更新群主
     * @url http://ip:port/app_service/group/updateGroupMainUser
     * @param userId 新的群主id
     * @param currentUserId 当前登录用户id
     * @param groupId 群id
     * @return
     */
    @RequestMapping(value="updateGroupMainUser")
    public BaseResp<Object> updateGroupMainUser(Long userId,Long currentUserId,String groupId){
        if(userId == null || currentUserId == null || StringUtils.isEmpty(groupId)){
            return new BaseResp<Object>(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        return this.groupService.updateGroupMainUser(userId,currentUserId,groupId);
    }

    /**
     * 查询用户的群列表
     * @url http://ip:port/app_service/group/groupList
     * @param userId 当前登录用户id
     * @param startNum
     * @param endNum
     * @return
     */
    @RequestMapping(value="groupList")
    public BaseResp<Object> groupList(Long userId,Integer startNum,Integer endNum){
        BaseResp<Object> baseResp = new BaseResp<>();
        if(userId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.groupService.goupListByUser(userId,startNum,endNum);
        return baseResp;
    }

}

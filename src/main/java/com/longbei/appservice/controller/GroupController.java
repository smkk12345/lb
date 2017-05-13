package com.longbei.appservice.controller;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by wangyongzhi 17/3/8.
 */
@RestController
@RequestMapping(value = "group")
public class GroupController {
    private static Logger logger = LoggerFactory.getLogger(GroupController.class);
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
        logger.info("userIds={},mainGroupUserId={},type={},typeId={},groupName={},needConfirm={}",userIds,mainGroupUserId,type,typeId,groupName,needConfirm);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(mainGroupUserId == null || (StringUtils.isNotEmpty(groupName) && groupName.length() > 40)){
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
        logger.info("userId={},groupId={},groupName={}",userId,groupId,groupName);
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
        logger.info("userId={},groupId={},needConfirm={}",userId,groupId,needConfirm);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(userId == null || StringUtils.isEmpty(groupId) || needConfirm == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.groupService.updateGroupInfo(userId,groupId,null,needConfirm,null);

        return baseResp;
    }

    /**
     * 更新用户群组的其他信息
     * @url http://ip:port/app_service/group/updateUserGroupOtherInfo
     * @param userid 用户id
     * @param groupId 群组id
     * @param topStatus 群组是否置顶 0.不置顶 1.置顶
     * @param disturbStatus 是否消息免打扰 0.关闭消息免打扰 1.开启消息免打扰
     * @return
     */
    @RequestMapping(value="updateUserGroupOtherInfo")
    public BaseResp<Object> updateUserGroupOtherInfo(Long userid,Long groupId,Integer topStatus,Integer disturbStatus){
        logger.info("userid={},groupId={},topStatus={},disturbStatus={}",userid,groupId,topStatus,disturbStatus);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userid == null || groupId == null || (topStatus != null && disturbStatus != null) ||
                (topStatus == null && disturbStatus == null)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.groupService.updateUserGroupOtherInfo(userid,groupId,topStatus,disturbStatus);
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
    @RequestMapping(value="updateGroupNotice")
    public BaseResp<Object> updateGroupNotice(Long userId,String groupId,String notice){
        logger.info("userId={},groupId={},notice={}",userId,groupId,notice);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(userId == null || StringUtils.hasBlankParams(groupId) || notice.length() > 100){
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
        logger.info("userId={},groupId={},nickName={}",userId,groupId,nickName);
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
        logger.info("userIds={},invitationUserId={},groupId={},remark={}",JSON.toJSONString(userIds),invitationUserId,groupId,remark);
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
        logger.info("userIds={},groupId={},currentUserId={},status={}",JSON.toJSONString(userIds),groupId,currentUserId,status);
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
        logger.info("userIds={},groupId={},currentUserId={}",JSON.toJSONString(userIds),groupId,currentUserId);
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
     * @param keyword 搜索的群成员用户昵称
     * @param groupId 群组id
     * @param userId 用户id
     * @param status 查询群组成员的状态 1.代表查询加群成功的,已经在群中的群成员 0.代表查询待群主审核的群成员(仅限群主查询)
     * @param noQueryCurrentUser 是否查询当前登录用户 false或不传该参数则代表查询userid,true代表不查询userid的用户
     * @param startNum 可不传 不传该参数，则代表获取所有的群成员 不分页
     * @param pageSize 可不传
     * @return
     */
    @RequestMapping(value="groupMemberList")
    public BaseResp<Object> groupMemberList(String keyword,String groupId,Long userId,Integer status,Boolean noQueryCurrentUser,Integer startNum,Integer pageSize){
        logger.info("keyword={},groupId={},userId={},status={},noQueryCurrentUser={},startNum={},pageSize={}", keyword,groupId,userId,status,noQueryCurrentUser,startNum,pageSize);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.isEmpty(groupId) || userId == null || status == null || (status != 0 && status != 1)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(startNum != null && startNum < 0){
            startNum = Integer.parseInt(Constant.DEFAULT_START_NO);
        }
        if(null == pageSize){
            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }

        baseResp = this.groupService.groupMemberList(groupId,userId,status,keyword,noQueryCurrentUser,startNum,pageSize);
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
        logger.info("userId={},groupId={}",userId,groupId);
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
        logger.info("userId={},groupId={},currentUserId={}",userId,groupId,currentUserId);
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
     * @param pageSize
     * @param updateTime 上次更新用户群列表的时间
     * @return
     */
    @RequestMapping(value="groupList")
    public BaseResp<Object> groupList(Long userId,Integer startNum,Integer pageSize,String updateTime){
        logger.info("userId={},startNum={},pageSize={},updateTime={}", userId,startNum,pageSize,updateTime);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(userId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(startNum != null && startNum < 0){
            startNum = 0;
        }
        if(null == pageSize){
            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }

        Date updateDate = null;
        if(StringUtils.isNotEmpty(updateTime) && !"0".equals(updateTime)){
            try{
                updateDate = DateUtils.formatDate(updateTime,null);
            }catch(Exception e){
                logger.error("select group list format updateTime error updateTime:{}",updateTime);
            }
        }

        baseResp = this.groupService.goupListByUser(userId,startNum,pageSize,updateDate);
        return baseResp;
    }

    /**
     * 搜索群组 按照群号搜索
     * @url http://ip:port/app_service/group/searchGroup
     * @param keyword
     * @return
     */
    @RequestMapping(value="searchGroup")
    public BaseResp<Object> searchGroup(String keyword,Integer startNum,Integer pageSize){
        logger.info("keyword={},startNum={},pageSize={}", keyword,startNum,pageSize);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(StringUtils.isEmpty(keyword)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(startNum == null || startNum < 0){
            startNum = Integer.parseInt(Constant.DEFAULT_START_NO);
        }
        if(null == pageSize){
            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }

        baseResp = this.groupService.searchGroup(keyword,startNum,pageSize);
        return baseResp;
    }

    /**
     * 查询群组详情
     * @url http://ip:port/app_service/group/groupDetail
     * @param groupId 群组id
     * @param userid 用户id
     * @return
     */
    @RequestMapping(value="groupDetail")
    public BaseResp<Object> groupDetail(Long groupId,Long userid){
        logger.info("groupId={},userid={}",groupId,userid);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(groupId == null || userid == null){
            return  baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.groupService.groupDetail(groupId,userid);
        return baseResp;
    }

    /**
     * 查询用户在群组中的信息
     * @param userid
     * @param groupId
     * @return
     */
    @RequestMapping(value="groupMemberDetail")
    public BaseResp<Object> groupMemberDetail(Long userid,Long groupId){
        logger.info("userid={},groupId={}",userid,groupId);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userid == null || groupId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.groupService.groupMemberDetail(userid,groupId);
        return baseResp;
    }

    /**
     * group/selectGrupMemberIdList
     * 查询群组的所有userid
     * @param groupId
     * @return
     */
    @RequestMapping(value="selectGrupMemberIdList")
    public BaseResp<Object> selectGrupMemberIdList(Long groupId){
        logger.info("groupId={}",groupId);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(groupId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.groupService.selectGroupMemberIdList(groupId);
        return baseResp;
    }

}

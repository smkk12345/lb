package com.longbei.appservice.service.api.outernetservice;

import com.longbei.appservice.common.BaseResp;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by smkk on 17/2/15.
 */
@FeignClient("outernetService")
@RequestMapping("outernetService")
public interface IRongYunService {

    /**
     * 获取融云的token
     * @param userid
     * @param username
     * @param portraitUri
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/rongyun/getRYToken")
    BaseResp<Object> getRYToken(@RequestParam("userid") String userid,
                                @RequestParam("username") String username,
                                @RequestParam("portraitUri") String portraitUri);

    /**
     * 更新用户的信息
     * @param userid
     * @param nickname 用户昵称
     * @param portraitUri 用户头像
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,value = "/rongyun/refreshUserInfo")
    BaseResp<Object> refreshUserInfo(@RequestParam("userid") String userid,@RequestParam("nickname") String nickname,
                                        @RequestParam("portraitUri") String portraitUri);

    /**
     * 新建群组
     * @param userIdss 新建群组的用户id
     * @param groupId 群组的id
     * @param groupName 群组名称
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/rongyun/createGroup")
    BaseResp<Object> createGroup(@RequestParam("userIdss") String userIdss,
                                 @RequestParam("groupId") Long groupId,
                                 @RequestParam("groupName") String groupName);

    /**
     * 更新群组名称
     * @param groupId 群组id
     * @param groupName 群组名称
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/rongyun/updateGroupName")
    BaseResp<Object> updateGroupName(@RequestParam("operatorUserId")String operatorUserId,
                                     @RequestParam("operatorNickname")String operatorNickname,
                                     @RequestParam("groupId") String groupId,
                                     @RequestParam("groupName") String groupName);

    /**
     * 加入群组
     * @param userIds
     * @param groupId
     * @param groupName
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/rongyun/joinGroupMember")
    BaseResp<Object> joinGroupMember(@RequestParam("operatorUserId")String operatorUserId,
                                     @RequestParam("operatorNickname")String operatorNickname,
                                     @RequestParam("targetUserDisplayNames")String targetUserDisplayNames,
                                     @RequestParam("userIds") String userIds,
                                     @RequestParam("groupId") String groupId,
                                     @RequestParam("groupName")String groupName);

    /**
     * 退出群组
     * @param userIds
     * @param groupId
     * @return
     */
    @RequestMapping(method=RequestMethod.GET,value="/rongyun/quietGroup")
    BaseResp<Object> quietGroup(@RequestParam("operatorUserId")String operatorUserId,
                                @RequestParam("operatorNickname")String operatorNickname,
                                @RequestParam("targetUserDisplayNames")String targetUserDisplayNames,
                                @RequestParam("userIds")String userIds,
                                @RequestParam("groupId")String groupId);

    /**
     * 解散群组
     * @param operatorNickname
     * @param userId
     * @param groupId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,value="/rongyun/dismissGroup")
    BaseResp<Object> dismissGroup(@RequestParam("operatorNickname")String operatorNickname,@RequestParam("userId") String userId,
                                  @RequestParam("groupId") String groupId);

    /**
     * 更改用户的群组昵称 发送融云的资料更改通知
     * @param userId
     * @param groupId
     * @param nickName
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/rongyun/updateGroupMemberNickname")
    BaseResp<Object> updateGroupMemberNickname(@RequestParam("userId")String userId,
                                @RequestParam("groupId")String groupId,
                                @RequestParam("nickName") String nickName);

    /**
     * 通知用户创建群
     * @param operatorUserId 操作群的用户id
     * @param operatorNickname 操作群的用户昵称
     * @param groupId 群组id
     * @param groupName 群组名称
     * @return
     */
    @RequestMapping(method=RequestMethod.GET,value="/rongyun/noticeCreateGroup")
    BaseResp<Object> noticeCreateGroup(@RequestParam("operatorUserId") String operatorUserId,@RequestParam("operatorNickname")String operatorNickname,
                                       @RequestParam("groupId") String groupId,@RequestParam("groupName") String groupName);


    /**
     * 批量创建群组
     * @param userId
     * @param groupIds
     * @param groupNames
     * @return
     */
    @RequestMapping(value="/rongyun/batchCreateGroup",method =RequestMethod.POST )
    BaseResp<Object> batchCreateGroup(@RequestParam("userId") Long userId,
                                      @RequestParam("groupIds") String groupIds,
                                      @RequestParam("groupNames")String groupNames,
                                      @RequestParam("otheruserids")String otheruserids);
}

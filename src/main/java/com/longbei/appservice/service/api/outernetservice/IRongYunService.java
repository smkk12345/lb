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

    @RequestMapping(method = RequestMethod.GET, value = "/rongyun/getRYToken")
    BaseResp<Object> getRYToken(@RequestParam("userid") String userid,
                                @RequestParam("username") String username,
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
    BaseResp<Object> joinGroupMember(@RequestParam("userIds") String[] userIds,
                                     @RequestParam("groupId") String groupId,
                                     @RequestParam("groupName")String groupName);

    /**
     * 退出群组
     * @param userIds
     * @param groupId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/rongyun/quietGroup")
    BaseResp<Object> quietGroup(@RequestParam("userIds")String[] userIds,
                                @RequestParam("groupId")String groupId);

    @RequestMapping(method = RequestMethod.GET, value = "/rongyun/dismissGroup")
    BaseResp<Object> dismissGroup(@RequestParam("userId") String userId,
                                  @RequestParam("groupId") String groupId);
}

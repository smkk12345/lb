package com.longbei.appservice.service.api.outernetservice;

import com.longbei.appservice.common.BaseResp;
import feign.Param;
import feign.RequestLine;
import net.sf.json.JSONArray;

/**
 * Created by smkk on 17/2/15.
 */
public interface IRongYunService {

    @RequestLine("GET /rongyun/getRYToken?userid={userid}&username={username}&portraitUri={portraitUri}")
    BaseResp<Object> getRYToken(@Param("userid") String userid,
                                 @Param("username") String username,
                                 @Param("portraitUri") String portraitUri);

    /**
     * 新建群组
     * @param userIdss 新建群组的用户id
     * @param groupId 群组的id
     * @param groupName 群组名称
     * @return
     */
    @RequestLine("GET /rongyun/createGroup?userIdss={userIdss}&groupId={groupId}&groupName={groupName}")
    BaseResp<Object> createGroup(@Param("userIdss") String userIdss,
                         @Param("groupId") Long groupId,
                         @Param("groupName") String groupName);

    /**
     * 更新群组名称
     * @param groupId 群组id
     * @param groupName 群组名称
     * @return
     */
    @RequestLine("GET /rongyun/updateGroupName?operatorUserId={operatorUserId}&operatorNickname={operatorNickname}&groupId={groupId}&groupName={groupName}")
    BaseResp<Object> updateGroupName(@Param("operatorUserId")String operatorUserId,@Param("operatorNickname")String operatorNickname,@Param("groupId") String groupId,@Param("groupName") String groupName);

    /**
     * 加入群组
     * @param userIds
     * @param groupId
     * @param groupName
     * @return
     */
    @RequestLine("GET /rongyun/joinGroupMember?userIds={userIds}&groupId={groupId}&groupName={groupName}")
    BaseResp<Object> joinGroupMember(@Param("userIds") String userIds,@Param("groupId") String groupId,
                                     @Param("groupName")String groupName);

    /**
     * 退出群组
     * @param userIds
     * @param groupId
     * @return
     */
    @RequestLine("GET /rongyun/quietGroup?userIds={userIds}&groupId={groupId}")
    BaseResp<Object> quietGroup(@Param("userIds")String userIds,@Param("groupId")String groupId);

    @RequestLine("GET /rongyun/dismissGroup?userId={userId}&groupId={groupId}")
    BaseResp<Object> dismissGroup(@Param("userId") String userId,@Param("groupId") String groupId);
}

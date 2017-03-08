package com.longbei.appservice.service.api.outernetservice;

import com.longbei.appservice.common.BaseResp;
import feign.Param;
import feign.RequestLine;

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
     * @param userIds 新建群组的用户id
     * @param groupId 群组的id
     * @param groupName 群组名称
     * @return
     */
    @RequestLine("GET /rongyun/createGroup?userIds={userIds}&groupId={groupId}&groupName={groupName}")
    BaseResp createGroup(@Param("userIds") String[] userIds,
                         @Param("groupId") Long groupId,
                         @Param("groupName") String groupName);
}

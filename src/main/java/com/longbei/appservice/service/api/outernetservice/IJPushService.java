package com.longbei.appservice.service.api.outernetservice;

import com.longbei.appservice.common.BaseResp;
import feign.Param;
import feign.RequestLine;

/**
 * Created by smkk on 17/2/15.
 *  jpush推送
 */
public interface IJPushService {
    @RequestLine("GET /jpush/messagePush?regid={regid}&alert={alert}&title={title}&content={content}")
    BaseResp<Object> messagePush(@Param("regid") String regid,
                                 @Param("alert") String alert,
                                 @Param("title") String title,
                                 @Param("content")String content);
}

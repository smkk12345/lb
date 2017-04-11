package com.longbei.appservice.service.api.outernetservice;

import com.longbei.appservice.common.BaseResp;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by smkk on 17/2/15.
 *  jpush推送
 */
@FeignClient("outernetService")
@RequestMapping("outernetService")
public interface IJPushService {
    @RequestMapping(method = RequestMethod.GET, value = "/jpush/messagePush")
    BaseResp<Object> messagePush(@RequestParam("regid") String regid,
                                 @RequestParam("alert") String alert,
                                 @RequestParam("title") String title,
                                 @RequestParam("content")String content);
}

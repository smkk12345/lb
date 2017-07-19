package com.longbei.appservice.service.api.outernetservice;

import com.longbei.appservice.common.BaseResp;

import feign.Headers;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by smkk on 17/2/15.
 */
@FeignClient("outernetService")
@RequestMapping("outernetService")
public interface IAlidayuService {
    /**
     * 阿里大鱼发送短信外网接口
     * @param mobile
     * @param validateCode
     * @param operateName
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/alidayu/sendMsg")
    BaseResp<Object> sendMsg(@RequestParam("mobile") String mobile,
                             @RequestParam("validateCode") String validateCode,
                             @RequestParam("operateName") String operateName);
    /**
     * 阿里大鱼批量发送短信外网接口
     * @param mobiles 手机号码列表
     * @param templateId 短信模版id
     */
    @RequestMapping(method = RequestMethod.GET, value = "/alidayu/sendMsgBatch")
    @Headers("Content-Type: application/json")
    BaseResp<Object> sendMsgBatch(List<String> mobiles,
                                  @RequestParam("templateId") String templateId);

}

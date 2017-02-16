package com.longbei.appservice.service.api.outernetservice;

import com.longbei.appservice.common.BaseResp;
import feign.Param;
import feign.RequestLine;

/**
 * Created by smkk on 17/2/15.
 */
public interface IAlidayuService {
    /**
     * 阿里大鱼发送短信外网接口
     * @param mobile
     * @param validateCode
     * @param operateName
     * @return
     */
    @RequestLine("GET /alidayu/sendMsg?mobile={mobile}&validateCode={validateCode}&operateName={operateName}")
    BaseResp<Object> sendMsg(@Param("mobile") String mobile, @Param("validateCode") String validateCode, @Param("operateName") String operateName);

}

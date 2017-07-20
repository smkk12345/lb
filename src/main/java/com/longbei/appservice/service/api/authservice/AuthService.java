package com.longbei.appservice.service.api.authservice;

import com.longbei.appservice.common.BaseResp;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * token
 *
 * @author luye
 * @create 2017-07-20 下午1:42
 **/
@FeignClient("authService")
@RequestMapping(value = "auth_service")
public interface AuthService {

    @RequestMapping(value = "auth/verify")
    BaseResp<String> verifyToken(@RequestParam("serviceName") String serviceName,
                                 @RequestParam("token") String token);


}

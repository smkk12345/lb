package com.longbei.appservice.service.api.outernetservice;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("userService")
public interface ComputeClient {

//    @RequestMapping(method = RequestMethod.GET, value = "/product/test")
//    int test();

        @RequestMapping(method = RequestMethod.GET, value = "/com/add")
    int add(@RequestParam("a")int a);

}
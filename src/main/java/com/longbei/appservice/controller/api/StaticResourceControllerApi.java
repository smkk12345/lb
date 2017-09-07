package com.longbei.appservice.controller.api;

import com.google.gson.Gson;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.MediaResourceDetail;
import com.longbei.appservice.service.MediaResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * staticresource
 *
 * @author luye
 * @create 2017-09-07 上午10:24
 **/
@RestController
@RequestMapping("/api/staticresource")
public class StaticResourceControllerApi {

    private static Logger logger = LoggerFactory.getLogger(StaticResourceControllerApi.class);

    @Autowired
    private MediaResourceService mediaResourceService;

    @RequestMapping(value = "ppttoiamgelist")
    public BaseResp pptToImage(@RequestBody List<MediaResourceDetail> list){
        logger.info("pptToImage MediaResourceDetaillist:{}",new Gson().toJson(list));
        BaseResp baseResp = new BaseResp();
        try {
            mediaResourceService.batchInsertMediaResourceDetail(list);
        } catch (Exception e) {
            logger.error("pptToImage is error:",e);
        }
        return baseResp;
    }


}

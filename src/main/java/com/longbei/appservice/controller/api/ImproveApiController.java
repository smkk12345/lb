package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.service.ImproveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by smkk on 17/2/21.
 */
@Controller
@RequestMapping(value = "/api/improve")
public class ImproveApiController {

    @Autowired
    private ImproveService improveService;

    private static Logger logger = LoggerFactory.getLogger(ImproveApiController.class);

    @ResponseBody
    @RequestMapping(value = "updatemedia")
    public BaseResp<Object> updatemedia(String objid, String pickey, String filekey,String workflow){
        try {
            return improveService.updateMedia(objid,pickey,filekey,workflow);
        }catch (Exception e){
            logger.error("improveService.updateMedia error and msg={}",e);
        }
        return new BaseResp<>();
    }


}

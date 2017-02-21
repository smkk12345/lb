package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.Rank;
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


    @ResponseBody
    @RequestMapping(value = "updatemedia")
    public BaseResp<Object> updatemedia(String objid, String pickey, String filekey){

        return null;
    }


}

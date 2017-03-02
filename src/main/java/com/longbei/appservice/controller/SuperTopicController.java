package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lixb on 2017/3/1.
 * 超级话题
 */
@RestController
public class SuperTopicController {

    private static Logger logger = LoggerFactory.getLogger(SuperTopicController.class);

    public BaseResp<Object> list(int startNum,int endNum) {
        BaseResp<Object> baseResp = new BaseResp<>();
        if(endNum == 0){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {

        } catch (Exception e) {
            logger.error("list startNum = {},endNum={} ", startNum,endNum, e);
        }
        return baseResp;
    }

}

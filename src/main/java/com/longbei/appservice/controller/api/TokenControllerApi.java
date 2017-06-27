package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.TokenUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * token
 *
 * @author luye
 * @create 2017-02-10 下午5:39
 **/
@RestController
@RequestMapping(value = "/api/token")
public class TokenControllerApi {


    private static Logger logger = LoggerFactory.getLogger(TokenControllerApi.class);

    @RequestMapping(value = "/getServiceToken")
    public BaseResp<Object> getServiceToken(String appKey) {
        logger.info("appKey",appKey);
        boolean isok = false;
        for (int i = 0 ; i < Constant.OK_SERVICE.length ; i++){
            if (Constant.OK_SERVICE[i].equals(appKey)) {
                isok = true;
                break;
            }
        }
        if(!isok){
            return BaseResp.fail();
        }

        BaseResp<Object> baseResp = new BaseResp<>();
        long exp = new Date().getTime() + 60*1000*60;
        try {
            String token = TokenUtils.createServiceToken(Constant.TOKEN_SIGN_USER, exp);
            Map<String, String> map = new HashMap<>();
            map.put("exp", String.valueOf(exp));
            map.put("token", token);
            baseResp.setData(JSONObject.fromObject(map).toString());
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("getServiceToken error and msg = {}",e);
        }
        return BaseResp.fail();
    }

}

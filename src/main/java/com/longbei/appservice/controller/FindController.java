package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.common.web.BaseController;
import com.longbei.appservice.dao.mongo.dao.CodeDao;
import com.longbei.appservice.service.FindService;
import net.minidev.json.JSONUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by lixb on 2017/3/16.
 */
@Controller
@RequestMapping(value = "find")
public class FindController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FindController.class);

    @Autowired
    private FindService findService;
    @Autowired
    private CodeDao codeDao;

    @RequestMapping("test")
    public String test(){
        String code = codeDao.getCode(null);
        return code;
    }

    /**
     * 附近的人
     * http://server_ip:port/app_service/find/near
     * @param longitude
     * @param latitude
     * @param userid
     * @param startNum
     * @param endNum
     * 全部必须传递
     * lixb
     * @return
     */
    @RequestMapping(value = "near")
    public void near(HttpServletRequest request,HttpServletResponse response,String longitude, String latitude,
                         String userid, String startNum,
                         String endNum,String sex) {

        BaseResp<Object> baseResp = new BaseResp<>();
        String radius = "50";
        if(StringUtils.hasBlankParams(longitude,latitude,userid,startNum,endNum)){
//            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        logger.info("near longitude={},latitude={},radius={},userid={}",longitude,latitude,radius,userid);
        baseResp = findService.near(longitude,latitude,userid,sex,startNum,endNum);
        try {
            PrintWriter out = response.getWriter();
            out.print(JSONObject.fromObject(baseResp).toString());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return  baseResp;
    }

}

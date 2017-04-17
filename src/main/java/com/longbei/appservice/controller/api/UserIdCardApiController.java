package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.UserIdcard;
import com.longbei.appservice.service.UserIdcardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户实名认证
 *
 * @author luye
 * @create 2017-04-14 下午2:32
 **/
@RestController
@RequestMapping(value = "api/useridcard")
public class UserIdCardApiController {

    private static Logger logger = LoggerFactory.getLogger(UserIdCardApiController.class);

    @Autowired
    private UserIdcardService userIdcardService;


    /**
     * 获取用户实名认证列表
     * @param userIdcard
     * @param pageno
     * @param pagesize
     * @return
     * @author luye
     */
    @RequestMapping(value = "list")
    public BaseResp<Page<UserIdcard>> selectUserIdCardListPage(@RequestBody UserIdcard userIdcard,
                                                               String pageno,String pagesize){
        if (StringUtils.isBlank(pageno)){
            pageno = "1";
        }
        if (StringUtils.isBlank(pagesize)){
            pagesize = Constant.DEFAULT_PAGE_SIZE;
        }
        BaseResp<Page<UserIdcard>> baseResp = new BaseResp<>();

        try {
            baseResp = userIdcardService.selectUserIdCardListPage(userIdcard,Integer.parseInt(pageno),
                    Integer.parseInt(pagesize));
        } catch (Exception e) {
            logger.error("select user idcard list  is error:",e);
        }

        return baseResp;

    }

    /**
     * 获取用户认证详情
     * @param userid
     * @return
     * @author luye
     */
    @RequestMapping(value = "info")
    public BaseResp<UserIdcard> getUserIdCardInfo(String userid){
        BaseResp<UserIdcard> baseResp = new BaseResp<>();
        if (StringUtils.isBlank(userid)){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = userIdcardService.userSafety(Long.parseLong(userid));
        } catch (NumberFormatException e) {
            logger.error("get user id card info is error:",e);
        }
        return baseResp;
    }


}

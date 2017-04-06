package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.entity.UserFeedback;
import com.longbei.appservice.service.UserFeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 意见反馈接口
 * Created by mchaolee on 17/4/1.
 */
@RestController
@RequestMapping(value = "api/feedback")
public class UserFeedbackApiController {

    private static Logger logger = LoggerFactory.getLogger(UserFeedbackApiController.class);

    @Autowired
    private UserFeedbackService userFeedbackService;

    /**
     * 意见反馈列表
     */
    @RequestMapping(value = "list/{pageno}/{pagesize}")
    public BaseResp<Page<UserFeedback>> getFeedbackListWithPage(@RequestBody UserFeedback userFeedback,
                                                                  @PathVariable("pageno") String pageno,
                                                                  @PathVariable("pagesize") String pagesize){
        BaseResp<Page<UserFeedback>> baseResp = new BaseResp<>();
        if (StringUtils.isEmpty(pageno)){
            pageno = "1";
        }
        if (StringUtils.isEmpty(pagesize)){
            pageno = Constant.DEFAULT_PAGE_SIZE;
        }
        try {
            Page<UserFeedback> page = userFeedbackService.selectFeedbackListWithPage(userFeedback,Integer.parseInt(pageno),Integer.parseInt(pagesize));
            baseResp = BaseResp.ok();
            baseResp.setData(page);
        } catch (NumberFormatException e) {
            logger.error("get userFeedback list with page is error:{}",e);
        }
        return baseResp;
    }

    @RequestMapping(value = "select/{id}")
    public  BaseResp<UserFeedback> selectFeedback(@PathVariable("id") String id){
        BaseResp<UserFeedback> baseResp = new BaseResp<>();
        if (StringUtils.isEmpty(id)){
            return baseResp;
        }
        try {
            UserFeedback userFeedback = userFeedbackService.selectUserFeedback(id);
            baseResp = BaseResp.ok();
            baseResp.setData(userFeedback);
        } catch (Exception e) {
            logger.error("get userFeedback is error:{}",e);
        }
        return baseResp;
    }

    @RequestMapping(value = "edit")
    public BaseResp editFeedback(@RequestBody UserFeedback userFeedback){
        BaseResp baseResp = new BaseResp();
        try {
            boolean flag = userFeedbackService.updateFeedback(userFeedback);
            if (flag){
                baseResp = BaseResp.ok();
            }
        } catch (NumberFormatException e) {
            logger.error("update userFeedback  is error:{}",e);
        }
        return baseResp;
    }

}

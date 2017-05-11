package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.BehaviorRule;
import com.longbei.appservice.service.BehaviorRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 行为奖励规则
 * created by lichaochao 2017/5/10e
 */
@RestController
@RequestMapping(value = "sysrule")
public class SystemRuleController {

    private static Logger logger = LoggerFactory.getLogger(SystemRuleController.class);

    @Autowired
    private BehaviorRuleService behaviorRuleService;

    /**
     * 获取行为奖励规则
     * @url http://ip:port/app_service/sysrule/behavior
     * @return
     */
    @RequestMapping(value = "behavior")
    public BaseResp<BehaviorRule> getBehaviorRule(){
        logger.info("get behavior rule");
        BaseResp<BehaviorRule> baseResp = new BaseResp<>();
        try {
            BehaviorRule behaviorRule = behaviorRuleService.selectBehaviorRule();
            baseResp.setData(behaviorRule);
        } catch (Exception e) {
            logger.error("get behavior rule is error:",e);
        }
        return baseResp;
    }

}

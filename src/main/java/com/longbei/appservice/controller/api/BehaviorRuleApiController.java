package com.longbei.appservice.controller.api;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.entity.BehaviorRule;
import com.longbei.appservice.entity.UserLevel;
import com.longbei.appservice.service.BehaviorRuleService;
import com.longbei.appservice.service.UserLevelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 行为奖励规则接口
 *
 * @author luye
 * @create 2017-02-27 下午5:27
 **/
@RestController
@RequestMapping(value = "api/behaviorrule")
public class BehaviorRuleApiController {

    private static Logger logger = LoggerFactory.getLogger(BehaviorRuleApiController.class);

    @Autowired
    private BehaviorRuleService behaviorRuleService;
    @Autowired
    private UserLevelService userLevelService;

    /**
     * 详情
     * @return
     */
    @RequestMapping(value = "get")
    public BaseResp<BehaviorRule> getBehaviorRule(){
        BaseResp<BehaviorRule> baseResp = new BaseResp<>();
        try {
            BehaviorRule behaviorRule = behaviorRuleService.selectBehaviorRule();
            baseResp.setData(behaviorRule);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("get behaviorRule  is error:{}",e);
        }
        return baseResp;
    }

    /**
     * 编辑
     * @param behaviorRule
     * @return
     */
    @RequestMapping(value = "update")
    public BaseResp updateBehaviorRule(@RequestBody BehaviorRule behaviorRule){
        logger.info("behaviorRule={}", JSON.toJSONString(behaviorRule));
        BaseResp<BehaviorRule> baseResp = new BaseResp<>();
        try {
            boolean flag = behaviorRuleService.updateBehaviorRule(behaviorRule);
            if (flag){
                baseResp = BaseResp.ok();
            }
        } catch (Exception e) {
            logger.error("update behaviorRule  is error:{}",e);
        }
        return baseResp;
    }

    /**
     * 添加
     * @param behaviorRule
     * @return
     */
    @RequestMapping(value = "add")
    public BaseResp addBehaviorRule(@RequestBody BehaviorRule behaviorRule){
        logger.info("behaviorRule={}", JSON.toJSONString(behaviorRule));
        BaseResp baseResp = new BaseResp<>();
        try {
            boolean flag = behaviorRuleService.insertBehaviorRule(behaviorRule);
            if (flag){
                baseResp = BaseResp.ok();
            }
        } catch (Exception e) {
            logger.error("add behaviorRule  is error:{}",e);
        }
        return baseResp;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "del/{id}")
    public BaseResp deleteBehaviorRule(@PathVariable("id") String id){
        logger.info("id={}",id);
        BaseResp baseResp = new BaseResp();
        if (StringUtils.isEmpty(id)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            boolean flag = behaviorRuleService.deleteBehaviorRule(Long.parseLong(id));
            if (flag){
                baseResp = BaseResp.ok();
            }
        } catch (Exception e) {
            logger.error("delete behaviorRule  is error:{}",e);
        }
        return baseResp;

    }

    /**
     * @Description: 查看用户等级列表
     * @param startNum 分页起始值
     * @param pageSize 每页显示条数
     * @auther IngaWu
     * @currentdate:2017年6月1日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "selectUserLevelList")
    public BaseResp<Page<UserLevel>> selectUserLevelList(Integer startNum, Integer pageSize){
        logger.info("selectUserLevelList for adminservice startNum={},pageSize={}",startNum,pageSize);
        BaseResp<Page<UserLevel>> baseResp = new BaseResp<>();
        if (null == startNum) {
            startNum = 0;
        }
        if (null == pageSize) {
            pageSize = 50;
        }
        try {
            Page<UserLevel> page = userLevelService.selectUserLevelList(startNum,pageSize);
            baseResp = BaseResp.ok();
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("selectUserLevelList for adminservice startNum={},pageSize={}", startNum,pageSize,e);
        }
        return baseResp;
    }

    /**
     * 批量插入等级权限
     * @param levelList
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/insertBatchLevelRules")
    public BaseResp insertBatchLevelRules(@RequestBody List<UserLevel> levelList) {
        BaseResp baseResp = new BaseResp();
        try {
            baseResp = userLevelService.insertBatchLevelRules(levelList);
        } catch (Exception e) {
            logger.error("insertBatchLevelRules is error:", e);
        }
        return baseResp;
    }

    /**
     * 全部删除等级权限
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/deleteLevelRules")
    public BaseResp deleteLevelRules() {
        BaseResp baseResp = new BaseResp();
        try {
            baseResp = userLevelService.deleteLevelRules();
        } catch (Exception e) {
            logger.error("deleteLevelRules is error:", e);
        }
        return baseResp;
    }



}

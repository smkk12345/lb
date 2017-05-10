package com.longbei.appservice.service;

import com.longbei.appservice.entity.BehaviorRule;

/**
 * 行为奖励规则相关操作
 *
 * @author lichaochao
 * @create 2017-05-10 下午4:05
 **/
public interface BehaviorRuleService {

    /**
     * 增
     * @param behaviorRule
     * @return
     */
    boolean insertBehaviorRule(BehaviorRule behaviorRule);

    /**
     * 删
     * @param id
     * @return
     */
    boolean deleteBehaviorRule(Long id);

    /**
     * 改
     * @param behaviorRule
     * @return
     */
    boolean updateBehaviorRule(BehaviorRule behaviorRule);

    /**
     * 查
     * @param id
     * @return
     */
    BehaviorRule selectBehaviorRule(Long id);

}

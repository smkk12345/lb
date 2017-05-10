package com.longbei.appservice.service.impl;

import com.longbei.appservice.dao.BehaviorRuleMapper;
import com.longbei.appservice.entity.BehaviorRule;
import com.longbei.appservice.service.BehaviorRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by mchaolee on 17/5/10.
 */
public class BehaviorRuleServiceImpl implements BehaviorRuleService {

    private static Logger logger = LoggerFactory.getLogger(BehaviorRuleServiceImpl.class);

    @Autowired
    private BehaviorRuleMapper behaviorRuleMapper;

    @Override
    public boolean insertBehaviorRule(BehaviorRule behaviorRule) {
        try {
            int res = behaviorRuleMapper.insert(behaviorRule);
            if(res>0){
                return true;
            }
        } catch (Exception e) {
            logger.error("insert behaviorRule is error:{}",e);
        }
        return false;
    }

    @Override
    public boolean deleteBehaviorRule(Long id) {
        try {
            int res = behaviorRuleMapper.deleteByPrimaryKey(id);
            if(res>0){
                return true;
            }
        } catch (Exception e) {
            logger.error("delete behaviorRule is error:{}",e);
        }
        return false;
    }

    @Override
    public boolean updateBehaviorRule(BehaviorRule behaviorRule) {
        try {
            int res = behaviorRuleMapper.updateByPrimaryKeySelective(behaviorRule);
            if(res>0){
                return true;
            }
        } catch (Exception e) {
            logger.error("update behaviorRule is error:{}",e);
        }
        return false;
    }

    @Override
    public BehaviorRule selectBehaviorRule() {
        BehaviorRule behaviorRule = new BehaviorRule();
        try {
            behaviorRule = behaviorRuleMapper.selectOne();
        } catch (Exception e) {
            logger.error("select behaviorRule is error:{}",e);
        }
        return behaviorRule;
    }
}

package com.longbei.appservice.config;

import com.longbei.appservice.common.Cache.SysRulesCache;
import com.longbei.appservice.dao.SysRuleCheckinMapper;
import com.longbei.appservice.dao.SysRuleLpMapper;
import com.longbei.appservice.dao.SysRulePerfectTenMapper;
import com.longbei.appservice.dao.SysScoringRuleMapper;
import com.longbei.appservice.entity.SysRuleCheckin;
import com.longbei.appservice.entity.SysRulePerfectTen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.List;

/**
 * 规则配置(启动时初始化)
 *
 * @author luye
 * @create 2017-02-04 下午1:59
 **/
@Component
public class InitConfig implements CommandLineRunner {

    //签到规则
    @Autowired
    private SysRuleCheckinMapper sysRuleCheckinMapper;
    //用户特权规则
    @Autowired
    private SysRuleLpMapper sysRuleLpMapper;
    //十全十美等级规则表
    @Autowired
    private SysRulePerfectTenMapper sysRulePerfectTenMapper;
    //计分规则
    @Autowired
    private SysScoringRuleMapper sysScoringRuleMapper;

    @Override
    public void run(String... strings) throws Exception {
        //初始化相关
        initSysRuleCheckInCache();
        //十全十美每级所需龙分缓存
        initSysRulePLevelPointCache();
        //龙级升级每级所需龙分缓存
        initSysRuleLevelPointCache();
    }

    /**
     * 初始化签到规则
     */
    private void initSysRuleCheckInCache(){
        Map<Integer,SysRuleCheckin> map = new HashMap<>();
        List<SysRuleCheckin> list =  sysRuleCheckinMapper.selectAll();
        for (int i = 0; i < list.size(); i++) {
            SysRuleCheckin sysRuleCheckin = list.get(i);
            map.put(sysRuleCheckin.getContinues(),sysRuleCheckin);
        }
        SysRulesCache.sysRuleCheckinMap = map;
    }

    /**
     *十全十美每级所需龙分缓存
     */
    private void initSysRulePLevelPointCache(){
        Map<Integer,Integer> map = new HashMap<>();
        List<SysRulePerfectTen> list = sysRulePerfectTenMapper.selectAll();
        for (int i = 0; i < list.size(); i++) {
            SysRulePerfectTen sysRule = list.get(i);
            map.put(sysRule.getPlevel(),sysRule.getMaxscore()-sysRule.getMinscore());
        }
        SysRulesCache.pLevelPointMap = map;
    }

    /**
     *龙级升级每级所需龙分缓存
     */
    private void initSysRuleLevelPointCache(){
        Map<Integer,Integer> map = new HashMap<>();
        //这里做初始化

        SysRulesCache.levelPointMap = map;
    }

}

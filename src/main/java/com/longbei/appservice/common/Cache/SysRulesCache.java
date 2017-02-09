package com.longbei.appservice.common.Cache;

import com.longbei.appservice.entity.SysRuleCheckin;
import com.longbei.appservice.entity.SysRuleLp;
import com.longbei.appservice.entity.SysRules;

import java.util.ArrayList;
import java.util.HashMap;
import  java.util.Map;
import java.util.List;

/**
 * 系统缓存  如系统规则
 * Created by smkk on 17/2/7.
 */
public class SysRulesCache {

    /**
     * 系统规则
     */
    public static SysRules sysRules;

    /**
     * 签到规则  key--天数  value--规则实体
     */
    public static Map<Integer,SysRuleCheckin> sysRuleCheckinMap = new HashMap<>();

    /**
     * 十全十美每级所需龙分缓存 key--等级 value--龙分  十全十美
     */
    public static Map<Integer,Integer> pLevelPointMap = new HashMap<Integer,Integer>();
    /**
     * 龙级没级所需龙分
     */
    public static Map<Integer,Integer> levelPointMap = new HashMap<>();
    /**
     *等级特权map  key--等级 value--特权
     */
    public static Map<Integer,SysRuleLp> lpMap = new HashMap<>();
    /**
     * 十全十美
     */
    public static Map<Integer,String> perfectTenMap = new HashMap<>(){{
        put(0,"学习");
        put(1,"运动");
        put(2,"社交");
        put(3,"艺术");
        put(4,"生活");
        put(5,"公益");
        put(6,"文学");
        put(7,"劳动");
        put(8,"修养");
        put(9,"健康");
    }};

}

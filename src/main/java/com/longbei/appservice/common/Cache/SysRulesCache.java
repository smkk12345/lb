package com.longbei.appservice.common.Cache;

import com.longbei.appservice.entity.SysRules;

import java.util.HashMap;
import  java.util.Map;

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
     * 每级所需龙分缓存
     */
    public static Map<Integer,Integer> levelPointMap = new HashMap<Integer,Integer>();

}

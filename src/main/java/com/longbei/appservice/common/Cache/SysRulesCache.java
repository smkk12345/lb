package com.longbei.appservice.common.Cache;

import com.longbei.appservice.entity.*;

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
    public static SysRules sysRules = new SysRules();

    /**
     * 签到规则  key--天数  value--规则实体
     */
    public static Map<Integer,SysRuleCheckin> sysRuleCheckinMap = new HashMap<>();

    /**
     * 十全十美每级所需龙分缓存 key--等级 value--龙分  十全十美
     * 业务发生变化社交升级龙分和别的分类不一致了 所以修改数据结构
     * key  ptype+&+level  value point
     */
    public static Map<String,Integer> pLevelPointMap = new HashMap<String,Integer>();
    /**
     * 龙级没级所需龙分
     */
    public static Map<Integer,UserLevel> levelPointMap = new HashMap<>();
    /**
     *等级特权map  key--等级 value--特权
     */
    public static Map<Integer,SysRuleLp> lpMap = new HashMap<>();
    /**
     * 十全十美
     */
    public static Map<Integer,String> perfectTenMap = new HashMap(){{
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

    public static List<PerfectTen> perfectTenList = new ArrayList<PerfectTen>(){{
        add(new PerfectTen("0","学习",0));
        add(new PerfectTen("1","运动",1));
        add(new PerfectTen("2","社交",2));
        add(new PerfectTen("3","艺术",3));
        add(new PerfectTen("4","生活",4));
        add(new PerfectTen("5","公益",5));
        add(new PerfectTen("6","文字",6));
        add(new PerfectTen("7","劳动",7));
        add(new PerfectTen("8","修养",8));
        add(new PerfectTen("9","健康",9));
    }};






}

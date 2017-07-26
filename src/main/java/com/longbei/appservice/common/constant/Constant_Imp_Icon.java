package com.longbei.appservice.common.constant;

import com.longbei.appservice.common.Cache.SysRulesCache;
import com.longbei.appservice.common.utils.RandomUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by smkk on 17/2/24.
 * 送进步币
 */
public class Constant_Imp_Icon {


    public static int DAILY_CHECKIN = 0;

    public static void init(){
        int first = SysRulesCache.behaviorRule.getFirstsignimpcoins();
        int more = SysRulesCache.behaviorRule.getContinuesignimpcoinsmore();
        int impcoinsmax = SysRulesCache.behaviorRule.getSignimpcoinsmax();
        for (int i = 0 ; i < impcoinsmax ; i++){
        	if(first>impcoinsmax){
            	break;
            }
            checkInImpIconMap.put(i+1,first);
            first = first + more;
        }
        INVITE_LEVEL1 = SysRulesCache.behaviorRule.getFriendregisterimpcoins();
        INVITE_LEVEL2 = SysRulesCache.behaviorRule.getFriendregisterimpcoins2();
        INVITE_LEVEL3 = SysRulesCache.behaviorRule.getFriendregisterimpcoins3();
        INVITE_LEVEL4 = SysRulesCache.behaviorRule.getFriendregisterimpcoins4();
        INVITE_LEVEL5 = SysRulesCache.behaviorRule.getFriendregisterimpcoins5();
        INVITE_LEVEL1_LIMIT = SysRulesCache.behaviorRule.getFriendregisterimpcoinsmax();
        DAILY_ADDFRIEND = SysRulesCache.behaviorRule.getAddfriendimpcoins();
        DAILY_ADDFRIEND_LIMIT = SysRulesCache.behaviorRule.getAddfriendimpcoinsmax();
        DAILY_SHARE_OUT_RANDOM = 0;
        DAILY_SHARE_OUT_LIMIT = SysRulesCache.behaviorRule.getOutshareawardtimes();
        fieldsMap = null;
    }


    /**
     * 签到送进步币
     */
    public static Map<Integer,Integer> checkInImpIconMap = new HashMap<Integer,Integer>(){};

    /**
     * 站外分享赠送的进步币
     */
    public static int DAILY_SHARE_OUT_RANDOM = 0;//5-10 5-10 算法0
    public static int DAILY_SHARE_OUT_LIMIT = 5; //限制5次

    public static int DAILY_ADDIMP_RANDOM = 1;//3-10 算法1
    public static int DAILY_ADDIMP_LIMIT = SysRulesCache.behaviorRule.getImproveimpcoinstimes();//限制次数
    public static int DAILY_ADDIMP_MIN = SysRulesCache.behaviorRule.getImproveimpcoinsmin(); //单个用户每发1条微进步，可得X个进步币，随机最小值
    public static int DAILY_ADDIMP_MAX = SysRulesCache.behaviorRule.getImproveimpcoinsmax(); //单个用户每发1条微进步，可得X个进步币，随机最大值

    /**
     * 邀请好友注册赠送的进步币
     */
//    public static int INVITE_LEVEL1 = 10;
    public static int INVITE_LEVEL1 = SysRulesCache.behaviorRule.getFriendregisterimpcoins();
    public static int INVITE_LEVEL2 = SysRulesCache.behaviorRule.getFriendregisterimpcoins2();
    public static int INVITE_LEVEL3 = SysRulesCache.behaviorRule.getFriendregisterimpcoins3();
    public static int INVITE_LEVEL4 = SysRulesCache.behaviorRule.getFriendregisterimpcoins4();
    public static int INVITE_LEVEL5 = SysRulesCache.behaviorRule.getFriendregisterimpcoins5();

    public static int INVITE_LEVEL1_LIMIT = SysRulesCache.behaviorRule.getFriendregisterimpcoinsmax();

    /**
     * 添加好友赠送的进步币
     */
    public static int DAILY_ADDFRIEND = SysRulesCache.behaviorRule.getAddfriendimpcoins();

    public static int DAILY_ADDFRIEND_LIMIT = SysRulesCache.behaviorRule.getAddfriendimpcoinsmax();

    //以下两类需要单独接口
    public static int DAILY_FLOWERED = 0;//被送花 进步币系数
//    public static int DAILY_DIAMONDED = 0;//被送钻着获取进步币系数

    /**
     * 随机数规则
     */
    private static Map<Integer,Map<String,Integer>> map = new HashMap<Integer,Map<String,Integer>>(){{
        put(0,new HashMap<String, Integer>(){{
            put("min",5);
            put("max",10);
        }});
        put(1,new HashMap<String, Integer>(){{
            put("min",DAILY_ADDIMP_MIN);
            put("max",DAILY_ADDIMP_MAX);
        }});
    }};

    /**
     * 根据算法规则获取随机数
     * @param code
     * @return
     */
    public static int getRandomCode(int code){
        Map<String,Integer> sMap = map.get(code);
        return RandomUtils.getRandomCode(sMap.get("min"),sMap.get("max"));
    }

    private static Map<String,Integer> fieldsMap = null;

    public static boolean hasContain(String fieldName){
        initFieldMap();
        return fieldsMap.containsKey(fieldName);
    }

    public static Integer getStaticProperty(String fieldName){
        initFieldMap();
        return fieldsMap.get(fieldName);
    }

    private static void initFieldMap(){
        try{
            if(null == fieldsMap){
                fieldsMap = new HashMap<>();
                Class ownerClass = Class.forName(Constant_Imp_Icon.class.getName());
                Field[] fields = ownerClass.getFields();
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    if(field.getType().getName().equals("int")){
                        fieldsMap.put(field.getName(), new Integer(field.getInt(field.getName())));
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}

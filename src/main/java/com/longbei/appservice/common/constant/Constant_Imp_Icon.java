package com.longbei.appservice.common.constant;

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
    /**
     * 签到送进步币
     */
    public static Map<Integer,Integer> checkInImpIconMap = new HashMap<Integer,Integer>(){{
        put(1,1);
        put(2,2);
        put(3,3);
        put(4,4);
        put(5,5);
        put(6,6);
        put(7,7);
    }};

    public static int DAILY_SHARE_RANDOM=0;//5-10 5-10 算法0
    public static int DAILY_SHARE_LIMIT = 3; //限制3次

    public static int DAILY_ADDIMP_RANDOM = 1;//3-10 算法1
    public static int DAILY_ADDIMP_LIMIT = 3;//限制3次

    public static int INVITE_LEVEL1 = 10;

    //以下两类需要单独接口
    public static int DAILY_FLOWERED = 7;//被送花 进步币系数
    public static int DAILY_DIAMONDED = 70;//被送钻着获取进步币系数

    /**
     * 随机数规则
     */
    private static Map<Integer,Map<String,Integer>> map = new HashMap<Integer,Map<String,Integer>>(){{
        put(0,new HashMap<String, Integer>(){{
            put("min",5);
            put("max",10);
        }});
        put(1,new HashMap<String, Integer>(){{
            put("min",3);
            put("max",10);
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
        return fieldsMap.containsKey(fieldName);
    }

    public static Integer getStaticProperty(String fieldName){
        try{
            if(null == fieldsMap){
                fieldsMap = new HashMap<>();
                Class ownerClass = Class.forName(Constant_point.class.getName());
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
        return fieldsMap.get(fieldName);
    }

}

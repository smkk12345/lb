package com.longbei.appservice.common.constant;

import org.apache.commons.collections.map.FixedSizeMap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by smkk on 17/2/24.
 * 龙分  每日上线
 */
public class Constant_point {

    /**
     * 新手任务
     */
    public static int NEW_REGISTER = 30;
    public static int NEW_LOGIN_QQ = 5;
    public static int NEW_LOGIN_WX = 5;
    public static int NEW_LOGIN_WB = 5;
    public static int NEW_BINDING_EMAIL = 10;
    public static int NEW_CERTIFY_USERCARD = 50;//身份证认证
    public static int NEW_CERTIFY_ACADEMIC = 30;//学历认证 academic
    public static int NEW_CERTIFY_JOB = 30;
    public static int NEW_USERINFO = 5;

    /**
     * 日常行为
     */
    public static int DAILY_CHECKIN = 3;//点赞
    public static int DAILY_SHARE=2;
    public static int DAILY_SHARE_LIMIT = 10;
    public static int DAILY_ADDFRIEND = 2;
    public static int DAILY_ADDFRIEND_LIMIT = 20;
    public static int DAILY_FUN = 1;
    public static int DAILY_FUN_LIMIT = 20;
    /**
     * 互动行为
     */
    public static int DAILY_LIKE = 1;
    public static int DAILY_LIKE_LIMIT = 30;
    public static int DAILY_COMMENT = 2;
    public static int DAILY_COMMENT_LIMIT = 40;
    public static int DAILY_FLOWER = 1;
    public static int DAILY_FLOWER_LIMIT = 100;
    public static int DAILY_DIAMOND = 10;
    public static int DAILY_DIAMOND_LIMIT = 100;
    /**
     * 进步行为
     */
    public static int DAILY_ADDIMP = 5;
    public static int DAILY_ADDIMP_LIMIT = 50;
    public static int DAILY_ADDRANK = 7;
    public static int DAILY_ADDRANK_LIMIT = 35;
    public static int DAILY_ADDCLASSROOM = 7;
    public static int DAILY_ADDCLASSROOM_LIMIT = 35;
    public static int DAILY_ADDCIRCLE = 7;
    public static int DAILY_ADDCIRCLE_LIMIT = 35;
    /**
     * 金融消费
     */
    public static int SPONSOR = 1;//赞助
    public static int APPRECIATE = 1;//appreciate 赞赏
    public static int LOTTERY = 1;//lottery 抽奖

    /**
     * 无限制
     */
    public static int INVITE_LEVEL1 = 50;

    

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

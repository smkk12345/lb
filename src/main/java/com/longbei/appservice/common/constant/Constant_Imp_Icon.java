package com.longbei.appservice.common.constant;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by smkk on 17/2/24.
 * 送进步币
 */
public class Constant_Imp_Icon {


    public static int CHECKIN = 0;
//    public static int SHARE =


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

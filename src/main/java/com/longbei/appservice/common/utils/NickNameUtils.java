package com.longbei.appservice.common.utils;

/**
 * Created by lixb on 2017/3/5.
 */
public class NickNameUtils {

    /**
     * 生成唯一的昵称
     * @param p
     * @param sNum
     * @return
     */
    public static String getSingleNickName(String p,String sNum) {
        sNum = sNum.replaceFirst("1","");
//        Calendar now = Calendar.getInstance();
//        String day = now.get(Calendar.DAY_OF_MONTH)+"";
        String sb1  = "";
        String sb2  = "";
        String mToken = sNum.charAt(4) + "" + sNum.charAt(3) + "" + sNum.charAt(1);
        for (int i = 0; i < sNum.length(); i++) {
            char c = sNum.charAt(i);
            if(i == 1||i == 3||i == 4){
                continue;
            }
            if(i%2 == 0){
                sb1 = c+sb1;
            }else{
                sb2 = sb2+c;
            }
        }
//        if(day.length() == 1){
//            day = day+"0";
//        }
//        String result = p+day.charAt(0)+mToken+sb2+sb1+day.charAt(1);
        String result = p+mToken+sb2+sb1;
        return result;
    }

}

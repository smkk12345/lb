package com.longbei.appservice.common.utils;

import java.math.BigDecimal;

/**
 * Created by wangyongzhi 17/3/14.
 */
public class NumberUtil {

    /**
     * double 四舍五入 保留两位小数
     * @param divisor
     * @return
     */
    public static double doubleRound(Double divisor){
        return (double) Math.round(divisor*100)/100;
    }

    public static double round(Double divisor,int scale){
        if(scale<0){
            throw new IllegalArgumentException( "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(divisor));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}

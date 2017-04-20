package com.longbei.appservice.common.utils;

import com.longbei.appservice.common.BaseResp;

/**
 * 结果集处理
 *
 * @author luye
 * @create 2017-02-07 下午5:31
 **/
public class ResultUtil {


    /**
     * 判断返回结果集是否成功
     * @param baseResp
     * @return
     */
    public static boolean isSuccess(BaseResp baseResp){
        if(BaseResp.SUCCESS == baseResp.getCode()){
            return true;
        }
        return false;
    }

    public static boolean fail(BaseResp baseResp){
        if(BaseResp.SUCCESS != baseResp.getCode()){
            return true;
        }
        return false;
    }

}

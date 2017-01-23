package com.longbei.appservice.common.utils;/**
 * Created by luye on 2017/1/23.
 */

import java.util.UUID;

/**
 * mongo 工具类
 *
 * @author luye
 * @create 2017-01-23 下午4:02
 **/
public class MongoUtils {

    public static String UUID(){
        return UUID.randomUUID().toString().replace("-", "_");
    }

}

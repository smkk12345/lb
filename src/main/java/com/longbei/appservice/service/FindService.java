package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;

/**
 * Created by lixb on 2017/3/16.
 */
public interface FindService {
    /**
     * 附近的人
     * @param longitude
     * @param latitude
     * @param userid
     * @param startNum
     * @param endNum
     * @return
     */
    public BaseResp<Object> near(String longitude, String latitude, String userid,String gender, String startNum, String endNum);
}
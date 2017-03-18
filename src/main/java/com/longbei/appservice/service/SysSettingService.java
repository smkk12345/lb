package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.SysLongbeiinfo;

/**
 * Created by smkk on 17/2/17.
 */
public interface SysSettingService {
    BaseResp<Object> selectCompanyInfo();

    /**
     * app 版本更新
     * @param ttype
     * @param version
     * @return
     */
    BaseResp<Object> upGrade(String ttype,String version);

}

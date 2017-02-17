package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.SysLongbeiinfo;

/**
 * Created by smkk on 17/2/17.
 */
public interface SysSettingService {
    BaseResp<Object> selectCompanyInfo();
}

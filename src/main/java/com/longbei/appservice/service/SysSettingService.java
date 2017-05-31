package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.SysAppupdate;
import com.longbei.appservice.entity.SysCommon;
import com.longbei.appservice.entity.SysLongbeiinfo;

import java.util.List;

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

    /**
     * 获取最新的app
     * @param ttype
     * @return
     */
    SysAppupdate selectRecentByKey(String ttype);

    /**
     * 获取分享的相关信息
     * @return
     */
    BaseResp<Object> getShareInfo();

    boolean insertSysCommon(String key,String info,String remark);

    SysCommon getSysCommonByKey(String key);

    List<SysCommon> getSysCommons();

}

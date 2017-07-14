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

    BaseResp<List<SysAppupdate>> findSysAppUpdateList();

    BaseResp<SysAppupdate> getSysAppUpdateDetail(Integer id);

    /**
     * 删除版本更新
     * @param id
     * @return
     */
    BaseResp<Object> deleteSysAppUpdate(Integer id);

    /**
     * 添加版本更新
     * @param ttype
     * @param version
     * @param enforced
     * @param url
     * @param remark
     * @param updateexplain
     * @return
     */
    BaseResp<Object> addSysAppUpdate(String ttype, String version, String enforced, String url, String remark, String updateexplain);

    /**
     * 编辑版本更新
     * @param id
     * @param ttype
     * @param version
     * @param enforced
     * @param url
     * @param remark
     * @param updateexplain
     * @return
     */
    BaseResp<Object> updateSysAppUpdate(Integer id,String ttype, String version, String enforced, String url, String remark, String updateexplain);

    void initSysAppUpdateMap();

}

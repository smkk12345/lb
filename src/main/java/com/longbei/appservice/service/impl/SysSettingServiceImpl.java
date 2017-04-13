package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.constant.Constant_Imp_Icon;
import com.longbei.appservice.common.constant.Constant_point;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.SysAppupdateMapper;
import com.longbei.appservice.dao.SysLongbeiinfoMapper;
import com.longbei.appservice.entity.SysAppupdate;
import com.longbei.appservice.entity.SysLongbeiinfo;
import com.longbei.appservice.service.SysSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by smkk on 17/2/17.
 */
@Service
public class SysSettingServiceImpl implements SysSettingService {
    @Autowired
    private SysLongbeiinfoMapper sysLongbeiinfoMapper;
    @Autowired
    private SysAppupdateMapper sysAppupdateMapper;

    private static Logger logger = LoggerFactory.getLogger(SysSettingServiceImpl.class);

    @Override
    public BaseResp<Object> selectCompanyInfo() {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            SysLongbeiinfo sysLongbeiinfo = sysLongbeiinfoMapper.selectDefault();
            baseResp.setData(sysLongbeiinfo);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch (Exception e){
            logger.error("selectCompanyInfo error and msg={}", e);
        }
        return baseResp;
    }

    private int getMinSize(int ia,int ib){
        if(ia>ib){
            return ib;
        }else {
            return ia;
        }
    }

    @Override
    public BaseResp<Object> upGrade(String ttype, String version) {
        BaseResp<Object> baseResp = new BaseResp<>();
        SysAppupdate app = sysAppupdateMapper.selectRecentByKey(ttype);
        if(null == app){
            baseResp.initCodeAndDesp();
        }
        String newVersion = app.getVersion().replaceAll("\\.", "");
        String cversion = version.replaceAll("\\.","");
        String enforceversion = app.getEnforceversion();
        int size = 0;
        if(!StringUtils.isBlank(enforceversion)){
            enforceversion = enforceversion.replaceAll("\\.","");
            size = getMinSize(newVersion.length(),enforceversion.length());
            size = getMinSize(size,cversion.length());
        }else{
            size = getMinSize(newVersion.length(),cversion.length());
        }

        boolean isNew = false;
        boolean isFor = false;
        for (int i = 0; i < size; i++) {
            int newi = newVersion.charAt(i);

            int versioni = cversion.charAt(i);
            if(!isNew){
                if(versioni < newi){
                    isNew = true;
                }
            }
            if(!StringUtils.isBlank(enforceversion)){
                int enforceveri = enforceversion.charAt(i);
                if(!isFor){
                    if(versioni < enforceveri){
                        isFor = true;
                    }
                }
            }

        }

        if(isNew){
            if(app.getEnforced().equals("1")){
            }else{
                if(isFor){
                    app.setEnforced("1");
                }
            }
            baseResp.setData(app);
        }else {
            baseResp.setData(null);
        }
        baseResp.initCodeAndDesp();
        return baseResp;
    }

    /**
     * 获取版本号相关
     * @param ttype
     * @return
     */
    @Override
    public SysAppupdate selectRecentByKey(String ttype) {
        SysAppupdate app = sysAppupdateMapper.selectRecentByKey(ttype);
        return app;
    }

    /**
     * 获取分享app的相关信息
     * @return
     */
    @Override
    public BaseResp<Object> getShareInfo() {
        BaseResp<Object> baseResp = new BaseResp<Object>();

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("point", Constant_point.INVITE_LEVEL1);//龙分
        map.put("impIcon", Constant_Imp_Icon.INVITE_LEVEL1);//进步币
        map.put("downloadUrl","http://www.baidu.com");

        baseResp.setData(map);
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
    }

}

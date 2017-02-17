package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.SysLongbeiinfoMapper;
import com.longbei.appservice.entity.SysLongbeiinfo;
import com.longbei.appservice.service.SysSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

/**
 * Created by smkk on 17/2/17.
 */
@Service
public class SysSettingServiceImpl implements SysSettingService {
    @Autowired
    private SysLongbeiinfoMapper sysLongbeiinfoMapper;

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
}

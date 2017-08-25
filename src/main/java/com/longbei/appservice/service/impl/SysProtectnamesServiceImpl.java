package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.SysProtectnamesMapper;
import com.longbei.appservice.entity.SysProtectnames;
import com.longbei.appservice.service.SysProtectnamesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SysProtectnamesServiceImpl implements SysProtectnamesService {
    private Logger logger = LoggerFactory.getLogger(SysProtectnamesServiceImpl.class);


    @Autowired
    private SysProtectnamesMapper sysProtectnamesMapper;

    @Override
    public BaseResp<SysProtectnames> selectProtectnames() {
        BaseResp<SysProtectnames> baseResp = new BaseResp<SysProtectnames>();
        try {
            SysProtectnames sysProtectnames = sysProtectnamesMapper.selectProtectnames();
            if(null ==sysProtectnames) {
                SysProtectnames sysProtectnames1= new SysProtectnames();
                sysProtectnames1.setCreatetime(new Date());
                sysProtectnamesMapper.insert(sysProtectnames1);
                sysProtectnames = sysProtectnamesMapper.selectProtectnames();
            }
            baseResp.setData(sysProtectnames);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        }
        catch (Exception e) {
            logger.error("selectProtectnames error and msg={}",e);
        }
        return baseResp;
    }

    @Override
    public 	BaseResp<Object> updateProtectNames(String nicknames,String protectNamesId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        SysProtectnames sysProtectnames = new SysProtectnames();
        sysProtectnames.setId(Integer.parseInt(protectNamesId));
        sysProtectnames.setNicknames(nicknames);
        sysProtectnames.setUpdatetime(new Date());
        try {
            int n = sysProtectnamesMapper.updateProtectNames(sysProtectnames);
            if(n >= 1){
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateProtectNames error and msg={}",e);
        }
        return baseResp;
    }

}

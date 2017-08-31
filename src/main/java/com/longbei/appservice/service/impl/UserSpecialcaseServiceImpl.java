package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.syscache.SysRulesCache;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.UserSpecialcaseMapper;
import com.longbei.appservice.entity.UserSpecialcase;
import com.longbei.appservice.service.UserSpecialcaseService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserSpecialcaseServiceImpl implements UserSpecialcaseService {
    private Logger logger = LoggerFactory.getLogger(UserSpecialcaseServiceImpl.class);


    @Autowired
    private UserSpecialcaseMapper userSpecialcaseMapper;

    @Override
    public BaseResp<UserSpecialcase> selectUserSpecialcase() {
        BaseResp<UserSpecialcase> baseResp = new BaseResp<UserSpecialcase>();
        try {
            UserSpecialcase userSpecialcase = userSpecialcaseMapper.selectUserSpecialcase();
            if(null ==userSpecialcase) {
                UserSpecialcase userSpecialcase1= new UserSpecialcase();
                userSpecialcase1.setCreatetime(new Date());
                userSpecialcaseMapper.insert(userSpecialcase1);
            }
            baseResp.setData(userSpecialcase);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        }
        catch (Exception e) {
            logger.error("selectUserSpecialcase error and msg={}",e);
        }
        return baseResp;
    }

    @Override
    public 	BaseResp<Object> updateUserSpecialcase(UserSpecialcase userSpecialcase) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        Date date = new Date();
        userSpecialcase.setUpdatetime(date);
        try {
            updateUserSpecialcase();
            int n = userSpecialcaseMapper.updateUserSpecialcase(userSpecialcase);
            if(n >= 1){
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateUserSpecialcase error and msg={}",e);
        }
        return baseResp;
    }


    @Override
    public void updateUserSpecialcase() {
        try {
            UserSpecialcase userSpecialcase = userSpecialcaseMapper.selectUserSpecialcase();
            if(null != userSpecialcase){
                String words = userSpecialcase.getNoSwitchLogin();
                if(!StringUtils.isBlank(words)){
                    Set<String> set = new HashSet<>();
                    words = words.replaceAll( "\\s", "" );
                    words = words.replaceAll("　"," ");
                    words = words.replaceAll("，",",");
                    String[] sArr = words.split(",");
                    CollectionUtils.addAll(set,sArr);
                    SysRulesCache.userSpecialcaseMobileSet = set;
                }
            }
        }catch (Exception e){
        }
    }

}

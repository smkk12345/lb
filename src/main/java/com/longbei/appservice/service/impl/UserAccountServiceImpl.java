package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.dao.UserAccountMapper;
import com.longbei.appservice.dao.redis.SpringJedisDao;
import com.longbei.appservice.entity.UserAccount;
import com.longbei.appservice.service.UserAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("userAccountService")
public class UserAccountServiceImpl implements UserAccountService {

    private static Logger logger = LoggerFactory.getLogger(UserAccountServiceImpl.class);

    @Autowired
    private UserAccountMapper userAccountMapper;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private SpringJedisDao springJedisDao;

    @Override
    public BaseResp<UserAccount> selectUserAccountByUserId(Long userId) {
        BaseResp<UserAccount> baseResp = new BaseResp<UserAccount>();
        try {
            UserAccount userAccount = userAccountMapper.selectUserAccountByUserId(userId);
            if(null != userAccount)
            {
                baseResp.setData(userAccount);
                switch (userAccount.getFreezetime()+"") {
                    case (60*60*8L)+"":
                        baseResp.getExpandData().put("strFreezeTime","0");
                        break;
                    case (60*60*24*3L)+"":
                        baseResp.getExpandData().put("strFreezeTime","1");
                        break;
                    case (60*60*24*7L)+"":
                        baseResp.getExpandData().put("strFreezeTime","2");
                        break;
                    case (60*60*24*30*3L)+"":
                        baseResp.getExpandData().put("strFreezeTime","3");
                        break;
                    case (60*60*24*365*150L)+"":
                        baseResp.getExpandData().put("strFreezeTime","4");
                        break;
                }
            }else {
                baseResp.getExpandData().put("strFreezeTime",null);
            }
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        }
        catch (Exception e) {
            logger.error("selectUserAccountByUserId error and msg={}",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> insertUserAccount(UserAccount userAccount,String strFreezeTime){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        String strDate = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
        userAccount.setStatus("1");
        userAccount.setCreatetime(strDate);
        userAccount.setUpdatetime(strDate);
        switch (strFreezeTime) {
            case "0":
                userAccount.setFreezetime(Constant.FREEZETIME_EIGHT_HOURS);
                break;
            case "1":
                userAccount.setFreezetime(Constant.FREEZETIME_THREE_DAYS);
                break;
            case "2":
                userAccount.setFreezetime(Constant.FREEZETIME_ONE_WEEK);
                break;
            case "3":
                userAccount.setFreezetime(Constant.FREEZETIME_THREE_MONTHS);
                break;
            case "4":
                userAccount.setFreezetime(Constant.FREEZETIME_FOREVER);
                break;
        }
        try {
            int n = userAccountMapper.insertUserAccount(userAccount);
            if(n == 1){
                boolean flag = springJedisDao.del("userid&token&"+userAccount.getUserid());
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertUserAccount error and msg={}",e);
        }
        return baseResp;
    }


    @Override
    public 	BaseResp<Object> updateUserAccountByUserId(UserAccount userAccount,String strFreezeTime) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        userAccount.setUpdatetime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
        switch (strFreezeTime) {
            case "0":
                userAccount.setFreezetime(Constant.FREEZETIME_EIGHT_HOURS);
                break;
            case "1":
                userAccount.setFreezetime(Constant.FREEZETIME_THREE_DAYS);
                break;
            case "2":
                userAccount.setFreezetime(Constant.FREEZETIME_ONE_WEEK);
                break;
            case "3":
                userAccount.setFreezetime(Constant.FREEZETIME_THREE_MONTHS);
                break;
            case "4":
                userAccount.setFreezetime(Constant.FREEZETIME_FOREVER);
                break;
        }
        try {
            int n = userAccountMapper.updateUserAccountByUserId(userAccount);
            if(n >= 1){
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateUserAccountByUserId error and msg={}",e);
        }
        return baseResp;
    }
}
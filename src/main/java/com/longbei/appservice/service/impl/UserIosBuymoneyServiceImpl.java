package com.longbei.appservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.UserIosBuymoneyMapper;
import com.longbei.appservice.entity.UserIosBuymoney;
import com.longbei.appservice.service.UserIosBuymoneyService;

@Service
public class UserIosBuymoneyServiceImpl implements UserIosBuymoneyService {

	@Autowired
    private UserIosBuymoneyMapper userIosBuymoneyMapper;

    private static Logger logger = LoggerFactory.getLogger(UserIosBuymoneyServiceImpl.class);
	
	@Override
	public BaseResp<List<UserIosBuymoney>> selectMoneyAllList() {
		BaseResp<List<UserIosBuymoney>> baseResp = new BaseResp<>();
        try{
        	List<UserIosBuymoney> list = userIosBuymoneyMapper.selectMoneyAllList();
        	if(null != list && list.size()>0){
        		baseResp.setData(list);
        	}else{
        		baseResp.setData(new ArrayList<UserIosBuymoney>());
        	}
        	baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch (Exception e){
            logger.error("selectMoneyAllList ", e);
        }
        return baseResp;
	}

}

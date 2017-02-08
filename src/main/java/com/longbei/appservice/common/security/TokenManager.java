package com.longbei.appservice.common.security;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.redis.SpringJedisDao;
//import com.longbei.appservice.dao.JedisDao;
import com.longbei.appservice.service.api.FeignApiProxy;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by luye on 2017/1/14.
 */
@Service
public class TokenManager {

	@Autowired
	private SpringJedisDao springJedisDao;
	
	private static Logger logger = LoggerFactory.getLogger(TokenManager.class);
	
    private String getTokenFromTargetService(String servicename){
        if(Constant.SERVER_USER_SERVICE.equals(servicename)){
            BaseResp<Object> baseResp = FeignApiProxy.userBasicService.getServiceToken(Constant.SERVER_APP_SERVICE,12*30*24*3600*1000);
            if(baseResp.getCode() == Constant.STATUS_SYS_00){
                return (String) baseResp.getData();
            }
        }
        return null;
    }


    public String getTokenFromCache(String servicename){

        //从redis中获取token 通过servicename
        String token = springJedisDao.get(Constant.SERVER_USER_SERVICE);
        if(StringUtils.isBlank(token)){
            token = getTokenFromTargetService(servicename);
            //将token放入redis
            springJedisDao.set(Constant.SERVER_USER_SERVICE, token);
        }
        logger.info("token is "+token);
        JSONObject tokenjson = JSONObject.fromObject(token);
        
        if(Long.parseLong((String) tokenjson.get("exp")) <= new java.util.Date().getTime()){
            token = getTokenFromTargetService(servicename);
            //将token放入redis
            springJedisDao.set(Constant.SERVER_USER_SERVICE, token);
            tokenjson = JSONObject.fromObject(token);
        }
        return (String) tokenjson.get("token");
    }




}

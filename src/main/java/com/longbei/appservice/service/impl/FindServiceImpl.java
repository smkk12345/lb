package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.service.FindService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lixb on 2017/3/16.
 */
@Service
public class FindServiceImpl implements FindService{

    private static Logger logger = LoggerFactory.getLogger(FindServiceImpl.class);
    @Autowired
    private UserMongoDao userMongoDao;

    @Override
    public BaseResp<Object> near(String longitude, String latitude, String userid, String startNum, String endNum) {
        BaseResp<Object> baseResp = new BaseResp<>();

        try {
            List<AppUserMongoEntity> list = userMongoDao.findNear(
                    Double.parseDouble(longitude),Double.parseDouble(latitude),50.00,
                    Integer.parseInt(startNum),Integer.parseInt(endNum));
            for (int i = 0; i < list.size(); i++) {
                AppUserMongoEntity appuser = list.get(i);
                //判断是否好友 是否关注 是否粉丝等等

            }
            baseResp.setData(list);
            return baseResp.initCodeAndDesp();
        }catch (Exception e){
            logger.error("findNear error longitude={},latitude={}," +
                    "userid={},startNum={},endNum={}",longitude,latitude,userid,startNum,endNum,e);
        }
        return baseResp;
    }

}

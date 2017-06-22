package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.service.FindService;
import com.longbei.appservice.service.UserRelationService;

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
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserRelationService userRelationService;

    @SuppressWarnings("unchecked")
	@Override
    public BaseResp<Object> near(String longitude, String latitude, String userid,String gender, String startNum, String pageSize) {
        BaseResp<Object> baseResp = new BaseResp<>();

        try {
            List<AppUserMongoEntity> list = userMongoDao.findNear(
                    Double.parseDouble(longitude),Double.parseDouble(latitude),50.00,gender,
                    Integer.parseInt(startNum),Integer.parseInt(pageSize));
            for (int i = 0; i < list.size(); i++) {
                AppUserMongoEntity appuser = list.get(i);
                //获取好友昵称
				String remark = userRelationService.selectRemark(Long.parseLong(userid), Long.parseLong(appuser.getId()), "0");
				if(!StringUtils.isBlank(remark)){
					appuser.setNickname(remark);
				}
                //判断是否好友 是否关注 是否粉丝等等
                try {
                    UserInfo userInfo = userInfoMapper.getByUserName(appuser.getUsername());
                    if(null != userInfo)
                    {
                        appuser.setBrief(userInfo.getBrief());
                        appuser.setSex(userInfo.getSex());
                    }
                }catch (Exception e){
                    logger.error("getByUserName error nad username={}",appuser.getUsername(),e);
                }
                //屏蔽手机号
                appuser.setUsername("");
            }
            baseResp.setData(list);
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch (Exception e){
            logger.error("findNear error longitude={},latitude={}," +
                    "userid={},startNum={},endNum={}",longitude,latitude,userid,startNum,pageSize,e);
        }
        return baseResp;
    }

}

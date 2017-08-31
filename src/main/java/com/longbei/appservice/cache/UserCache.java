package com.longbei.appservice.cache;

import com.longbei.appservice.common.constant.RedisCacheNames;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 用户相关缓存
 *
 * @author luye
 * @create 2017-08-30 下午4:46
 **/
@Service
public class UserCache {

    private static Logger logger = LoggerFactory.getLogger(UserCache.class);
    @Autowired
    private UserInfoMapper userInfoMapper;


    @Cacheable(cacheNames = RedisCacheNames._USER_GRADE,key = "#userid")
    public int selectUserGradeByUserId(String userid) throws Exception{
        int grade = 0;
        try {
            UserInfo userInfo = userInfoMapper.selectByUserid(Long.parseLong(userid));
            grade = userInfo.getGrade();
        } catch (Exception e) {
            throw e;
        }
        return grade;
    }





}

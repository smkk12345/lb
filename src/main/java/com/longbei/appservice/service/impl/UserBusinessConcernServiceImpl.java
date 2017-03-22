package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.dao.UserBusinessConcernMapper;
import com.longbei.appservice.entity.UserBusinessConcern;
import com.longbei.appservice.service.UserBusinessConcernService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wangyongzhi 17/3/21.
 */
@Service("userBusinessConcern")
public class UserBusinessConcernServiceImpl extends BaseServiceImpl implements UserBusinessConcernService {
    private static Logger logger = LoggerFactory.getLogger(UserBusinessConcernServiceImpl.class);
    @Autowired
    private UserBusinessConcernMapper userBusinessConcernMapper;

    /**
     * 增加关注
     * @param userid 用户id
     * @param businessType 关注的类型
     * @param businessId 关注的id
     * @return
     */
    @Override
    public BaseResp<Object> insertUserBusinessConcern(Long userid, Integer businessType, Long businessId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            UserBusinessConcern userBusinessConcern = new UserBusinessConcern(userid,businessType,businessId);
            int row = this.userBusinessConcernMapper.insertUserBusinessConcern(userBusinessConcern);
            if(row > 0){
                return baseResp.ok();
            }
        }catch(Exception e){
            logger.error("insert userBusinessConcern error userBusinessConcern:{}",userBusinessConcernMapper.toString());
            printException(e);
        }
        return baseResp;
    }

    /**
     * 取消关注
     * @param userid
     * @param businessType
     * @param businessId
     * @return
     */
    @Override
    public BaseResp<Object> deleteUserBusinessConcern(Long userid, Integer businessType, Long businessId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            int row = this.userBusinessConcernMapper.deleteUserBusinessConcern(userid,businessType,businessId);
            return baseResp.ok();
        }catch(Exception e){
            logger.error("delete userBusinessConcern error userid:{} businessType:{} businessId:{}",userid,businessType,businessId);
            printException(e);
        }
        return baseResp;
    }
}

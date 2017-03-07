package com.longbei.appservice.service.impl;

import com.longbei.appservice.dao.SnsFansMapper;
import com.longbei.appservice.entity.SnsFans;
import com.longbei.appservice.service.FansService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by wangyongzhi 17/3/7.
 */
public class FansServiceImpl extends BaseServiceImpl implements FansService {

    @Autowired
    private SnsFansMapper snsFansMapper;

    /**
     * 校验fanseId是否关注了userId
     * @param userId
     * @param fansId
     * @return
     */
    @Override
    public boolean checkIsFans(Long userId, Long fansId) {
        SnsFans snsFans = snsFansMapper.selectByUidAndLikeid(userId,fansId);
        if(snsFans == null){
            return false;
        }
        return true;
    }
}

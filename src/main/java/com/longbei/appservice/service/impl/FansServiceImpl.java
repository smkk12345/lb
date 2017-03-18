package com.longbei.appservice.service.impl;

import com.longbei.appservice.dao.SnsFansMapper;
import com.longbei.appservice.entity.SnsFans;
import com.longbei.appservice.service.FansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wangyongzhi 17/3/7.
 */
@Service("fansService")
public class FansServiceImpl extends BaseServiceImpl implements FansService {

    @Autowired
    private SnsFansMapper snsFansMapper;

    /**
     * 校验fanseId是否关注了userId
     * @param fansId 粉丝Id
     * @param userId 被关注的人
     * @return
     */
    @Override
    public boolean checkIsFans(Long fansId, Long userId) {
        SnsFans snsFans = snsFansMapper.selectByUidAndLikeid(fansId,userId);
        if(snsFans == null){
            return false;
        }
        return true;
    }
}

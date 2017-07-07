package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.constant.Constant;
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


}

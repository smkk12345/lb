package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.dao.ImproveCircleMapper;
import com.longbei.appservice.entity.ImproveCircle;
import com.longbei.appservice.service.ImproveCircleService;
import com.longbei.appservice.service.ImproveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by wangyongzhi 17/3/1.
 */
@Service("improveCircleService")
public class ImproveCircleServiceImpl implements ImproveCircleService {
    @Autowired
    private ImproveCircleMapper improveCircleMapper;
    @Autowired
    private ImproveService improveService;

}

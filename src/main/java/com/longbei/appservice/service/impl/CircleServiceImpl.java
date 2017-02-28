package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.CircleMapper;
import com.longbei.appservice.entity.Circle;
import com.longbei.appservice.service.CircleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wyz on 17/2/28.
 */
@Service("circleService")
public class CircleServiceImpl implements CircleService {

    @Autowired
    private CircleMapper circleMappler;

    @Autowired
    private IdGenerateService idGenerateService;

    @Override
    public BaseResp<Object> relevantCircle(String circleName,Integer startNo,Integer pageSize) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("circleName",circleName);
        map.put("startNo",startNo);
        map.put("pageSize",pageSize);
        List<Circle> circleList = circleMappler.findRelevantCircle(map);
        baseResp.setData(circleList);
        baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        return baseResp;
    }

    @Override
    public BaseResp<Object> insertCircle(String userId, String circleTitle, String circlephotos, String circlebrief,
                                         Integer circleinvoloed,String ptype,Boolean ispublic,Boolean needconfirm,Boolean creategoup) {
        Circle circle = new Circle();
        circle.setCreatetime(new Date());
        circle.setUpdatetime(new Date());
        circle.setCircleid(idGenerateService.getUniqueIdAsLong());
        circle.setCircletitle(circleTitle);
        circle.setCirclephotos(circlephotos);
        circle.setCirclebrief(circlebrief);
        circle.setCreateuserid(Long.parseLong(userId));
        circle.setCircleinvoloed(circleinvoloed);
        circle.setPtype(ptype);
        circle.setIspublic(ispublic);
        circle.setNeedconfirm(needconfirm);
        int row = circleMappler.insert(circle);
        BaseResp baseResp = new BaseResp();
        if(row > 0){
            if(creategoup){//需要创建龙信群

            }
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_01,Constant.RTNINFO_SYS_01);
    }

    /**
     * 校验兴趣圈名字是否可用
     * @param circleTitle
     * @return
     */
    @Override
    public boolean checkCircleTitle(String circleTitle) {
        Circle circle = circleMappler.findCircleByCircleTitle(circleTitle);
        if(circle == null){
            return true;
        }
        return false;
    }
}

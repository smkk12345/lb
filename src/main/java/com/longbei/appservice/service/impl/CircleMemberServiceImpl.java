package com.longbei.appservice.service.impl;

import com.longbei.appservice.dao.CircleMembersMapper;
import com.longbei.appservice.service.CircleMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangyongzhi 17/3/2.
 */
@Service("circleMemberService")
public class CircleMemberServiceImpl implements CircleMemberService {

    @Autowired
    private CircleMembersMapper circleMembersMapper;

    /**
     * 修改用户的某个兴趣圈中的总赞数以及获得的花(在原来的基础上增加)
     * @param userId 用户id
     * @param circleId 圈子id
     * @param likes 增加的赞数
     * @param flowers 增加的花 数量
     * @param diamonds 增加的 钻石 数量
     */
    @Override
    public boolean updateCircleMemberInfo(Long userId, String circleId, Integer likes, Integer flowers, Integer diamonds) {
        if(userId == null || circleId == null || (likes == null && flowers == null && diamonds == null)){
            return false;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId",userId);
        map.put("circleId",circleId);
        if(likes != null){
            map.put("likes",likes);
        }
        if(flowers != null){
            map.put("flowers",flowers);
        }
        if(diamonds != null){
            map.put("diamonds",diamonds);
        }
        map.put("updateTime",new Date());
        int row = circleMembersMapper.updateCircleMemberInfo(map);
        if(row > 0){
            return true;
        }
        return false;
    }

    /**
     * 更改用户在圈子中的进步条数
     * @param userid
     * @param circleId
     * @param num
     * @return
     */
    @Override
    public boolean updateCircleMemberIcount(Long userid, Long circleId, int num) {
        int updateNum = this.circleMembersMapper.updateCircleMembersIcount(userid,circleId,num);
        return false;
    }
}

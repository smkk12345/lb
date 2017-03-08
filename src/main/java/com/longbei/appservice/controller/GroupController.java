package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangyongzhi 17/3/8.
 */
@RestController
@RequestMapping(value = "group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    /**
     * 新建群组
     * @param userIds 新建群时,加入的用户id
     * @param mainGroupUserId 群主用户id
     * @param type 群组关联的类型 1.榜 2.兴趣圈 3.教室
     * @param typeId 群组关联的类型id
     * @param groupName 群组名称
     * @return
     */
    public BaseResp<Object> createGroup(String[] userIds,String mainGroupUserId,Integer type,Long typeId,String groupName,Boolean needConfirm){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(mainGroupUserId == null || StringUtils.isEmpty(groupName)){
            return baseResp.fail("参数错误");
        }
        baseResp = groupService.createGroup(userIds,mainGroupUserId,type,typeId,groupName,needConfirm);
        return baseResp;
    }

}

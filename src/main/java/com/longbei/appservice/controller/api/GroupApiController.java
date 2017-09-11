package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by wangyongzhi 17/9/4.
 */
@RestController
@RequestMapping("api/group")
public class GroupApiController {

    @Autowired
    private GroupService groupService;

    /**
     * 批量创建叶圣陶群组
     * @param mainGroupUserid
     * @param gradeid 代表年级权限 的id
     * @param groupname
     * @param managerid 代表机构管理员的用户id
     * @return
     */
    @RequestMapping(value="batchCreateYSTGroup")
    public BaseResp<List<Map<String,Object>>> batchCreateYSTGroup(Long mainGroupUserid, String gradeid, String groupname,Long managerid){
        BaseResp<List<Map<String,Object>>> baseResp = new BaseResp<List<Map<String,Object>>>();
        if(mainGroupUserid == null || StringUtils.isEmpty(groupname) || StringUtils.isEmpty(gradeid) || managerid == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.groupService.batchCreateGroup(mainGroupUserid,gradeid,groupname,managerid);
        return baseResp;
    }

    /**
     * 加入群组
     * @param userid
     * @param groupid
     * @param groupuserid 群主id 当groupid群满了的时候,新建一个群,群主id即是groupuserid
     * @param groupname 群名称 当groupid群满了的时候,新建一个群,群名称即是groupname
     * @return
     */
    @RequestMapping(value="joinYSTInstitutionGroup")
    public BaseResp<Object> joinYSTInstitutionGroup(Long userid,Long groupid,Long groupuserid,String groupname,Long managerid){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userid == null || groupid == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.groupService.joinYSTInstitutionGroup(userid,groupid,groupuserid,groupname,managerid);
        return baseResp;
    }
}

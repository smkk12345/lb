package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.dao.SnsGroupMapper;
import com.longbei.appservice.dao.SnsGroupMembersMapper;
import com.longbei.appservice.entity.SnsGroup;
import com.longbei.appservice.entity.SnsGroupMembers;
import com.longbei.appservice.service.GroupService;
import com.longbei.appservice.service.api.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by wangyongzhi 17/3/8.
 */
@Service("groupService")
public class GroupServiceImpl extends BaseServiceImpl implements GroupService {
    private static Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    @Autowired
    private IdGenerateService idGenerateService;
    @Autowired
    private SnsGroupMapper snsGroupMapper;
    @Autowired
    private SnsGroupMembersMapper snsGroupMembersMapper;

    /**
     * 新建群组
     * @param userIds
     * @return
     */
    @Transactional
    @Override
    public BaseResp<Object> createGroup(String[] userIds, String mainGroupUserId, Integer type, Long typeId, String groupName, Boolean needConfirm) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            if((type != null && typeId == null) || (type == null || typeId != null)){
                return baseResp.fail("参数错误");
            }
            //先调用融云创建群,融云创建群成功后,再在数据库创建数据
            Long groupId=idGenerateService.getUniqueIdAsLong();

            Set<String> userIdSet = new HashSet<String>();
            if(userIds != null && userIds.length > 0){
                for(String userId:userIds){
                    userIdSet.add(userId);
                }
            }
            userIdSet.add(mainGroupUserId);
            String[] newUserIds = userIdSet.toArray(new String[]{});

            //1.调用融云 创建群组
            BaseResp rongyunResp = HttpClient.rongYunService.createGroup(newUserIds,groupId,groupName);
            if(rongyunResp.getCode() != 0){
                return baseResp.fail("系统异常");
            }

            //2.数据库新建群组
            SnsGroup snsGroup = new SnsGroup();
            snsGroup.setCreatetime(new Date());
            snsGroup.setUpdatetime(new Date());
            snsGroup.setGroupid(groupId);
            if(type != null){
                snsGroup.setGrouptype(type);
                snsGroup.setRelateid(typeId);
            }
            snsGroup.setNeedconfirm(needConfirm);
            int row = snsGroupMapper.insertSelective(snsGroup);
            if(row < 1){
                return BaseResp.fail("系统异常");
            }

            //3.插入群成员
            SnsGroupMembers snsGroupMembers = new SnsGroupMembers();
            snsGroupMembers.setCreatetime(new Date());
            snsGroupMembers.setUpdatetime(new Date());
            snsGroupMembers.setGroupid(groupId);
            if(needConfirm){
                snsGroupMembers.setStatus(1);
            }else{
                snsGroupMembers.setStatus(0);
            }

            Map<String,Object> map = new HashMap<String,Object>();
            map.put("userIds",newUserIds);
            map.put("snsGroupMembers",snsGroupMembers);
            int insertGroupMemebersRow = snsGroupMembersMapper.batchInsertGroupMembers(map);

        }catch(Exception e){
            logger.error("create rongyun group error userIds:{} mainGroupUserId:{} type:{} typeId:{} groupName:{}",userIds,mainGroupUserId,type,typeId,groupName);
            printExceptionAndRollBackTransaction(e);
        }
        return baseResp;
    }
}

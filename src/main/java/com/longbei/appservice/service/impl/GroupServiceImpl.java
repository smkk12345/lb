package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.SnsGroupMapper;
import com.longbei.appservice.dao.SnsGroupMembersMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.dao.redis.SpringJedisDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.SnsGroup;
import com.longbei.appservice.entity.SnsGroupMembers;
import com.longbei.appservice.service.GroupService;
import com.longbei.appservice.service.api.HttpClient;
import net.sf.json.JSONArray;
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
    @Autowired
    private SpringJedisDao springJedisDao;
    @Autowired
    private UserMongoDao userMongoDao;

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
            if((type != null && typeId == null) || (type == null && typeId != null)){
                return baseResp.fail("参数错误");
            }
            if(needConfirm == null){
                needConfirm = true;
            }
            //先调用融云创建群,融云创建群成功后,再在数据库创建数据
            Long groupId = null;
            do{
                String tempId = IdGenerateService.getRandomString(true,SnsGroup.groupIdLength);
                if(!springJedisDao.sIsMember(SnsGroup.groupIdSet,tempId)){
                    springJedisDao.sAdd(SnsGroup.groupIdSet,tempId);
                    groupId = Long.parseLong(tempId);
                }
            }while(groupId == null);

            Set<String> userIdSet = new HashSet<String>();
            if(userIds != null && userIds.length > 0){
                for(String userId:userIds){
                    userIdSet.add(userId);
                }
            }
            userIdSet.add(mainGroupUserId);
            String userIdString = userIdSet.toString();
            userIdString = userIdString.substring(1,userIdString.length()-1);
            String[] newUserIds = userIdString.split(",");

            //1.调用融云 创建群组
            BaseResp rongyunResp = HttpClient.rongYunService.createGroup(userIdString,groupId,groupName);
            if(rongyunResp.getCode() != 0){
                return baseResp.fail("系统异常");
            }

            //2.数据库新建群组
            SnsGroup snsGroup = new SnsGroup();
            snsGroup.setCreatetime(new Date());
            snsGroup.setUpdatetime(new Date());
            snsGroup.setGroupid(groupId);
            snsGroup.setMainuserid(Long.parseLong(mainGroupUserId));
            if(type != null){
                snsGroup.setGrouptype(type);
                snsGroup.setRelateid(typeId);
            }
            if(needConfirm){
                snsGroup.setCurrentnum(1);
            }else{
                snsGroup.setCurrentnum(newUserIds.length);
            }
            snsGroup.setGroupname(groupName);
            snsGroup.setNeedconfirm(needConfirm);
            snsGroup.setMaxnum(1000);
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
                snsGroupMembers.setStatus(0);
            }else{
                snsGroupMembers.setStatus(1);
            }
            List<AppUserMongoEntity> userList = new ArrayList<AppUserMongoEntity>();
            for(String tempUserId:newUserIds){
                AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(tempUserId);
                if(appUserMongoEntity != null){
                    userList.add(appUserMongoEntity);
                }
            }

            Map<String,Object> map = new HashMap<String,Object>();
            map.put("userList",userList);
            map.put("snsGroupMembers",snsGroupMembers);
            map.put("mainUserId",mainGroupUserId);
            int insertGroupMemebersRow = snsGroupMembersMapper.batchInsertGroupMembers(map);
            if(insertGroupMemebersRow > 0){
                return baseResp.ok();
            }
            return baseResp.fail();
        }catch(Exception e){
            logger.error("create rongyun group error userIds:{} mainGroupUserId:{} type:{} typeId:{} groupName:{}",userIds,mainGroupUserId,type,typeId,groupName);
            printExceptionAndRollBackTransaction(e);
            return baseResp.fail("系统异常");
        }
    }

    /**
     * 更新群组信息
     * @param userId 用户id
     * @param groupId 群组id
     * @param groupName 新的群组名称
     * @return
     */
    @Override
    public BaseResp<Object> updateGroupInfo(Long userId, String groupId, String groupName,Boolean needConfirm,String notice) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            //根据userId 和 groupId查询群组 校验权限
            SnsGroup snsGroup = this.snsGroupMapper.selectByGroupIdAndMainUserId(groupId,userId);
            if(snsGroup == null){
                return baseResp.fail("抱歉,您无权操作该群组!");
            }

            if(StringUtils.isNotEmpty(groupName)){
                //更新群组名称
                BaseResp<Object> ryResult = HttpClient.rongYunService.updateGroupName(groupId,groupName);
                if(ryResult.getCode() != 0){
                    return baseResp.fail();
                }
            }
            int row = this.snsGroupMapper.updateGroupInfo(groupId,groupName,needConfirm,notice,new Date());
            if(row > 0){
                return baseResp.ok();
            }
        }catch(Exception e){
            logger.error("update groupInfo error userId:{} groupId:{} groupName:{} needConfirm:{} notice:{}",userId,groupId,groupName,needConfirm,notice);
            printException(e);
        }
        return baseResp.fail();
    }

    /**
     * 更改用户在群组中的昵称
     * @param userId 用户id
     * @param groupId 群组id
     * @param nickName 昵称
     * @return
     */
    @Override
    public BaseResp<Object> updateGroupMemberNickName(Long userId, String groupId, String nickName) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            //校验用户是否在群组中
            SnsGroupMembers snsGroupMembers = snsGroupMembersMapper.findByUserIdAndGroupId(userId,groupId);
            if(snsGroupMembers == null || snsGroupMembers.getStatus() != 1){
                return baseResp.fail("抱歉,你无法修改用户昵称!");
            }
            int row= snsGroupMembersMapper.updateSnsGroupMemberInfo(userId,groupId,nickName,null);
            if(row > 0){
                return baseResp.ok();
            }
        }catch(Exception e){
            logger.error("update groupmember nickname error",userId,groupId,nickName);
            printException(e);
        }

        return baseResp.fail("系统异常");
    }

    /**
     * 申请加群
     * @param userIds
     * @param invitationUserId
     * @param groupId
     * @param remark
     * @return
     */
    @Transactional
    @Override
    public BaseResp<Object> insertGroupMember(String[] userIds, Long invitationUserId, String groupId, String remark) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            //根据groupiD查询群
            SnsGroup snsGroup = this.snsGroupMapper.selectByGroupIdAndMainUserId(groupId,null);
            if(snsGroup == null){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }
            if(userIds.length+snsGroup.getCurrentnum() > snsGroup.getMaxnum()){
                return baseResp.fail("抱歉,当前群已达人数上限!");
            }
            List<String> updateGroupMemberList = new ArrayList<String>();
            List<String> userIdList = new ArrayList<String>(Arrays.asList(userIds));
            int insertGroupNum = 0;

            //根据多个用户id和groupid查询数据 校验用户是否已经在群中
            Map<String,Object> parameterMap = new HashMap<String,Object>();
            parameterMap.put("userIds",userIdList);
            parameterMap.put("groupId",groupId);
            List<SnsGroupMembers> snsGroupMembersList = this.snsGroupMembersMapper.selectSnsGroupMembersList(parameterMap);

            if(snsGroupMembersList != null && snsGroupMembersList.size() > 0){
                int status = 0;
                for(SnsGroupMembers snsGroupMembers:snsGroupMembersList){
                    if(snsGroupMembers.getStatus() == 1){
                        userIdList.remove(snsGroupMembers.getUserid()+"");
                        continue;
                    }else{
                        if(snsGroup.getNeedconfirm() && snsGroupMembers.getStatus() == 0 && (invitationUserId == null || (invitationUserId != null && !invitationUserId.equals(snsGroup.getMainuserid())))){
                            return baseResp.ok("您已申请加群,正在等待群主审核~~");
                        }

                        if(!snsGroup.getNeedconfirm() || (invitationUserId != null && invitationUserId.equals(snsGroup.getMainuserid()))){
                            status = 1;
                        }
                        updateGroupMemberList.add(snsGroupMembers.getUserid()+"");
                        userIdList.remove(snsGroupMembers.getUserid()+"");
                    }
                }
                if(updateGroupMemberList.size() > 0){
                    boolean flag =true;
                    if(status == 1){
                        flag = insertRongYunGroupMember(updateGroupMemberList.toArray(new String[]{}),snsGroup.getGroupid()+"",snsGroup.getGroupname());
                    }
                    if(flag){
                        Map<String,Object> updateMap = new HashMap<String,Object>();
                        updateMap.put("updateUserIds",updateGroupMemberList);
                        updateMap.put("groupId",groupId);
                        updateMap.put("status",status);
                        //更改用户加群的状态
                        int updateStatusRow = this.snsGroupMembersMapper.batchUpdateSnsGroupMemberStatus(updateMap);
                        if(updateStatusRow > 0 && status == 1){
                            insertGroupNum += updateGroupMemberList.size();
                        }
                    }
                }
            }

            if(userIdList.size() < 1){
                if(insertGroupNum > 0){
                    updateGroupCurrentNum(groupId,insertGroupNum);
                }
                return baseResp.ok();
            }
            SnsGroupMembers newSnsGroupMember = new SnsGroupMembers();
            newSnsGroupMember.setCreatetime(new Date());
            newSnsGroupMember.setUpdatetime(new Date());
            newSnsGroupMember.setGroupid(Long.parseLong(groupId));
            if(!snsGroup.getNeedconfirm() || (invitationUserId != null && invitationUserId.equals(snsGroup.getMainuserid()))){
                newSnsGroupMember.setStatus(1);
                boolean flag = insertRongYunGroupMember(userIdList.toArray(new String[]{}),snsGroup.getGroupid()+"",snsGroup.getGroupname());
                if(!flag){
                    return baseResp.fail("系统异常");
                }
            }else{
                newSnsGroupMember.setStatus(0);
            }
            if(StringUtils.isNotEmpty(remark)){
                newSnsGroupMember.setRemark(remark);
            }
            List<AppUserMongoEntity> userList = new ArrayList<AppUserMongoEntity>();
            for(String tempUserId:userIdList){
                AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(tempUserId);
                if(appUserMongoEntity != null){
                    userList.add(appUserMongoEntity);
                }
            }

            Map<String,Object> insertMap = new HashMap<String,Object>();
            insertMap.put("snsGroupMembers",newSnsGroupMember);
            insertMap.put("userList",userList);
            int insertRow = snsGroupMembersMapper.batchInsertGroupMembers(insertMap);
            if(insertRow > 0){
                insertGroupNum += userIdList.size();
                if(insertGroupNum > 0){
                    updateGroupCurrentNum(groupId,insertGroupNum);
                }
                return baseResp.ok();
            }else{
                return baseResp.fail("系统异常");
            }
        }catch(Exception e){
            logger.error("insert groupMember error userIds:{} invitationUserId:{} groupId:{} remark:{}",userIds,invitationUserId,groupId,remark);
            printExceptionAndRollBackTransaction(e);
        }
        return baseResp.fail("系统异常");
    }

    /**
     * 批量处理群组成员的状态
     * @param userIds
     * @param groupId
     * @param currentUserId
     * @return
     */
    @Transactional
    @Override
    public BaseResp<Object> batchUpdateGroupMember(String[] userIds, String groupId, Long currentUserId,Integer status) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            //根据groupiD查询群
            SnsGroup snsGroup = this.snsGroupMapper.selectByGroupIdAndMainUserId(groupId,currentUserId);
            if(snsGroup == null){
                return baseResp.fail("您暂时没有权限操作该群组相关信息");
            }
            List<String> deleteList = new ArrayList<String>();
            List<String> userIdList = new ArrayList<String>(Arrays.asList(userIds));
            int insertGroupNum = 0;

            //根据多个用户id和groupid查询数据 校验用户是否已经在群中
            Map<String,Object> parameterMap = new HashMap<String,Object>();
            parameterMap.put("userIds",userIdList);
            parameterMap.put("groupId",groupId);
            List<SnsGroupMembers> snsGroupMembersList = this.snsGroupMembersMapper.selectSnsGroupMembersList(parameterMap);

            if(snsGroupMembersList != null && snsGroupMembersList.size() > 0) {
                for(SnsGroupMembers snsGroupMembers:snsGroupMembersList){
                    if(status.equals(snsGroupMembers.getStatus())){
                        userIdList.remove(snsGroupMembers.getUserid()+"");
                    }else if(status == 2 && snsGroupMembers.getStatus() == 1){//原来是审核通过的,现在变成审核不通过
                        deleteList.add(snsGroupMembers.getUserid()+"");
                    }
                }
            }
            if(deleteList.size() > 0){
                boolean flag = deleteRongYunGroupMember(deleteList.toArray(new String[]{}),groupId);
                if(flag){
                    insertGroupNum = insertGroupNum - deleteList.size();
                }
            }
            if(userIdList.size() == 0){
                updateGroupCurrentNum(groupId,insertGroupNum);
                return baseResp.ok();
            }
            boolean updateFlag = true;
            if(status == 1){//审核通过
                //校验群组人数是否超过限制
                if((snsGroup.getCurrentnum()-deleteList.size()+ userIdList.size()) > snsGroup.getMaxnum()){
                    return baseResp.fail("抱歉,当前群组已达最大人数!");
                }

                updateFlag = insertRongYunGroupMember(userIdList.toArray(new String[]{}),groupId,snsGroup.getGroupname());
                if(updateFlag){
                    insertGroupNum += userIdList.size();
                }
            }
            Map<String, Object> updateMap = new HashMap<String, Object>();
            updateMap.put("updateUserIds", userIdList);
            updateMap.put("groupId", groupId);
            updateMap.put("status", status);
            //更改用户加群的状态
            int updateStatusRow = this.snsGroupMembersMapper.batchUpdateSnsGroupMemberStatus(updateMap);
            if(updateStatusRow > 0){
                if(insertGroupNum != 0){
                    updateGroupCurrentNum(groupId,insertGroupNum);
                }
                return baseResp.ok();
            }
        }catch (Exception e){
            logger.error("batch Update GroupMember status error userIds:{} currentUserId:{} groupId:{} status:{}",userIds,currentUserId,groupId,status);
            printExceptionAndRollBackTransaction(e);
        }
        return baseResp.fail("系统异常");
    }

    /**
     * 退出群组
     * @param userIds 用户id
     * @param groupId 群组id
     * @param currentUserId 当前登录用户id
     * @return
     */
    @Override
    public BaseResp<Object> quietGroup(String[] userIds, String groupId, Long currentUserId) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            //根据groupid查询群
            SnsGroup snsGroup = this.snsGroupMapper.selectByGroupIdAndMainUserId(groupId,null);
            if(snsGroup == null){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }
            if(userIds.length > 1 && !currentUserId.equals(snsGroup.getMainuserid())){
                return baseResp.fail("您暂时没有权限操作该群组相关信息");
            }
            if(userIds.length == 1 && !currentUserId.equals(snsGroup.getMainuserid()) && !currentUserId.equals(userIds[0])  ){
                return baseResp.fail("您暂时没有权限操作该群组相关信息");
            }
            for(String tempUserId:userIds){
                if(tempUserId.equals(snsGroup.getMainuserid()+"")){
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    return baseResp.fail("抱歉,群主不可退出群组!");
                }
            }

            boolean flag = deleteRongYunGroupMember(userIds,groupId);
            if(flag){
                //从数据库删除数据
                Map<String,Object> deleteMap = new HashMap<String,Object>();
                deleteMap.put("userIds",userIds);
                deleteMap.put("groupId",groupId);
                int deleteRow = this.snsGroupMembersMapper.batchDeleteGroupMember(deleteMap);
                if(deleteRow > 0){
                    updateGroupCurrentNum(groupId,-deleteRow);
                    return baseResp.ok();
                }
            }
        }catch(Exception e){
            logger.error("quiet group error userIds:{} groupId:{} currentUserId:{}",userIds,groupId,currentUserId);
        }

        return baseResp.fail("系统异常");
    }

    /**
     * 查询群组成员list
     * @param groupId 群组id
     * @param userId 用户id
     * @param status 查询的状态
     * @return
     */
    @Override
    public BaseResp<Object> groupMemberList(String groupId, Long userId, Integer status) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            Map<String,Object> resultMap = new HashMap<String,Object>();
            //根据groupid查询群
            SnsGroup snsGroup = this.snsGroupMapper.selectByGroupIdAndMainUserId(groupId,null);
            if(snsGroup == null){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }
            if(status == 0 && !userId.equals(snsGroup.getMainuserid())){
                return baseResp.fail("抱歉,您暂时无法查询群组成员列表");
            }
            List<SnsGroupMembers> snsGroupMembersList = this.snsGroupMembersMapper.groupMemberList(groupId,status);

            if(snsGroup.getMainuserid().equals(userId)){
                resultMap.put("isMainUser",true);
            }else{
                resultMap.put("isMainUser",false);
            }
            resultMap.put("snsGroupMembersList",snsGroupMembersList);
            baseResp.setData(resultMap);
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("groupMember List error groupId:{} userId:{} status:{}",groupId,userId,status);
            printException(e);
        }
        return baseResp.fail("系统异常");
    }

    /**
     * 解散群
     * @param userId
     * @param groupId
     * @return
     */
    @Override
    public BaseResp<Object> dismissGroup(Long userId, String groupId) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            Map<String,Object> resultMap = new HashMap<String,Object>();
            //根据groupid查询群
            SnsGroup snsGroup = this.snsGroupMapper.selectByGroupIdAndMainUserId(groupId,userId);
            if(snsGroup == null){
                return baseResp.fail("抱歉,您暂时没有权限解散该群组!");
            }
            BaseResp<Object> ryResult = HttpClient.rongYunService.dismissGroup(userId+"",groupId);
            if(ryResult.getCode() != 0){
                return baseResp.fail("系统异常");
            }
            //删除群组成员
            Map<String,Object> deleteMap = new HashMap<String,Object>();
            deleteMap.put("groupId",groupId);
            int deleteRow = this.snsGroupMembersMapper.batchDeleteGroupMember(deleteMap);
            //删除群组 snsGroup
            int deleteGroupRow = this.snsGroupMapper.deleteByGroupId(groupId);
            if(deleteGroupRow > 0){
                return baseResp.ok();
            }
        }catch(Exception e){
            logger.error("dismiss group userId:{} groupId:{}",userId,groupId);
            printException(e);
        }
        return baseResp.fail();
    }

    /**
     * 更新群组 群主id
     * @param userId
     * @param currentUserId
     * @param groupId
     * @return
     */
    @Override
    public BaseResp<Object> updateGroupMainUser(Long userId, Long currentUserId, String groupId) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            //根据userId 和 groupId查询群组 校验权限
            SnsGroup snsGroup = this.snsGroupMapper.selectByGroupIdAndMainUserId(groupId,currentUserId);
            if(snsGroup == null){
                return baseResp.fail("抱歉,您无权操作该群组!");
            }

            if(userId.equals(currentUserId)){
                return baseResp.ok();
            }
            //校验新的群主id是否在群里
            SnsGroupMembers snsGroupMembers = snsGroupMembersMapper.findByUserIdAndGroupId(userId,groupId);
            if(snsGroupMembers == null){
                return baseResp.fail("用户还未成为群成员,请先添加为群成员!");
            }
            int row = this.snsGroupMapper.updateGroupMainUser(userId+"",groupId);
            if(row > 0){
                return baseResp.ok();
            }
        }catch(Exception e){
            logger.error("update group mainUser userId:{} currentUserId:{} groupId:{}",userId,currentUserId,groupId);
            printException(e);
        }
        return baseResp.fail();
    }

    /**
     * 查询用户的群列表
     * @param userId 用户id
     * @param startNum
     * @param pageSize
     * @return
     */
    @Override
    public BaseResp<Object> goupListByUser(Long userId, Integer startNum, Integer pageSize) {
        BaseResp<Object> baseResp = new BaseResp<>();
        List<SnsGroupMembers> snsGroupMembersList = this.snsGroupMembersMapper.groupMembersList(userId,startNum,pageSize);
        List<SnsGroup> snsGroupList = new ArrayList<SnsGroup>();
        if(snsGroupMembersList != null && snsGroupMembersList.size() > 0){
            for(SnsGroupMembers snsGroupMembers:snsGroupMembersList){
                SnsGroup snsGroup = this.snsGroupMapper.selectByGroupIdAndMainUserId(snsGroupMembers.getGroupid()+"",null);
                if(snsGroup == null){
                    continue;
                }
                Map<String,Object> parameterMap = new HashMap<String,Object>();
                parameterMap.put("groupId",snsGroup.getGroupid());
                parameterMap.put("startNum",0);
                parameterMap.put("pageSize",9);
                List<SnsGroupMembers> groupMembersList = snsGroupMembersMapper.selectSnsGroupMembersList(parameterMap);
                int maxLength = groupMembersList.size();
                String[] avatarArray = new String[maxLength];
                for(int i = 0;i<maxLength;i++){
                    AppUserMongoEntity appUserMongoEntity= userMongoDao.getAppUser(snsGroupMembersList.get(i).getUserid()+"");
                    avatarArray[i] = appUserMongoEntity.getAvatar();
                }
                snsGroup.setAvatarArray(avatarArray);
                snsGroupList.add(snsGroup);
            }
        }
        baseResp.setData(snsGroupList);
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
    }

    /**
     * 将用户加入到群组中,同步到融云
     * @param userIds 用户id
     * @param groupId 群组id
     * @param groupName 群组名称
     * @return
     */
    private boolean insertRongYunGroupMember(String[] userIds,String groupId,String groupName){
        if(userIds == null || StringUtils.hasBlankParams(groupId,groupName)){
            return false;
        }
        BaseResp<Object> baseResp = HttpClient.rongYunService.joinGroupMember(userIds,groupId,groupName);
        if(baseResp.getCode() == 0){
            return true;
        }
        return false;
    }

    /**
     * 退出群组
     * @param userIds 用户id
     * @param groupId 群组id
     * @return
     */
    private boolean deleteRongYunGroupMember(String[] userIds,String groupId){
        if(userIds == null || userIds.length < 1 || StringUtils.isEmpty(groupId)){
            return false;
        }
        BaseResp<Object> baseResp = HttpClient.rongYunService.quietGroup(userIds,groupId);
        if(baseResp.getCode() == 0){
            return true;
        }
        return false;
    }

    /**
     * 更新群组人数
     * @param groupId 群组id
     * @param addNum 新增的人数
     * @return
     */
    private boolean updateGroupCurrentNum(String groupId,Integer addNum){
        if(addNum == 0){
            return true;
        }
        int row = this.snsGroupMapper.updateGroupCurrentNum(groupId,addNum+"");
        if(row > 0){
            return true;
        }
        return false;
    }

}

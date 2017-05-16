package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.SnsGroupMapper;
import com.longbei.appservice.dao.SnsGroupMembersMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.dao.redis.SpringJedisDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.SnsGroup;
import com.longbei.appservice.entity.SnsGroupMembers;
import com.longbei.appservice.entity.UserMsg;
import com.longbei.appservice.service.GroupService;
import com.longbei.appservice.service.JPushService;
import com.longbei.appservice.service.UserMsgService;
import com.longbei.appservice.service.api.outernetservice.IRongYunService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
    @Autowired
    private UserMsgService userMsgService;
    @Autowired
    private IRongYunService iRongYunService;
    @Autowired
    private JPushService jPushService;

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
                needConfirm = false;
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

            List<String> userIdSet = new ArrayList<String>();
            userIdSet.add(mainGroupUserId.trim());
            if(userIds != null && userIds.length > 0){
                for(String userId:userIds){
                    if(!userIdSet.contains(userId)){
                        userIdSet.add(userId.trim());
                    }
                }
            }
            StringBuilder sb = new StringBuilder();
            for(String tempId:userIdSet){
                sb.append(",").append(tempId.trim());
            }
            String userIdString = sb.toString().substring(1);
            String[] newUserIds = userIdString.split(",");

            //获取群主信息
            AppUserMongoEntity mainGroupUser = this.userMongoDao.getAppUser(mainGroupUserId);
            if(mainGroupUser == null){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }

            if(StringUtils.isEmpty(groupName)){
                groupName = "";
                groupName += mainGroupUser.getNickname();
                if(null != userIds&&userIds.length > 0){
                    String tempUserId = userIds[0].trim();
                    if(userIds.length > 1 && tempUserId.equals(mainGroupUserId)){
                        tempUserId = userIds[1].trim();
                    }else if(tempUserId.equals(mainGroupUserId)){
                        tempUserId = null;
                    }
                    if(StringUtils.isNotEmpty(tempUserId)){
                        AppUserMongoEntity tempUser = this.userMongoDao.getAppUser(tempUserId);
                        groupName += "、"+tempUser.getNickname();
                    }
                }
            }

            //1.调用融云 创建群组
            BaseResp rongyunResp = iRongYunService.createGroup(userIdString,groupId,groupName);
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
            snsGroupMembers.setStatus(1);//加群是否需要验证
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
                //通知群成员 发送群组通知消息,通知创建了群组
                this.iRongYunService.noticeCreateGroup(mainGroupUserId,mainGroupUser.getNickname(),groupId+"",groupName);

                Map<String,Object> parameterMap = new HashMap<String,Object>();
                parameterMap.put("groupId",snsGroup.getGroupid());
                parameterMap.put("startNum",0);
                parameterMap.put("pageSize",9);
                List<SnsGroupMembers> groupMembersList = snsGroupMembersMapper.selectSnsGroupMembersList(parameterMap);
                int maxLength = groupMembersList.size();
                String[] avatarArray = new String[maxLength];
                for(int i = 0;i<maxLength;i++){
                    AppUserMongoEntity appUserMongoEntity= userMongoDao.getAppUser(groupMembersList.get(i).getUserid()+"");
                    avatarArray[i] = appUserMongoEntity == null?null:appUserMongoEntity.getAvatar();
                }
                snsGroup.setAvatarArray(avatarArray);
                baseResp.setData(snsGroup);
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
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
                SnsGroupMembers snsGroupMembers = this.snsGroupMembersMapper.findByUserIdAndGroupId(userId,groupId);
                if (snsGroupMembers == null) {
                    return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
                }
                String userNickname = snsGroupMembers.getNickname();
                if(StringUtils.isEmpty(userNickname)){
                    AppUserMongoEntity appUserMongoEntity = this.userMongoDao.getAppUser(userId+"");
                    userNickname = appUserMongoEntity.getNickname();
                }
                //更新群组名称
                BaseResp<Object> ryResult = iRongYunService.updateGroupName(snsGroup.getMainuserid()+"",userNickname,groupId,groupName);
                if(ryResult.getCode() != 0){
                    return baseResp.fail();
                }
            }
            Date updateDate = null;
            if(StringUtils.isNotEmpty(notice)){
                updateDate = new Date();
            }
            int row = this.snsGroupMapper.updateGroupInfo(groupId,groupName,needConfirm,notice,updateDate);
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
            //暂时不用推送此通知消息
//            BaseResp rongyunBaseResp = iRongYunService.updateGroupMemberNickname(userId+"",groupId,nickName);
            int row= snsGroupMembersMapper.updateSnsGroupMemberInfo(userId,groupId,nickName,null);
            if(row > 0){
                return baseResp.ok();
            }
        }catch(Exception e){
            logger.error("update groupmember nickname error userId:{} groupId:{} nickName;{}",userId,groupId,nickName);
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
        Map<String,Object> expandData = new HashMap<String,Object>();
        try {
            //根据groupiD查询群
            SnsGroup snsGroup = this.snsGroupMapper.selectByGroupIdAndMainUserId(groupId, null);
            if (snsGroup == null) {
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
            }
            if (userIds.length + snsGroup.getCurrentnum() > snsGroup.getMaxnum()) {
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_92, Constant.RTNINFO_SYS_92);
            }
            List<String> updateGroupMemberList = new ArrayList<String>();
            List<String> userIdList = new ArrayList<String>(Arrays.asList(userIds));
            AppUserMongoEntity invitationAppUserMongoEntity = null;
            String operatorUserId = invitationUserId != null?invitationUserId+"":snsGroup.getMainuserid()+"";
            if (invitationUserId != null) {
                invitationAppUserMongoEntity = this.userMongoDao.getAppUser(invitationUserId + "");
            }
            if (invitationAppUserMongoEntity == null){
                invitationAppUserMongoEntity = this.userMongoDao.getAppUser(userIds[0]);
            }
            int insertGroupNum = 0;

            //根据多个用户id和groupid查询数据 校验用户是否已经在群中
            Map<String,Object> parameterMap = new HashMap<String,Object>();
            parameterMap.put("userIds",userIdList);
            parameterMap.put("groupId",groupId);
            List<SnsGroupMembers> snsGroupMembersList = this.snsGroupMembersMapper.selectSnsGroupMembersList(parameterMap);

            int status = 0;
            if(snsGroupMembersList != null && snsGroupMembersList.size() > 0){
                StringBuilder sb = new StringBuilder();//存储新增的用户昵称
                for(SnsGroupMembers snsGroupMembers:snsGroupMembersList){
                    userIdList.remove(snsGroupMembers.getUserid()+"");
                    if(snsGroupMembers.getStatus() == 1){
                        continue;
                    }else{
                        if(snsGroup.getNeedconfirm() && snsGroupMembers.getStatus() == 0 && (invitationUserId == null || (invitationUserId != null && !invitationUserId.equals(snsGroup.getMainuserid())))){
                            continue;
                        }

                        if(!snsGroup.getNeedconfirm() || (invitationUserId != null && invitationUserId.equals(snsGroup.getMainuserid()))){
                            status = 1;
                        }else{
                            status = 0;
                        }
                        sb.append(",").append(snsGroupMembers.getNickname());
                        updateGroupMemberList.add(snsGroupMembers.getUserid()+"");
                    }
                }
                if(updateGroupMemberList.size() > 0){
                    boolean flag =true;
                    if(status == 1){
                        flag = insertRongYunGroupMember(operatorUserId,invitationAppUserMongoEntity.getNickname(),
                                    sb.toString().substring(1),updateGroupMemberList.toArray(new String[]{}),snsGroup.getGroupid()+"",snsGroup.getGroupname());
                    }
                    if(flag){
                        Map<String,Object> updateMap = new HashMap<String,Object>();
                        updateMap.put("updateUserIds",updateGroupMemberList);
                        updateMap.put("groupId",groupId);
                        updateMap.put("status",status);
                        if(invitationUserId != null){
                            updateMap.put("inviteuserid",invitationUserId);
                        }
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
                expandData.put("status",status);
                baseResp.setExpandData(expandData);
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
            }
            StringBuilder sb  = new StringBuilder();//存储被加入群的用户昵称

            SnsGroupMembers newSnsGroupMember = new SnsGroupMembers();
            newSnsGroupMember.setCreatetime(new Date());
            newSnsGroupMember.setUpdatetime(new Date());
            newSnsGroupMember.setGroupid(Long.parseLong(groupId));
            if(invitationUserId != null){
                newSnsGroupMember.setInviteuserid(invitationUserId);
            }

            List<AppUserMongoEntity> userList = new ArrayList<AppUserMongoEntity>();
            for(String tempUserId:userIdList){
                AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(tempUserId);
                if(appUserMongoEntity != null){
                    userList.add(appUserMongoEntity);
                    sb.append(",").append(appUserMongoEntity.getNickname());
                }
            }

            if(!snsGroup.getNeedconfirm() || (invitationUserId != null && invitationUserId.equals(snsGroup.getMainuserid()))){
                status = 1;
                newSnsGroupMember.setStatus(1);
                boolean flag = insertRongYunGroupMember(operatorUserId,invitationAppUserMongoEntity.getNickname(),
                                                    sb.toString().substring(1),userIdList.toArray(new String[]{}),snsGroup.getGroupid()+"",snsGroup.getGroupname());
                if(!flag){
                    logger.error("insert rongyun group members error groupid:{}",groupId);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return baseResp.fail("系统异常");
                }
            }else{
                newSnsGroupMember.setStatus(0);
            }
            if(StringUtils.isNotEmpty(remark)){
                newSnsGroupMember.setRemark(remark);
            }
            if(userList.size() < 1){
                expandData.put("status",status);
                baseResp.setExpandData(expandData);
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
            }

            //通知群主审核
            if(newSnsGroupMember.getStatus() == 0){
                String memo = "有用户申请加入群组:"+snsGroup.getGroupname()+",快去处理吧!";
                //gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统 
    			//10：榜中  11 圈子中  12 教室中  13:教室批复作业
                boolean sendMessageFlag = this.userMsgService.sendMessage(true,snsGroup.getMainuserid(),null,"0","35",snsGroup.getGroupid(),memo,"5");
                //JPush推送 消息
                boolean pushFlag = this.jPushService.pushMessage("消息标识",snsGroup.getMainuserid()+"","用户加群申请",memo,snsGroup.getGroupid()+"",Constant.JPUSH_TAG_COUNT_1101);
            }else{
                status = 1;
            }

            Map<String,Object> insertMap = new HashMap<String,Object>();
            insertMap.put("snsGroupMembers",newSnsGroupMember);
            insertMap.put("userList",userList);
            int insertRow = snsGroupMembersMapper.batchInsertGroupMembers(insertMap);
            if(insertRow > 0){
                if(newSnsGroupMember.getStatus() == 1){
                    insertGroupNum += userIdList.size();
                }
                if(insertGroupNum > 0){
                    updateGroupCurrentNum(groupId,insertGroupNum);
                }
                expandData.put("status",status);
                baseResp.setExpandData(expandData);
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
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
            AppUserMongoEntity operatorUser = this.userMongoDao.getAppUser(currentUserId+"");
            StringBuilder sb = new StringBuilder();//被审核入群成员的所有用户昵称
            StringBuilder deleteSb = new StringBuilder();

            Set<Long> sendMessageUserList = new HashSet<Long>();
            Set<Long> sendToInviteUserList = new HashSet<Long>();
            List<String> deleteList = new ArrayList<String>();
            List<String> userIdList = new ArrayList<String>(Arrays.asList(userIds));
            int insertGroupNum = 0;

            //根据多个用户id和groupid查询数据 校验用户是否已经在群中
            Map<String,Object> parameterMap = new HashMap<String,Object>();
            parameterMap.put("userIds",userIdList);
            parameterMap.put("groupId",groupId);
            List<SnsGroupMembers> snsGroupMembersList = this.snsGroupMembersMapper.selectSnsGroupMembersList(parameterMap);
            if(snsGroupMembersList == null || snsGroupMembersList.size() == 0){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
            }

            for(SnsGroupMembers snsGroupMembers:snsGroupMembersList){
                if(status.equals(snsGroupMembers.getStatus())){
                    userIdList.remove(snsGroupMembers.getUserid()+"");
                    continue;
                }else if(status == 2 && snsGroupMembers.getStatus() == 1){//原来是审核通过的,现在变成审核不通过
                    deleteList.add(snsGroupMembers.getUserid()+"");
                    sb.append(",").append(snsGroupMembers.getNickname());
                }
                if(snsGroupMembers.getStatus() == 0){
                    if(snsGroupMembers.getInviteuserid() != null){
                        sendToInviteUserList.add(snsGroupMembers.getInviteuserid());
                    }else{
                        sendMessageUserList.add(snsGroupMembers.getUserid());
                    }
                    sb.append(",").append(snsGroupMembers.getNickname());
                }
            }
            if(deleteList.size() > 0){
                boolean flag = deleteRongYunGroupMember(currentUserId+"",operatorUser.getNickname(),sb.toString().substring(1),deleteList.toArray(new String[]{}),groupId);
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
                    return baseResp.initCodeAndDesp(Constant.STATUS_SYS_92,Constant.RTNINFO_SYS_92);
                }

                updateFlag = insertRongYunGroupMember(currentUserId+"",operatorUser.getNickname(),
                                        sb.toString().substring(1),userIdList.toArray(new String[]{}),groupId,snsGroup.getGroupname());
                if(updateFlag){
                    insertGroupNum += userIdList.size();
                }
            }

            UserMsg userMsg = new UserMsg();
            userMsg.setFriendid(snsGroup.getMainuserid());
            userMsg.setMtype("0");
            userMsg.setMsgtype("17");
            //gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统 
			//10：榜中  11 圈子中  12 教室中  13:教室批复作业
            userMsg.setGtype("5");
//            userMsg.setSnsid(snsGroup.getGroupid());
            userMsg.setGtypeid(snsGroup.getGroupid());
            userMsg.setIsdel("0");
            userMsg.setIsread("0");
            userMsg.setTitle("加入请求审批结果");
            userMsg.setCreatetime(new Date());
            userMsg.setUpdatetime(new Date());
            String remark;
            String inviteRemark;
            if(status == 2){
                remark =  "您申请加入群组:"+snsGroup.getGroupname()+"的申请审核未通过，请联系管理员再次申请！";
                inviteRemark = "您邀请好友加入群组:"+snsGroup.getGroupname()+"的申请审核未通过，请联系管理员再次申请！";
            }else {
                remark =  "您申请加入群组:"+snsGroup.getGroupname()+"的申请审核通过了,快去群组聊天吧!";
                inviteRemark = "您邀请好友加入群组:"+snsGroup.getGroupname()+"的申请审核通过了,快去群组聊天吧!";
            }

            if(sendMessageUserList.size() > 0){
                List<Long> tempList = new ArrayList<Long>();
                for(Long tempUserId:sendMessageUserList){
                    tempList.add(tempUserId);
                }
                userMsg.setRemark(remark);
                boolean insertRow = this.userMsgService.batchInsertUserMsg(tempList,userMsg);
            }
            if(sendToInviteUserList.size() > 0){
                List<Long> tempList = new ArrayList<Long>();
                for(Long tempUserId:sendToInviteUserList){
                    tempList.add(tempUserId);
                }
                userMsg.setRemark(inviteRemark);
                boolean insertRow = this.userMsgService.batchInsertUserMsg(tempList,userMsg);
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
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_94,Constant.RTNINFO_SYS_94);
            }
            if(userIds.length == 1 && !currentUserId.equals(snsGroup.getMainuserid()) && !(currentUserId+"").equals(userIds[0])){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_94,Constant.RTNINFO_SYS_94);
            }
            for(String tempUserId:userIds){
                if(tempUserId.equals(snsGroup.getMainuserid()+"")){
                    return baseResp.initCodeAndDesp(Constant.STATUS_SYS_95,Constant.RTNINFO_SYS_95);
                }
            }
            SnsGroupMembers snsGroupMembers = this.snsGroupMembersMapper.findByUserIdAndGroupId(currentUserId,groupId);
            AppUserMongoEntity operatorUser = this.userMongoDao.getAppUser(currentUserId+"");
            StringBuilder sb = new StringBuilder();
            for(String userid:userIds){
                SnsGroupMembers snsGroupMembers1 = this.snsGroupMembersMapper.findByUserIdAndGroupId(Long.parseLong(userid),groupId);
                sb.append(",").append(snsGroupMembers1.getNickname());
            }

            boolean flag = deleteRongYunGroupMember(currentUserId+"",snsGroupMembers.getNickname(),sb.toString().substring(1),userIds,groupId);
            if(flag){
                //从数据库删除数据
                Map<String,Object> deleteMap = new HashMap<String,Object>();
                deleteMap.put("userIds",userIds);
                deleteMap.put("groupId",groupId);
                deleteMap.put("nowDate",new Date());
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
    public BaseResp<Object> groupMemberList(String groupId, Long userId, Integer status,String keyword,Boolean noQueryCurrentUser,Integer startNum,Integer pageSize) {
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
            Long noQueryUserId = null;
            if(noQueryCurrentUser != null && noQueryCurrentUser){
                noQueryUserId = userId;
            }
            List<SnsGroupMembers> snsGroupMembersList = this.snsGroupMembersMapper.groupMemberList(groupId,status,keyword,noQueryUserId,startNum,pageSize);

            if(startNum != null && startNum == 0 && status == 1 && snsGroupMembersList != null && snsGroupMembersList.size() > 0){
                Integer count = this.snsGroupMembersMapper.groupMembersCount(groupId,status);
                resultMap.put("snsGroupMembersCount",count);
            }

            if(snsGroup.getMainuserid().equals(userId)){
                resultMap.put("isMainUser",true);
            }else{
                resultMap.put("isMainUser",false);
            }

            if(status == 0 && snsGroupMembersList != null && snsGroupMembersList.size() > 0){
                for(SnsGroupMembers snsGroupMembers:snsGroupMembersList){
                    if(snsGroupMembers.getInviteuserid() != null){
                        SnsGroupMembers snsGroupMembers1 = this.snsGroupMembersMapper.findByUserIdAndGroupId(snsGroupMembers.getInviteuserid(),groupId);
                        if(snsGroupMembers1 != null){
                            snsGroupMembers.setRemark(snsGroupMembers1.getNickname());
                        }
                    }
                }
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
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }
            AppUserMongoEntity appUserMongoEntity = this.userMongoDao.getAppUser(userId+"");
            BaseResp<Object> ryResult = iRongYunService.dismissGroup(appUserMongoEntity.getNickname(),userId+"",groupId);
            if(ryResult.getCode() != 0){
                return baseResp.fail("系统异常");
            }
            //删除群组成员
            Map<String,Object> deleteMap = new HashMap<String,Object>();
            deleteMap.put("groupId",groupId);
            int deleteGroupMemberRow = this.snsGroupMembersMapper.batchDeleteGroupMember(deleteMap);
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
    public BaseResp<Object> goupListByUser(Long userId, Integer startNum, Integer pageSize,Date updateTime) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            List<SnsGroup> resultList = new ArrayList<SnsGroup>();
            List<SnsGroupMembers> snsGroupMembersList = this.snsGroupMembersMapper.groupMembersList(userId,startNum,pageSize,updateTime);
            if(snsGroupMembersList != null && snsGroupMembersList.size() > 0) {
                for (SnsGroupMembers snsGroupMembers : snsGroupMembersList) {
                    SnsGroup snsGroup = this.snsGroupMapper.selectByGroupIdAndMainUserId(snsGroupMembers.getGroupid() + "", null);
                    if (snsGroup == null) {
                        continue;
                    }
                    resultList.add(snsGroup);
//                    Map<String,Object> map = new HashMap<String,Object>();
//
                    Map<String,Object> parameterMap = new HashMap<String,Object>();
                    parameterMap.put("groupId",snsGroup.getGroupid());
                    parameterMap.put("startNum",0);
                    parameterMap.put("pageSize",9);
                    List<SnsGroupMembers> groupMembersList = snsGroupMembersMapper.selectSnsGroupMembersList(parameterMap);
                    int maxLength = groupMembersList.size();
                    String[] avatarArray = new String[maxLength];
                    for(int i = 0;i<maxLength;i++){
                        AppUserMongoEntity appUserMongoEntity= userMongoDao.getAppUser(groupMembersList.get(i).getUserid()+"");
                        avatarArray[i] = appUserMongoEntity == null?null:appUserMongoEntity.getAvatar();
                    }
                    snsGroup.setAvatarArray(avatarArray);
//
//                    map.put("groupid",snsGroup.getGroupid());
//                    map.put("groupname",snsGroup.getGroupname());
//                    map.put("grouptype",snsGroup.getGrouptype());
//                    map.put("needconfirm",snsGroup.getNeedconfirm());
//                    map.put("notice",snsGroup.getNotice());
//                    map.put("currentnum",snsGroup.getCurrentnum());
//                    map.put("maxnum",snsGroup.getMaxnum());
//                    map.put("avatarArray",avatarArray);
//
//                    if(updateTime != null){
//                        if(snsGroupMembers.getStatus() == 4){
//                            map.put("operationType","delete");
//                        }else if(DateUtils.compare(snsGroup.getCreatetime(),updateTime)){
//                            map.put("operationType","insert");
//                        }else{
//                            map.put("operationType","update");
//                        }
//                    }
//                    resultList.add(map);
//                }
                }
//            Map<String,Object> resultMap = new HashMap<String,Object>();
//            resultMap.put("groupList",resultList);
//            resultMap.put("updateTime",DateUtils.getDateTime());
                baseResp.setData(resultList);
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        }catch(Exception e){
            logger.error("select user group list error userId:{} startNum:{} pageSize:{} updateTime:{}",userId,startNum,pageSize,updateTime);
            printException(e);
        }
        return baseResp;
    }

    /**
     * 查询群组 根据群号搜索
     * @param keyword
     * @return
     */
    @Override
    public BaseResp<Object> searchGroup(Long userid,String keyword,Integer startNum,Integer pageSize) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            List<SnsGroup> groupList = this.snsGroupMapper.selectGroup(keyword,startNum,pageSize);
            if(groupList != null && groupList.size() > 0){
                for(SnsGroup snsGroup:groupList){
                    Map<String,Object> parameterMap = new HashMap<String,Object>();
                    parameterMap.put("groupId",snsGroup.getGroupid());
                    parameterMap.put("startNum",0);
                    parameterMap.put("pageSize",9);
                    SnsGroupMembers snsGroupMembers = snsGroupMembersMapper.findByUserIdAndGroupId(userid,String.valueOf(snsGroup.getGroupid()));
                    if(null == snsGroupMembers){
                        snsGroup.setHasjoin("0");
                    }else {
                        snsGroup.setHasjoin("1");
                    }
                    List<SnsGroupMembers> groupMembersList = snsGroupMembersMapper.selectSnsGroupMembersList(parameterMap);
                    int maxLength = groupMembersList.size();
                    String[] avatarArray = new String[maxLength];
                    for(int i = 0;i<maxLength;i++){
                        AppUserMongoEntity appUserMongoEntity= userMongoDao.getAppUser(groupMembersList.get(i).getUserid()+"");
                        avatarArray[i] = appUserMongoEntity.getAvatar();
                    }
                    snsGroup.setAvatarArray(avatarArray);
                }
            }

            baseResp.setData(groupList);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("search group by keyword error keyword:{}",keyword);
            printException(e);
        }
        return baseResp;
    }




    /**
     * 更新用户群组的其他信息
     * @param userid 用户id
     * @param groupId 群组id
     * @param topStatus 群组是否置顶 0.不置顶 1.置顶
     * @param disturbStatus 是否消息免打扰 0.关闭消息免打扰 1.开启消息免打扰
     * @return
     */
    @Override
    public BaseResp<Object> updateUserGroupOtherInfo(Long userid, Long groupId, Integer topStatus, Integer disturbStatus) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("userId",userid);
            map.put("groupId",groupId);
            if(topStatus != null){
                map.put("topStatus",topStatus == 1?true:false);
            }
            if(disturbStatus != null){
                map.put("disturbStatus",disturbStatus == 1?true:false);
            }
            int row = this.snsGroupMembersMapper.updateUserGroupOtherInfo(map);
            if(row > 0){
                return baseResp.ok();
            }
        }catch(Exception e){
            logger.error("update User group other info userid:{} groupId:{} topStatus;{} disturbStatus:{}",userid,groupId,topStatus,disturbStatus);
            printException(e);
        }
        return baseResp;
    }

    /**
     * 查询群组详情
     * @param groupId 群组id
     * @param userid 用户id
     * @return
     */
    @Override
    public BaseResp<Object> groupDetail(Long groupId, Long userid) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            Map<String,Object> resultMap = new HashMap<String,Object>();
            SnsGroup snsGroup = this.snsGroupMapper.selectByGroupIdAndMainUserId(groupId+"",null);
            if(snsGroup == null){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }
            int status = 0;//0.可加群 1.已提交入群申请 2.已经在群中
            //校验该用户是否已经在这个群中
            SnsGroupMembers snsGroupMembers = this.snsGroupMembersMapper.findByUserIdAndGroupId(userid,groupId+"");
            if(snsGroupMembers != null && snsGroupMembers.getStatus() != 4 && snsGroupMembers.getStatus() != 2){
                status = snsGroupMembers.getStatus() == 0?1:2;
                resultMap.put("nickname",snsGroupMembers.getNickname());
                if(snsGroup.getMainuserid().longValue() == userid.longValue()){
                    resultMap.put("isMainUser",true);
                }else{
                    resultMap.put("isMainUser",false);
                }
            }
            baseResp.setData(snsGroup);
            resultMap.put("status",status);
            baseResp.setExpandData(resultMap);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
            return baseResp;
        }catch(Exception e){
            logger.error("select group detail error groupId:{} userId:{}",groupId,userid,e);
        }
        return baseResp;
    }

    /**
     * 查询用户在群组中的信息
     * @param userid
     * @param groupId
     * @return
     */
    @Override
    public BaseResp<Object> groupMemberDetail(Long userid, Long groupId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            SnsGroupMembers snsGroupMembers = this.snsGroupMembersMapper.findByUserIdAndGroupId(userid,groupId+"");
            baseResp.setData(snsGroupMembers);
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("group member detail error userid:{} groupId:{}",userid,groupId);
            printException(e);
        }
        return baseResp;
    }

    /**
     * 查询所有的群组成员 用户id
     * @param groupId
     * @return
     */
    @Override
    public BaseResp<Object> selectGroupMemberIdList(Long groupId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            List<Long> groupMemberIdList = this.snsGroupMembersMapper.selectGroupMemberIdList(groupId);
            baseResp.setData(groupMemberIdList);
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch (Exception e){
            logger.error("select groupMember id list error groupId:{}",groupId);
            printException(e);
        }
        return baseResp;
    }

    /**
     * 将用户加入到群组中,同步到融云
     * @param operatorUserId 操作人用户id
     * @param operatorNickname 操作人用户昵称
     * @param targetUserDisplayNames 被加入群的所有人用户昵称
     * @param userIds 用户id
     * @param groupId 群组id
     * @param groupName 群组名称
     * @return
     */
    private boolean insertRongYunGroupMember(String operatorUserId,String operatorNickname,String targetUserDisplayNames,String[] userIds,String groupId,String groupName){
        if(userIds == null || StringUtils.hasBlankParams(groupId,groupName)){
            return false;
        }
        StringBuilder sb = new StringBuilder();
        for(String userId:userIds){
            sb.append(",").append(userId.trim());
        }
        BaseResp<Object> baseResp = iRongYunService.joinGroupMember(operatorUserId,operatorNickname,targetUserDisplayNames,sb.toString().substring(1),groupId,groupName);
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
    private boolean deleteRongYunGroupMember(String operatorUserId,String operatorUserNickName,String targetUserDisplayNames, String[] userIds,String groupId){
        if(userIds == null || userIds.length < 1 || StringUtils.isEmpty(groupId)){
            return false;
        }
        StringBuilder sb = new StringBuilder();
        for(String userId:userIds){
            sb.append(",").append(userId.trim());
        }
        BaseResp<Object> baseResp = iRongYunService.quietGroup(operatorUserId,operatorUserNickName,targetUserDisplayNames,sb.toString().substring(1),groupId);
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

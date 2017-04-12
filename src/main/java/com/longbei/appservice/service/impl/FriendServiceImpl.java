package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.SnsFriendsMapper;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.dao.mongo.dao.FriendMongoDao;
import com.longbei.appservice.dao.mongo.dao.NewMessageTipDao;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.FriendService;
import com.longbei.appservice.service.api.outernetservice.IJPushService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangyongzhi 17/3/6.
 */
@Service("friendService")
public class FriendServiceImpl extends BaseServiceImpl implements FriendService {
    @Autowired
    private SnsFriendsMapper snsFriendsMapper;
    @Autowired
    private FriendMongoDao friendMongoDao;
    @Autowired
    private IdGenerateService idGenerateService;
    @Autowired
    private UserMongoDao userMongoDao;
    @Autowired
    private NewMessageTipDao newMessageTipDao;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private IJPushService ijPushService;

    private Logger logger = LoggerFactory.getLogger(FriendServiceImpl.class);

    /**
     * 请求添加朋友
     * @param userId 当前用户id
     * @param friendId 请求添加的朋友id
     * @return
     */
    @Override
    public BaseResp addFriendAsk(Long userId, Long friendId,FriendAddAsk.Source source,String message) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            SnsFriends snsFriends = snsFriendsMapper.selectByUidAndFid(userId,friendId);
            if(snsFriends != null){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_90, Constant.RTNINFO_SYS_90);
            }
            AppUserMongoEntity senderUser = userMongoDao.getAppUser(userId+"");
            if(StringUtils.isEmpty(message)){
                message = "您好,我是" + senderUser.getNickname();
            }
            AppUserMongoEntity receiveUser = userMongoDao.getAppUser(friendId+"");
            if(senderUser == null || receiveUser == null){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
            }

            //从mongo中查看该用户是否添加过该朋友
            FriendAddAsk friendAddAsk = friendMongoDao.findFriendAddAsk(userId,friendId);
            if(friendAddAsk == null){//从没添加过朋友
                FriendAddAsk newFriendAddAsk = new FriendAddAsk();
                newFriendAddAsk.setCreateDate(new Date());
                newFriendAddAsk.setId(idGenerateService.getUniqueIdAsLong());
                newFriendAddAsk.setSenderUserId(userId);
                newFriendAddAsk.setReceiveUserId(friendId);
                newFriendAddAsk.setStatus(FriendAddAsk.STATUS_PENDING);
                newFriendAddAsk.setSource(source);
                newFriendAddAsk.setSenderIsRead(true);
                newFriendAddAsk.setReceiveIsRead(false);

                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("senderId",userId);
                jsonObject.put("content",message);
                jsonArray.add(jsonObject);
                newFriendAddAsk.setMessage(jsonArray);

                friendMongoDao.addFriendAddAsk(newFriendAddAsk);
                updateUserNewMessageTip(friendId,true);
                //JPUSH通知用户
                JSONObject pushMessage = new JSONObject();
                pushMessage.put("status","消息标识");
                pushMessage.put("userid",friendId);
                pushMessage.put("content","有用户申请加为好友");
                pushMessage.put("msgid",newFriendAddAsk.getId());
                pushMessage.put("tag",Constant.JPUSH_TAG_COUNT_1001);
                ijPushService.messagePush(friendId+"","申请加为好友","申请加为好友",pushMessage.toString());

                return baseResp.ok("申请添加好友成功,请等待好友审核~~");
            }

            long timeDifference =DateUtils.getTimeDifference(friendAddAsk.getCreateDate(),new Date());
            if(!FriendAddAsk.STATUS_PENDING.equals(friendAddAsk.getStatus())){
                //查看上次添加好友的时间是否超过七天
                if(timeDifference < FriendAddAsk.EXPIRETIME){
                    return baseResp.initCodeAndDesp(Constant.STATUS_SYS_91, Constant.RTNINFO_SYS_91);
                }
                //校验用户现在是否还是好友
                SnsFriends tempSnsFriend = snsFriendsMapper.selectByUidAndFid(userId,friendId);
                if(tempSnsFriend != null){
                    return baseResp.initCodeAndDesp(Constant.STATUS_SYS_90, Constant.RTNINFO_SYS_90);
                }

                friendMongoDao.updateFriendAddAskStatus(userId,friendId,FriendAddAsk.STATUS_PENDING,true);
                updateUserNewMessageTip(friendId,true);

                //JPUSH通知用户
                JSONObject pushMessage = new JSONObject();
                pushMessage.put("status","消息标识");
                pushMessage.put("userid",friendId);
                pushMessage.put("content","有用户申请加为好友");
                pushMessage.put("msgid",friendAddAsk.getId());
                pushMessage.put("tag",Constant.JPUSH_TAG_COUNT_1001);
                ijPushService.messagePush(friendId+"","申请加为好友","申请加为好友",pushMessage.toString());
                return baseResp.ok("申请添加好友成功,请等待好友审核~~");
            }
            JSONArray jsonArray = friendAddAsk.getMessage();
            if(jsonArray == null){
                jsonArray = new JSONArray();
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("senderId",userId);
            jsonObject.put("content",message);
            jsonArray.add(jsonObject);

            friendMongoDao.updateFriendAddAsk(friendAddAsk.getId(),jsonArray,"receive",false,FriendAddAsk.STATUS_PENDING,true);
            updateUserNewMessageTip(friendId,true);

//            if(timeDifference < FriendAddAsk.EXPIRETIME){
//                return baseResp.fail("您最近已经申请了加为好友,正在等待该好友处理~~");
//            }

            //JPUSH通知用户
            JSONObject pushMessage = new JSONObject();
            pushMessage.put("status","消息标识");
            pushMessage.put("userid",friendId);
            pushMessage.put("content","有用户申请加为好友");
            pushMessage.put("msgid",friendAddAsk.getId());
            pushMessage.put("tag",Constant.JPUSH_TAG_COUNT_1001);
            ijPushService.messagePush(friendId+"","申请加为好友","申请加为好友",pushMessage.toString());
            return baseResp.ok("申请添加好友成功,请等待好友审核~~");
        }catch(Exception e){
            logger.error("insert friendAddAsk userId:{} friendId:{}",userId,friendId);
            printException(e);
            return baseResp.fail("系统异常");
        }
    }

    /**
     * 回复加好友信息
     * @param id
     * @param userId
     * @param message
     * @return
     */
    @Override
    public BaseResp<Object> replyMessage(Long id, Long userId, String message) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            FriendAddAsk friendAddAsk = friendMongoDao.findFriendAddAskById(id);
            if(friendAddAsk == null || (!userId.equals(friendAddAsk.getSenderUserId()) && !userId.equals(friendAddAsk.getReceiveUserId()))){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }
            if(FriendAddAsk.STATUS_PASS.equals(friendAddAsk.getStatus())){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_90, Constant.RTNINFO_SYS_90);
            }
            String tempFlag = "";
            Long receiveUserId = null;
            if(userId.equals(friendAddAsk.getSenderUserId())){
                tempFlag = "receive";
                receiveUserId = friendAddAsk.getReceiveUserId();
                updateUserNewMessageTip(friendAddAsk.getReceiveUserId(),true);
            }else{
                tempFlag = "sender";
                receiveUserId = friendAddAsk.getSenderUserId();
                updateUserNewMessageTip(friendAddAsk.getSenderUserId(),true);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("senderId",userId);
            jsonObject.put("content",message);
            JSONArray jsonArray = friendAddAsk.getMessage();
            jsonArray.add(jsonObject);
            //注释掉的代码是为了满足当初的需求 同一个用户不能连续回复三次消息
//            if(jsonArray.size() < 3){
                friendMongoDao.updateFriendAddAsk(id,jsonArray,tempFlag,false,null,null);
                //JPUSH通知用户
                JSONObject pushMessage = new JSONObject();
                pushMessage.put("status","消息标识");
                pushMessage.put("userid",receiveUserId);
                pushMessage.put("content","有用户回复了消息");
                pushMessage.put("msgid",friendAddAsk.getId());
                pushMessage.put("tag",Constant.JPUSH_TAG_COUNT_1001);
                ijPushService.messagePush(receiveUserId+"","申请加为好友","申请加为好友",pushMessage.toString());

                return baseResp.ok("回复成功");
//            }
//            int repeat = 0;
//            for(int i = jsonArray.size()-1;i > -1;i--){
//                JSONObject beforeJSONObject = jsonArray.getJSONObject(i);
//                if(userId.equals(beforeJSONObject.get("senderId"))){
//                    repeat ++;
//                }else{
//                    break;
//                }
//                if(repeat > 3){
//                    jsonArray.remove(i);
//                }
//            }
//            friendMongoDao.updateFriendAddAsk(id,jsonArray,tempFlag,false,null,null);
//            return baseResp.ok("回复成功");
        }catch(Exception e){
            logger.error("reply friendAddAsk id:{} userId:{} message:{}",id,userId,message);
            printException(e);
            return baseResp.fail();
        }

    }

    /**
     * 获取添加好友的详细信息
     * @param id 添加好友的请求id
     * @param userId 查看信息 的用户id
     * @return
     */
    @Override
    public BaseResp<Object> getFriendAddAskDetail(Long id, Long userId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            FriendAddAsk friendAddAsk = friendMongoDao.findFriendAddAskById(id);
            if(friendAddAsk == null || (!userId.equals(friendAddAsk.getSenderUserId()) && !userId.equals(friendAddAsk.getReceiveUserId()))){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }
            if(userId.equals(friendAddAsk.getSenderUserId())){//当前查看信息的是发送者
                friendAddAsk.setAppUserMongoEntity(userMongoDao.getAppUser(friendAddAsk.getReceiveUserId()+""));
                //更改用户消息为已读
                friendMongoDao.updateFriendAddAsk(id,null,"sender",true,null,null);
            }else{
                friendAddAsk.setAppUserMongoEntity(userMongoDao.getAppUser(friendAddAsk.getSenderUserId()+""));
                //更改用户消息为已读
                friendMongoDao.updateFriendAddAsk(id,null,"receive",true,null,null);
            }
            //更改用户新消息提示为没有新消息(会校验是否还有未读消息,如果有未读消息,则不会更新新消息提示的状态)
            updateUserNewMessageTip(userId,false);

            JSONArray jsonArray = friendAddAsk.getMessage();
            if(jsonArray.size() > 3){
                JSONArray newJSONArray = new JSONArray();
                for(int i=3;i > 0;i--){
                    newJSONArray.add(jsonArray.get(jsonArray.size()-i));
                }
                friendAddAsk.setMessage(newJSONArray);
            }

            Map<String,Object> resultMap = new HashMap<String,Object>();
            resultMap.put("message",friendAddAsk.getMessage());
            resultMap.put("senderUserId",friendAddAsk.getSenderUserId());
            resultMap.put("status",friendAddAsk.getStatus());
            resultMap.put("appUserMongoEntity",friendAddAsk.getAppUserMongoEntity());

            baseResp.setData(resultMap);

            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("get FriendAddAsk detail id:{} userId:{}");
            printException(e);
            return baseResp.fail();
        }

    }

    /**
     * 更改用户的请求加好友状态
     * @param id
     * @param status
     * @param userId
     * @return
     */
    @Transactional
    @Override
    public BaseResp<Object> updateFriendAddAskStatus(Long id, Integer status, Long userId) {
        try{
            FriendAddAsk friendAddAsk = friendMongoDao.findByFriendAddAskId(id);
            if(friendAddAsk == null || !userId.equals(friendAddAsk.getReceiveUserId())){
                return new BaseResp<Object>().initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
            }
            friendMongoDao.updateFriendAddAsk(id,null,null,null,status,null);
            if(status == 2){
                //JPUSH通知用户
                JSONObject pushMessage = new JSONObject();
                pushMessage.put("status","消息标识");
                pushMessage.put("userid",friendAddAsk.getSenderUserId());
                pushMessage.put("content","加好友申请被拒绝");
                pushMessage.put("msgid",friendAddAsk.getId());
                pushMessage.put("tag",Constant.JPUSH_TAG_COUNT_1001);
                ijPushService.messagePush(friendAddAsk.getSenderUserId()+"","加好友申请被拒绝","加好友申请被拒绝",pushMessage.toString());
                return new BaseResp<>().ok();
            }
            SnsFriends tempSnsFriends = snsFriendsMapper.selectByUidAndFid(userId,friendAddAsk.getSenderUserId());
            if(tempSnsFriends != null && tempSnsFriends.getIsdel() == 0){
                return new BaseResp<Object>().initCodeAndDesp(Constant.STATUS_SYS_90,Constant.RTNINFO_SYS_90);
            }else if(tempSnsFriends != null){
                //更改好友状态
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("userId",tempSnsFriends.getUserid());
                map.put("friendId",tempSnsFriends.getFriendid());
                map.put("isDel","0");
                map.put("createtime",new Date());
                map.put("updatetime",new Date());
                int row = this.snsFriendsMapper.updateByUidAndFid(map);

                map.put("userId",tempSnsFriends.getFriendid());
                map.put("friendId",tempSnsFriends.getUserid());
                int row1 = this.snsFriendsMapper.updateByUidAndFid(map);
                if(row > 0 && row1 > 0){
                    return new BaseResp<Object>().initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
                }
            }


            //往数据库添加数据
            SnsFriends snsFriends = new SnsFriends();
            snsFriends.setCreatetime(new Date());
            snsFriends.setUserid(friendAddAsk.getSenderUserId());
            snsFriends.setFriendid(friendAddAsk.getReceiveUserId());
            snsFriends.setIspublic("1");
            snsFriends.setIsdel(0);
            snsFriends.setUpdatetime(new Date());
            snsFriendsMapper.insertSelective(snsFriends);

            snsFriends.setUserid(friendAddAsk.getReceiveUserId());
            snsFriends.setFriendid(friendAddAsk.getSenderUserId());
            snsFriendsMapper.insertSelective(snsFriends);

            //JPUSH通知用户
            JSONObject pushMessage = new JSONObject();
            pushMessage.put("status","消息标识");
            pushMessage.put("userid",friendAddAsk.getSenderUserId());
            pushMessage.put("content","同意了加好友申请");
            pushMessage.put("msgid",friendAddAsk.getId());
            pushMessage.put("tag",Constant.JPUSH_TAG_COUNT_1001);
            ijPushService.messagePush(friendAddAsk.getSenderUserId()+"","同意了加好友申请","同意了加好友申请",pushMessage.toString());
            return new BaseResp<Object>().ok();
        }catch(Exception e){
            logger.error("update friendAddAsk status id:{} status:{} userId:{}",id,status,userId);
            printExceptionAndRollBackTransaction(e);
            return new BaseResp<Object>().fail();
        }
    }

    /**
     * 添加好友列表
     * @param userId 用户id
     * @param startNo 开始下标
     * @param pageSize 每页条数
     * @return
     */
    @Override
    public BaseResp<Object> friendAddAskList(Long userId, Integer startNo, Integer pageSize) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try {
            List<FriendAddAsk> list = friendMongoDao.friendAddAskList(userId,startNo,pageSize);
            if(list != null && list.size() > 0){
                for(FriendAddAsk friendAddAsk:list){
                    if(friendAddAsk.getMessage() != null && friendAddAsk.getMessage().size() > 0){
                        JSONObject jsonObject = friendAddAsk.getMessage().getJSONObject(friendAddAsk.getMessage().size()-1);
                        friendAddAsk.setLastMessage(jsonObject.getString("content"));
                        friendAddAsk.setMessage(null);
                    }
                    if(userId.equals(friendAddAsk.getSenderUserId())){
                        friendAddAsk.setAppUserMongoEntity(userMongoDao.getAppUser(friendAddAsk.getReceiveUserId()+""));
                    }else{
                        friendAddAsk.setAppUserMongoEntity(userMongoDao.getAppUser(friendAddAsk.getSenderUserId()+""));
                    }
                }
            }

            baseResp.setData(list);
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch (Exception e){
            logger.error("find friendAddAsk list userId:{} startNo:{} pageSize:{}");
            printException(e);
            return baseResp.fail();
        }
    }

    /**
     * 校验两个用户是否是好友
     * @param userId 用户id
     * @param friendId 朋友id
     * @return
     */
    @Override
    public boolean checkIsFriend(Long userId, Long friendId) {
        SnsFriends snsFriends = snsFriendsMapper.selectByUidAndFid(userId,friendId);
        if(snsFriends == null){
            return false;
        }
        return true;
    }

    /**
     * 获取用户昵称
     * @param userId 当前登录用户
     * @param friendId 好友的id
     * @return
     */
    @Override
    public String getNickName(Long userId, Long friendId) {
        SnsFriends snsFriends = this.snsFriendsMapper.selectByUidAndFid(userId,friendId);
        if(snsFriends != null && (StringUtils.isNotEmpty(snsFriends.getNickname()) || StringUtils.isNotEmpty(snsFriends.getRemark()))){
            return StringUtils.isNotEmpty(snsFriends.getRemark())?snsFriends.getRemark():snsFriends.getNickname();
        }
        AppUserMongoEntity appUserMongoEntity = this.userMongoDao.getAppUser(friendId+"");
        if(appUserMongoEntity != null && StringUtils.isNotEmpty(appUserMongoEntity.getNickname())){
            return appUserMongoEntity.getNickname();
        }
        UserInfo userInfo = this.userInfoMapper.selectByUserid(friendId);
        if(userInfo != null){
            return StringUtils.isNotEmpty(userInfo.getNickname())?userInfo.getNickname():userInfo.getUsername();
        }
        return null;
    }

    /**
     * 更新用户的添加好友请求的新消息提示状态
     * @param userId 用户id
     * @param flag 更改的状态 true代表有新消息 false代表没有新消息
     * @return
     */
    private boolean updateUserNewMessageTip(Long userId,boolean flag){
        if(flag){
            return newMessageTipDao.updateNewMessageTip(userId, NewMessageTip.MessageType.friendAddAsk,true);
        }
        int count = this.friendMongoDao.findUserUnReadAsk(userId);
        if(count < 1){
            return newMessageTipDao.updateNewMessageTip(userId, NewMessageTip.MessageType.friendAddAsk,false);
        }
        return true;
    }
}

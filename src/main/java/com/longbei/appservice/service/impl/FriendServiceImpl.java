package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.SnsFriendsMapper;
import com.longbei.appservice.dao.mongo.dao.FriendMongoDao;
import com.longbei.appservice.dao.mongo.dao.NewMessageTipDao;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.FriendAddAsk;
import com.longbei.appservice.entity.NewMessageTip;
import com.longbei.appservice.entity.SnsFriends;
import com.longbei.appservice.service.FriendService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by wangyongzhi 17/3/6.
 */
@Service("friendService")
public class FriendServiceImpl implements FriendService {
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

    /**
     * 请求添加朋友
     * @param userId 当前用户id
     * @param friendId 请求添加的朋友id
     * @return
     */
    @Override
    public BaseResp addFriendAsk(Long userId, Long friendId,FriendAddAsk.Source source,String message) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        SnsFriends snsFriends = snsFriendsMapper.selectByUidAndFid(userId,friendId);
        if(snsFriends != null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_90, Constant.RTNINFO_SYS_90);
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
            AppUserMongoEntity senderUser = userMongoDao.findById(userId+"");
            AppUserMongoEntity receiveUser = userMongoDao.findById(friendId+"");
            if(senderUser == null || receiveUser == null){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
            }
            jsonObject.put("senderId",userId);
            if(StringUtils.isEmpty(message)){
                message = "您好,我是" + senderUser.getNickname();
            }
            jsonObject.put("content",message);
            jsonArray.add(jsonObject);
            newFriendAddAsk.setMessage(jsonArray);
            friendMongoDao.addFriendAddAsk(newFriendAddAsk);
            updateUserNewMessageTip(friendId,true);
            return baseResp.ok("申请添加好友成功,请等待好友审核~~");
        }

        long timeDifference =DateUtils.getTimeDifference(friendAddAsk.getCreateDate(),new Date());
        if(!FriendAddAsk.STATUS_PENDING.equals(friendAddAsk.getStatus())){
            //查看上次添加好友的时间是否超过七天
            if(timeDifference < FriendAddAsk.EXPIRETIME){
                return baseResp.fail("抱歉,七天内不能重复添加同一个好友!");
            }
            friendMongoDao.updateFriendAddAskStatus(userId,friendId,FriendAddAsk.STATUS_PENDING,true);
            updateUserNewMessageTip(friendId,true);
            return baseResp.ok("申请添加好友成功,请等待好友审核~~");
        }
        if(timeDifference < FriendAddAsk.EXPIRETIME){
            return baseResp.fail("您最近已经申请了加为好友,正在等待该好友处理~~");
        }
        friendMongoDao.updateFriendAddAskStatus(userId,friendId,FriendAddAsk.STATUS_PENDING,true);
        updateUserNewMessageTip(friendId,true);
        return baseResp.ok("申请添加好友成功,请等待好友审核~~");
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
        FriendAddAsk friendAddAsk = friendMongoDao.findFriendAddAskById(id);
        if(friendAddAsk == null || (!userId.equals(friendAddAsk.getSenderUserId()) && !userId.equals(friendAddAsk.getReceiveUserId()))){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(FriendAddAsk.STATUS_PASS.equals(friendAddAsk.getStatus())){
            return baseResp.fail("你们已经是好友了,请直接去聊天框输入信息~~");
        }
        String tempFlag = "";
        if(userId.equals(friendAddAsk.getSenderUserId())){
            tempFlag = "receive";
            updateUserNewMessageTip(friendAddAsk.getReceiveUserId(),true);
        }else{
            tempFlag = "sender";
            updateUserNewMessageTip(friendAddAsk.getSenderUserId(),true);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("senderId",userId);
        jsonObject.put("content",message);
        JSONArray jsonArray = friendAddAsk.getMessage();
        jsonArray.add(jsonObject);
        if(jsonArray.size() < 3){
            friendMongoDao.updateFriendAddAsk(id,jsonArray,tempFlag,false,null);
            return baseResp.ok("回复成功");
        }
        int repeat = 0;
        for(int i = jsonArray.size()-1;i > -1;i--){
            JSONObject beforeJSONObject = jsonArray.getJSONObject(i);
            if(userId.equals(beforeJSONObject.get("senderId"))){
                repeat ++;
            }else{
                break;
            }
            if(repeat > 3){
                jsonArray.remove(i);
            }
        }
        friendMongoDao.updateFriendAddAsk(id,jsonArray,tempFlag,false,null);
        return baseResp.ok("回复成功");
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
        FriendAddAsk friendAddAsk = friendMongoDao.findFriendAddAskById(id);
        if(friendAddAsk == null || (!userId.equals(friendAddAsk.getSenderUserId()) && !userId.equals(friendAddAsk.getReceiveUserId()))){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(userId.equals(friendAddAsk.getSenderUserId())){//当前查看信息的是发送者
            friendAddAsk.setAppUserMongoEntity(userMongoDao.findById(friendAddAsk.getReceiveUserId()+""));
            //更改用户消息为已读
            friendMongoDao.updateFriendAddAsk(id,null,"sender",true,null);
        }else{
            friendAddAsk.setAppUserMongoEntity(userMongoDao.findById(friendAddAsk.getSenderUserId()+""));
            //更改用户消息为已读
            friendMongoDao.updateFriendAddAsk(id,null,"receive",true,null);
        }
        //更改用户新消息提示为没有新消息
        updateUserNewMessageTip(userId,false);

        baseResp.setData(friendAddAsk);

        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
    }

    /**
     * 更改用户的请求加好友状态
     * @param id
     * @param status
     * @param userId
     * @return
     */
    @Override
    public BaseResp<Object> updateFriendAddAskStatus(Long id, Integer status, Long userId) {
        FriendAddAsk friendAddAsk = friendMongoDao.findByFriendAddAskId(id);
        if(friendAddAsk == null || !userId.equals(friendAddAsk.getReceiveUserId())){
            return BaseResp.fail("参数错误");
        }
        friendMongoDao.updateFriendAddAsk(id,null,null,null,status);
        //往数据库添加数据
        SnsFriends snsFriends = new SnsFriends();
        snsFriends.setCreatetime(new Date());
        snsFriends.setUserid(friendAddAsk.getSenderUserId());
        snsFriends.setFriendid(friendAddAsk.getReceiveUserId());
        snsFriends.setIspublic("1");
        snsFriendsMapper.insertSelective(snsFriends);

        snsFriends.setUserid(friendAddAsk.getReceiveUserId());
        snsFriends.setFriendid(friendAddAsk.getSenderUserId());
        snsFriendsMapper.insertSelective(snsFriends);
        return new BaseResp<Object>().ok();
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
        List<FriendAddAsk> list = friendMongoDao.friendAddAskList(userId,startNo,pageSize);
        if(list != null && list.size() > 0){
            for(FriendAddAsk friendAddAsk:list){
                if(friendAddAsk.getMessage() != null && friendAddAsk.getMessage().size() > 0){
                    JSONObject jsonObject = friendAddAsk.getMessage().getJSONObject(friendAddAsk.getMessage().size()-1);
                    friendAddAsk.setLastMessage(jsonObject.getString("content"));
                    friendAddAsk.setMessage(null);
                }
                if(userId.equals(friendAddAsk.getSenderUserId())){
                    friendAddAsk.setAppUserMongoEntity(userMongoDao.findById(friendAddAsk.getReceiveUserId()+""));
                }else{
                    friendAddAsk.setAppUserMongoEntity(userMongoDao.findById(friendAddAsk.getSenderUserId()+""));
                }
            }
        }
        BaseResp<Object> baseResp = new BaseResp<Object>();
        baseResp.setData(list);
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
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

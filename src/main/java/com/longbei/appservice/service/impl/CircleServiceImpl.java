package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.CircleMapper;
import com.longbei.appservice.dao.CircleMembersMapper;
import com.longbei.appservice.dao.UserMsgMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.dao.redis.SpringJedisDao;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.CircleService;
import com.longbei.appservice.service.UserMsgService;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wyz on 17/2/28.
 */
@Service("circleService")
public class CircleServiceImpl implements CircleService {

    @Autowired
    private CircleMapper circleMappler;

    @Autowired
    private CircleMembersMapper circleMembersMapper;

    @Autowired
    private IdGenerateService idGenerateService;

    @Autowired
    private UserMsgService userMsgService;

    @Autowired
    private UserMsgMapper userMsgMapper;

    @Autowired
    private UserMongoDao userMongoDao;

    @Autowired
    private SpringJedisDao springJedisDao;

    @Override
    public BaseResp<Object> relevantCircle(String circleName, Integer startNo, Integer pageSize) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("circleName", circleName);
        map.put("startNo", startNo);
        map.put("pageSize", pageSize);
        List<Circle> circleList = circleMappler.findRelevantCircle(map);
        baseResp.setData(circleList);
        baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        return baseResp;
    }

    @Override
    public BaseResp<Object> insertCircle(String userId, String circleTitle, String circlephotos, String circlebrief,
                                         String ptype, Boolean ispublic, Boolean needconfirm, Boolean creategoup) {
        Circle circle = new Circle();
        circle.setCreatetime(new Date());
        circle.setUpdatetime(new Date());
        circle.setCircleid(idGenerateService.getUniqueIdAsLong());
        circle.setCircletitle(circleTitle);
        circle.setCirclephotos(circlephotos);
        circle.setCirclebrief(circlebrief);
        circle.setCreateuserid(Long.parseLong(userId));
        circle.setCircleinvoloed(0);
        circle.setPtype(ptype);
        circle.setIspublic(ispublic);
        circle.setNeedconfirm(needconfirm);
        int row = circleMappler.insert(circle);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if (row > 0) {
            //将圈主放到圈子成员中
            CircleMembers circleMembers = new CircleMembers();
            circleMembers.setCreatetime(new Date());
            circleMembers.setUpdatetime(new Date());
            circleMembers.setItype(0);
            circleMembers.setCircleid(new Long(circle.getId()));
            circleMembers.setUserid(new Long(userId));
            int insertCircleMember = circleMembersMapper.insert(circleMembers);

            if (creategoup) {//需要创建龙信群

            }
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        }
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_01, Constant.RTNINFO_SYS_01);
    }

    /**
     * 校验兴趣圈名字是否可用
     *
     * @param circleTitle
     * @return
     */
    @Override
    public boolean checkCircleTitle(String circleTitle) {
        Circle circle = circleMappler.findCircleByCircleTitle(circleTitle);
        if (circle == null) {
            return true;
        }
        return false;
    }

    @Override
    public BaseResp<Object> updateCircleInfo(Integer circleId, String userId, String circlephotos, String circlebrief, String circleNotice) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if (StringUtils.isEmpty(circlephotos) && StringUtils.isEmpty(circlebrief) && StringUtils.isEmpty(circleNotice)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("circleId", circleId);
        map.put("userId", userId);
        if (StringUtils.isNotEmpty(circlephotos)) {
            map.put("circlephotos", circlephotos);
        }
        if (StringUtils.isNotEmpty(circlebrief)) {
            map.put("circlebrief", circlebrief);
        }
        if (StringUtils.isNotEmpty(circleNotice)) {
            map.put("notice", circleNotice);
        }
        int row = circleMappler.updateCircleInfo(map);

        if (row > 0) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        }
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_81, Constant.RTNINFO_SYS_81);
    }

    @Override
    public BaseResp<Object> selectCircleMember(Long circleId, String username, Integer startNo, Integer pageSize, boolean flag) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("circleId", circleId);
        if (StringUtils.isNotEmpty(username)) {
            map.put("username", username);
        }
        map.put("startNo",startNo);
        map.put("pageSize",pageSize);

        List<CircleMembers> circleMembersList = circleMembersMapper.selectCircleMember(map);

        if (circleMembersList == null || circleMembersList.size() == 0) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        }
        for (CircleMembers circleMembers : circleMembersList) {
            circleMembers.setAppUserMongoEntity(userMongoDao.findById(String.valueOf(circleMembers.getUserid())));
        }

        baseResp.setData(circleMembersList);
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
    }

    @Override
    public BaseResp<Object> insertCircleMember(Long circleId, String userId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        //校验circleId 是否存在
        Circle circle = circleMappler.selectByPrimaryKey(circleId);
        if (circle == null) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }

        //校验是否已经在该兴趣圈
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("circleId", circleId + "");
        map.put("userId", userId);
        CircleMembers circleMembers = circleMembersMapper.findCircleMember(map);
        if (circleMembers != null && circleMembers.getItype() == 1) {
            if (circle.getNeedconfirm()) {
                map.put("iType", 2);
            } else {
                map.put("iType", 0);
            }
            map.put("updateTime", new Date());
            int row = circleMembersMapper.updateCircleMembers(map);
            if (row > 0 && circle.getNeedconfirm()) {
                //发消息通知群主
                noticeCircleCreateUserId(circleId,circle.getCreateuserid());

                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_84, Constant.RTNINFO_SYS_84);
            } else if (row > 0) {
                //修改circle的加圈子人数
                map.put("personNum", 1);
                circleMappler.updateCircleInvoloed(map);

                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            } else {
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_01, Constant.RTNINFO_SYS_01);
            }
        } else if (circleMembers != null && circleMembers.getItype() == 0) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_82, Constant.RTNINFO_SYS_82);
        } else if (circleMembers != null && circleMembers.getItype() == 2) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_83, Constant.RTNINFO_SYS_83);
        }

        CircleMembers newCircleMembers = new CircleMembers();
        newCircleMembers.setCircleid(circleId);
        newCircleMembers.setUserid(Long.parseLong(userId));
        if (circle.getNeedconfirm()) {
            newCircleMembers.setItype(2);
        } else {
            newCircleMembers.setItype(0);
        }
        newCircleMembers.setCreatetime(new Date());
        newCircleMembers.setUpdatetime(new Date());
        int row = circleMembersMapper.insert(newCircleMembers);
        if (row > 0) {
            if (circle.getNeedconfirm()) {//通知群主审核
                noticeCircleCreateUserId(circleId,circle.getCreateuserid());

                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_84, Constant.RTNINFO_SYS_84);
            }
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        }
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_01, Constant.RTNINFO_SYS_01);
    }

    private boolean noticeCircleCreateUserId(Long circleId,Long userId){
        // 校验是否给圈主发送过验证消息,如果已经发送过验证消息,则判断该消息是否已读,如果目前为已读则update为未读
        UserMsg tempUserMsg = userMsgService.findCircleNoticeMsg(circleId,userId);
        if(tempUserMsg != null && "0".equals(tempUserMsg.getIsdel()) && "0".equals(tempUserMsg.getIsread())){
            return true;
        }else if(tempUserMsg != null && ("1".equals(tempUserMsg.getIsdel()) || "1".equals(tempUserMsg.getIsread()))){
            //更改该消息为未删除,且未读
            UserMsg updateUserMsg = new UserMsg();
            updateUserMsg.setId(tempUserMsg.getId());
            updateUserMsg.setIsdel("0");
            updateUserMsg.setIsread("0");
            int updateRow = userMsgMapper.updateByPrimaryKeySelective(updateUserMsg);
            if(updateRow > 0){
                return true;
            }
            return false;
        }
        UserMsg userMsg = new UserMsg();
        userMsg.setCreatetime(new Date());
        userMsg.setUserid(userId);
        userMsg.setFriendid(Long.parseLong(Constant.SQUARE_USER_ID));
        userMsg.setGtype("3");
        userMsg.setMsgtype("11");
        userMsg.setSnsid(circleId);
        userMsg.setRemark("有新的成员申请加入圈子,快去进行审核吧!");
        userMsg.setIsdel("0");
        userMsg.setIsread("0");
        userMsg.setMtype("2");
        int row = userMsgMapper.insertSelective(userMsg);
        if(row > 0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public BaseResp<Object> removeCircleMembers(Long circleId, String userId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("circleId", circleId);
        map.put("userId", userId);
        map.put("iType", 1);
        map.put("updateTime", new Date());
        int row = circleMembersMapper.updateCircleMembers(map);
        if (row > 0) {
            //修改circle的加圈子人数
            map.put("personNum", -1);
            circleMappler.updateCircleInvoloed(map);
        }
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
    }

    @Override
    public BaseResp<Object> circleMemberDetail(Long circleId, Long userId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("circleId", circleId);
        map.put("userId", userId);
        //查询该用户是否在该圈子中
        CircleMembers circleMembers = circleMembersMapper.findCircleMember(map);
        if (circleMembers == null) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_85, Constant.RTNINFO_SYS_85);
        } else if (circleMembers.getItype() != 0) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_85, Constant.RTNINFO_SYS_85);
        }
        //查询该用户在该圈中的信息


        return baseResp;
    }

    @Override
    public BaseResp<Object> circleDetail(Long circleId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        Circle circle = circleMappler.selectByPrimaryKey(circleId);
        if (circle == null) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_86, Constant.RTNINFO_SYS_86);
        }

        //根据用户id获取用户信息
        AppUserMongoEntity appUserMongoEntity = userMongoDao.findById(String.valueOf(circle.getCreateuserid()));
        circle.setAppUserMongoEntity(appUserMongoEntity);

        //获取当日发表的进步数量

        //获取圈子的评论数量


        baseResp.setData(circle);
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
    }

    @Override
    public BaseResp<Object> confirmInsertCircleMember(Long userId, Integer circleMembersId, Boolean confirmFlag) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        //通过circleMembersId查询
        CircleMembers circleMembers = circleMembersMapper.selectByPrimaryKey(circleMembersId);
        if (circleMembersId == null) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        } else if (circleMembers.getItype() == 0) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_87, Constant.RTNINFO_SYS_87);
        } else if (circleMembers.getItype() == 1) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_85, Constant.RTNINFO_SYS_85);
        }
        //校验该用户是否是该兴趣圈的圈主
        Circle circle = circleMappler.selectByPrimaryKey(circleMembers.getCircleid());
        if (circle == null || !circle.getCreateuserid().equals(userId)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_81, Constant.RTNINFO_SYS_81);
        }
        CircleMembers tempCircleMembers = new CircleMembers();
        tempCircleMembers.setId(circleMembers.getId());
        if (confirmFlag) {
            tempCircleMembers.setItype(0);
        } else {
            tempCircleMembers.setItype(1);//退群
        }
        tempCircleMembers.setUpdatetime(new Date());
        //更新circleMembers 信息
        int row = circleMembersMapper.updateByPrimaryKeySelective(tempCircleMembers);
        if (row > 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("circleId", circle.getId());
            map.put("updateTime", new Date());
            //修改circle的加圈子人数
            map.put("personNum", -1);
            circleMappler.updateCircleInvoloed(map);
        }
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
    }

    @Override
    public List<Long> findCircleMemberId(Integer circleId) {
        return circleMembersMapper.findCircleMembersId(circleId);
    }

}

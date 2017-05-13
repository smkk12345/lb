package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.CircleMapper;
import com.longbei.appservice.dao.CircleMembersMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by wyz on 17/2/28.
 */
@Service("circleService")
public class CircleServiceImpl extends BaseServiceImpl implements CircleService {
    private static Logger logger = LoggerFactory.getLogger(CircleServiceImpl.class);

    @Autowired
    private CircleMapper circleMappler;

    @Autowired
    private CircleMembersMapper circleMembersMapper;

    @Autowired
    private IdGenerateService idGenerateService;

    @Autowired
    private UserMsgService userMsgService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private FansService fansService;

    @Autowired
    private UserMongoDao userMongoDao;

    @Autowired
    private CommentMongoService commentMongoService;

    @Override
    public BaseResp<Object> relevantCircle(String circleName, Integer startNo, Integer pageSize) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("circleName", circleName);
            map.put("startNo", startNo);
            map.put("pageSize", pageSize);
            List<Circle> circleList = circleMappler.findRelevantCircle(map);
            baseResp.setData(circleList);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            return baseResp;
        }catch(Exception e){
            logger.error("relevant Circle circleName = {} startNo = {} pageSize = {}",
            		circleName, startNo, pageSize, e);
//            printException(e);
            return baseResp.fail("系统异常");
        }

    }

    @Transactional
    @Override
    public BaseResp<Object> insertCircle(String userId, String circleTitle, String circlephotos, String circlebrief,
                                         String ptype, Boolean ispublic, Boolean needconfirm, Boolean creategoup) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
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
        }catch(Exception e){
        	//TODO 用error
            logger.error("insert Circle userId = {}", userId, e);
//            printExceptionAndRollBackTransaction(e);
            return baseResp.fail("系统异常");
        }

    }

    /**
     * 校验兴趣圈名字是否可用
     * @param circleTitle
     * @return
     */
    @Override
    public boolean checkCircleTitle(String circleTitle) {
        try{
            Circle circle = circleMappler.findCircleByCircleTitle(circleTitle);
            if (circle == null) {
                return true;
            }
            return false;
        }catch(Exception e){
            logger.error("checkCircleTitle circleTitle = {}",circleTitle, e);
//            printException(e);
            return false;
        }

    }

    @Override
    public BaseResp<Object> updateCircleInfo(Integer circleId, String userId, String circlephotos, String circlebrief, String circleNotice) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
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
        }catch(Exception e){
        	//TODO 日志打印的方式比较奇怪
            logger.error("updateCircleInfo circleId = {}, userId = {}, circlephotos = {}, circlebrief = {}, notice = {}", 
            		circleId, userId, circlephotos, circlebrief, circleNotice, e);
//            printException(e);
            return baseResp.fail("系统异常");
        }
    }

    @Override
    public BaseResp<Object> selectCircleMember(Long circleId, String username, Integer startNo, Integer pageSize, boolean flag) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
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
                circleMembers.setAppUserMongoEntity(userMongoDao.getAppUser(String.valueOf(circleMembers.getUserid())));
            }

            baseResp.setData(circleMembersList);
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("select CircleMember circleId:{} username:{} startNo:{} endNo:{}", 
            		circleId, username, startNo, pageSize, e);
//            printException(e);
            return baseResp.fail("系统异常");
        }

    }

    @Transactional
    @Override
    public BaseResp<Object> insertCircleMember(Long circleId, String userId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
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
                    map.put("iType", CircleMembers.pending);
                } else {
                    map.put("iType", CircleMembers.normal);
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
                newCircleMembers.setItype(CircleMembers.pending);
            } else {
                newCircleMembers.setItype(CircleMembers.normal);
            }
            newCircleMembers.setCreatetime(new Date());
            newCircleMembers.setUpdatetime(new Date());
            int row = circleMembersMapper.insert(newCircleMembers);
            if (row > 0) {
                //修改circle的加圈子人数
                map.put("personNum", 1);
                circleMappler.updateCircleInvoloed(map);
                if (circle.getNeedconfirm()) {//通知群主审核
                    noticeCircleCreateUserId(circleId,circle.getCreateuserid());

                    return baseResp.initCodeAndDesp(Constant.STATUS_SYS_84, Constant.RTNINFO_SYS_84);
                }
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_01, Constant.RTNINFO_SYS_01);
        }catch(Exception e){
            logger.error("insert circlemember circleId:{} userId:{}", circleId, userId, e);
//            printExceptionAndRollBackTransaction(e);
            return baseResp.fail("系统异常");
        }
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
            int updateRow = userMsgService.updateByPrimaryKeySelective(updateUserMsg);
            //TODO 一句就好， return updateRow > 0;
//            if(updateRow > 0){
//                return true;
//            }
//            return false;
            return updateRow > 0;
        }
//        UserMsg userMsg = new UserMsg();
//        userMsg.setCreatetime(new Date());
//        userMsg.setUserid(userId);
//        userMsg.setFriendid(Long.parseLong(Constant.SQUARE_USER_ID));
//        userMsg.setGtype("3");
//        userMsg.setMsgtype("11");
//        userMsg.setSnsid(circleId);
//        userMsg.setRemark("有新的成员申请加入圈子,快去进行审核吧!");
//        userMsg.setIsdel("0");
//        userMsg.setIsread("0");
//        userMsg.setMtype("2");
        //gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统 
		//10：榜中  11 圈子中  12 教室中  13:教室批复作业
        BaseResp<Object> baseResp = userMsgService.insertMsg(Constant.SQUARE_USER_ID, userId.toString(), 
        		"", "11", circleId.toString(), 
        		"有新的成员申请加入圈子,快去进行审核吧!", "2", "11", "申请加入圈子", 0);
        if(ResultUtil.isSuccess(baseResp)){
        	return true;
        }
        return false;
//        int row = userMsgMapper.insertSelective(userMsg);
        
        // TODO 一句代替及好  return row > 0;
//        if(row > 0){
//            return true;
//        }else{
//            return false;
//        }
//        return row > 0;
    }

    @Transactional
    @Override
    public BaseResp<Object> removeCircleMembers(Long circleId, String userId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
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
        }catch(Exception e){
        	//TODO 没有把exception 打印出来
            logger.error("removeCircleMembers circleId = {} userId = {}", circleId, userId, e);
//            printExceptionAndRollBackTransaction(e);
            return baseResp.fail();
        }

    }

    @Override
    public BaseResp<Object> circleMemberDetail(Long circleId, Long userId,Long currentUserId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            Map<String,Object> resultMap = new HashMap<String,Object>();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("circleId", circleId);
            map.put("userId", userId);
            //查询该用户是否在该圈子中
            CircleMembers circleMembers = circleMembersMapper.findCircleMember(map);
            if (circleMembers == null || circleMembers.getItype() != 0) {
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_85, Constant.RTNINFO_SYS_85);
            }
            circleMembers.setAppUserMongoEntity(userMongoDao.getAppUser(userId+""));
            resultMap.put("circleMembers",circleMembers);
            resultMap.put("isFriend",friendService.checkIsFriend(currentUserId,userId));
            resultMap.put("isFans",fansService.checkIsFans(currentUserId,userId));
            baseResp.setData(resultMap);
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("see circleMember datail circleId:{} userId:{}", circleId, userId, e);
//            e.printStackTrace();
            return baseResp.fail();
        }

    }

    @Override
    public BaseResp<Object> circleDetail(Long circleId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            Circle circle = circleMappler.selectByPrimaryKey(circleId);
            if (circle == null) {
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_86, Constant.RTNINFO_SYS_86);
            }

            //根据用户id获取用户信息
            AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(circle.getCreateuserid()));
            circle.setAppUserMongoEntity(appUserMongoEntity);

            //获取圈子的评论数量
            circle.setCommentCount(0);
            BaseResp<Integer> result = commentMongoService.selectCommentCountSum(circleId+"","3", "");
            if(result.getCode() == 0){
                circle.setCommentCount(result.getData());
            }

            baseResp.setData(circle);
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("see circleDetail circleId:{}", circleId, e);
//            printException(e);
            return baseResp.fail();
        }

    }

    @Transactional
    @Override
    public BaseResp<Object> confirmInsertCircleMember(Long userId, Integer circleMembersId, Boolean confirmFlag) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
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
        }catch(Exception e){
        	//TODO 日志打印的应该不符合预期把
            logger.error("confirm insert circleMember userId:{} circleMembersId:{} confirmFlag:{}",
                    userId, circleMembersId, confirmFlag, e);
//            printExceptionAndRollBackTransaction(e);
            return baseResp.fail(Constant.RTNINFO_SYS_01);
        }
    }

    @Override
    public List<Long> findCircleMemberId(Integer circleId) {
        try{
            return circleMembersMapper.findCircleMembersId(circleId);
        }catch(Exception e){
        	//TODO 一次性打印日志就好啊， 分两次打印，而且第二次是打印到控制台的，不好追踪查看
            logger.error("find CircleMember circleId:{}", circleId, e);
//            printException(e);
            return new ArrayList<Long>();
        }

    }

}

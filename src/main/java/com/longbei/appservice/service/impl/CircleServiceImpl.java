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

    @Autowired
    private UserBehaviourService userBehaviourService;
    @Autowired
    private UserService userService;

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
            circle.setCreategoup(creategoup);
            circle.setPtype(ptype);
            circle.setIspublic(ispublic);
            circle.setNeedconfirm(needconfirm);
            int row = circleMappler.insertSelective(circle);
            if (row > 0) {
                //将圈主放到圈子成员中
                CircleMembers circleMembers = new CircleMembers();
                circleMembers.setCreatetime(new Date());
                circleMembers.setUpdatetime(new Date());
                circleMembers.setItype(CircleMembers.normal);
                circleMembers.setCircleid(circle.getCircleid());
                circleMembers.setUserid(new Long(userId));
                int insertCircleMember = circleMembersMapper.insert(circleMembers);
                if(insertCircleMember > 0){
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("circleId",circle.getCircleid());
                    map.put("updateTime",new Date());
                    //修改circle的加圈子人数
                    map.put("personNum", 1);
                    circleMappler.updateCircleInvoloed(map);
                }

                if (creategoup) {//需要创建龙信群

                }
                baseResp.setData(circle);
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_01, Constant.RTNINFO_SYS_01);
        }catch(Exception e){
        	//TODO 用error
            logger.error("insert Circle userId = {}", userId, e);
            printExceptionAndRollBackTransaction(e);
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
            logger.error("checkCircleTitle circleTitle = {} errorMsg:{}",circleTitle, e);
//            printException(e);
            return false;
        }

    }

    @Override
    public BaseResp<Object> updateCircleInfo(Long circleId, String userId, String circlephotos, String circlebrief, String circleNotice,Boolean needconfirm) {
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
            if(needconfirm != null){
                map.put("needconfirm",needconfirm);
            }
            map.put("updateTime",new Date());
            int row = circleMappler.updateCircleInfo(map);

            if (row > 0) {
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_81, Constant.RTNINFO_SYS_81);
        }catch(Exception e){
        	//TODO 日志打印的方式比较奇怪
            logger.error("updateCircleInfo circleId = {}, userId = {}, circlephotos = {}, circlebrief = {}, notice = {}", 
            		circleId, userId, circlephotos, circlebrief, circleNotice, e);
//          printException(e);
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
            map.put("itype",CircleMembers.normal);
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
    public BaseResp<Object> insertCircleMember(Long circleId, String userId,String remark) {
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
            if (circleMembers != null && circleMembers.getItype() == CircleMembers.delete) {
                if (circle.getNeedconfirm()) {
                    map.put("iType", CircleMembers.pending);
                } else {
                    map.put("iType", CircleMembers.normal);
                }
                if(StringUtils.isNotEmpty(remark)){
                    map.put("remark",remark);
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
            } else if (circleMembers != null && circleMembers.getItype() == CircleMembers.normal) {
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_82, Constant.RTNINFO_SYS_82);
            } else if (circleMembers != null && circleMembers.getItype() == CircleMembers.pending) {
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
            if(StringUtils.isNotEmpty(remark)){
                newCircleMembers.setRemark(remark);
            }
            newCircleMembers.setCreatetime(new Date());
            newCircleMembers.setUpdatetime(new Date());
            int row = circleMembersMapper.insertSelective(newCircleMembers);
            if (row > 0) {
                //修改circle的加圈子人数
                map.put("personNum", 1);
                circleMappler.updateCircleInvoloed(map);
                //加入圈子成功获得龙分
                UserInfo userInfo = userService.selectJustInfo(Long.parseLong(userId));
                userBehaviourService.pointChange(userInfo,"DAILY_ADDCIRCLE",circle.getPtype(),null,0,0);
                if (circle.getNeedconfirm()) {//通知群主审核
                    noticeCircleCreateUserId(circleId,circle.getCreateuserid());
                    baseResp.getExpandData().put("status",CircleMembers.pending);
                    return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_84);
                }
                baseResp.getExpandData().put("status",CircleMembers.normal);
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_01, Constant.RTNINFO_SYS_01);
        }catch(Exception e){
            logger.error("insert circlemember circleId:{} userId:{}", circleId, userId);
            printExceptionAndRollBackTransaction(e);
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
            return updateRow > 0;
        }
        //gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统
		//10：榜中  11 圈子中  12 教室中  13:教室批复作业
        BaseResp<Object> baseResp = userMsgService.insertMsg(Constant.SQUARE_USER_ID, userId.toString(), 
        		"", "11", circleId.toString(), 
        		"有新的成员申请加入圈子,快去进行审核吧!", "2", "11", "申请加入圈子", 0, "", "");
        if(ResultUtil.isSuccess(baseResp)){
        	return true;
        }
        return false;
    }

    @Transactional
    @Override
    public BaseResp<Object> removeCircleMembers(Long circleId, String userId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("circleId", circleId);
            map.put("userId", userId);
            map.put("iType", CircleMembers.delete);
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
            logger.error("removeCircleMembers circleId = {} userId = {}", circleId, userId);
            printExceptionAndRollBackTransaction(e);
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
            if (circleMembers == null || circleMembers.getItype() != CircleMembers.normal) {
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
    public BaseResp<Object> circleDetail(Long userid,Long circleId) {
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
//            BaseResp<Integer> result = commentMongoService.selectCommentCountSum(circleId+"","3", "");
//            if(result.getCode() == 0){
//                circle.setCommentCount(result.getData());
//            }

            if(userid != null && !Constant.VISITOR_UID.equals(userid+"")){
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("userId",userid);
                map.put("circleId",circleId);
                CircleMembers circleMembers = this.circleMembersMapper.findCircleMember(map);
                if(circleMembers != null && circleMembers.getItype() == CircleMembers.normal){
                    circle.setHasjoin(1);//已加入
                }else if(circleMembers != null && circleMembers.getItype() == CircleMembers.pending){
                    circle.setHasjoin(2);//审核中
                }
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
            } else if (circleMembers.getItype() == CircleMembers.normal) {
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_87, Constant.RTNINFO_SYS_87);
            } else if (circleMembers.getItype() == CircleMembers.delete) {
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
                tempCircleMembers.setItype(CircleMembers.normal);
            } else {
                tempCircleMembers.setItype(CircleMembers.delete);//退群
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
            logger.error("confirm insert circleMember userId:{} circleMembersId:{} confirmFlag:{}",
                    userId, circleMembersId, confirmFlag);
            printExceptionAndRollBackTransaction(e);
            return baseResp.fail(Constant.RTNINFO_SYS_01);
        }
    }

    @Override
    public List<Long> findCircleMemberId(Long circleId) {
        try{
            return circleMembersMapper.findCircleMembersId(circleId);
        }catch(Exception e){
        	//TODO 一次性打印日志就好啊， 分两次打印，而且第二次是打印到控制台的，不好追踪查看
            logger.error("find CircleMember circleId:{}", circleId, e);
            return new ArrayList<Long>();
        }

    }

    /**
     * 查询圈子列表
     * @param pType 十项分类的id
     * @param keyword 关键字
     * @param startNum
     * @param pageSize
     * @return
     */
    @Override
    public BaseResp<Object> circleList(Long userId,Integer pType, String keyword, Integer startNum, Integer pageSize) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            Map<String,Object> map = new HashMap<String,Object>();
            if(pType == 10){
                map.put("isrecommend",1);
            }else{
                map.put("pType",pType);
            }
            if(StringUtils.isNotEmpty(keyword)){
                map.put("keyword",keyword);
            }
            map.put("ispublic",1);
            map.put("startNum",startNum);
            map.put("pageSize",pageSize);
            List<Circle> circleList = this.circleMappler.findCircleList(map);
            if(circleList == null){
                circleList= new ArrayList<Circle>();
            }
            for(Circle circle:circleList){
                //查询圈主
                circle.setAppUserMongoEntity(this.userMongoDao.getAppUser(circle.getCreateuserid()+""));

                if(userId != null){
                    //判断该用户是否加入了圈子
                    Map<String,Object> circleMemberMap = new HashMap<String,Object>();
                    circleMemberMap.put("circleId",circle.getCircleid());
                    circleMemberMap.put("userId",userId);
                    circleMemberMap.put("itype",CircleMembers.normal);
                    CircleMembers circleMembers = this.circleMembersMapper.findCircleMember(map) ;
                    if(circleMembers != null){
                        circle.setHasjoin(1);
                    }
                }
            }
            baseResp.setData(circleList);
            return baseResp.initCodeAndDesp();
        }catch (Exception e){
            logger.error("circle list error pType:{} keyword:{} startNum:{} pageSize:{} errorMsg:{}",pType,keyword,startNum,pageSize,e);
        }
        return baseResp;
    }

}

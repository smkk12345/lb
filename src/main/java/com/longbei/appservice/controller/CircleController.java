package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.entity.UserMsg;
import com.longbei.appservice.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.longbei.appservice.common.constant.Constant.USER_PRIVILEGE_ADD_CIRCLE;

/**
 * Created by wangyongzhi on 17/2/28.
 */
@RestController
@RequestMapping(value="circle")
public class CircleController {

    @Autowired
    private CircleService circleService;
    @Autowired
    private UserBehaviourService userBehaviourService;
    @Autowired
    private ImproveService improveService;
    @Autowired
    private ImproveCircleService improveCircleService;
    @Autowired
    private UserMsgService userMsgService;

    private static Logger logger = LoggerFactory.getLogger(CircleController.class);

    /**
     * 根据圈子的名称查询类似的圈子
     * @url http://ip:port/app_service/circle/relevantCircle
     * @param circleName 圈子名称
     * @param startNo 开始下标
     * @param endNo 结束下标
     * @author wangyongzhi
     * @return
     */
    @RequestMapping(value="relevantCircle")
    public BaseResp<Object> relevantCircle(String circleName,Integer startNo,Integer endNo){
        logger.info("circleName={},startNo={},endNo={}",circleName,startNo,endNo);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(StringUtils.hasBlankParams(circleName)){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            return baseResp;
        }
        if(startNo == null || startNo < 0){
            startNo = Integer.parseInt(Constant.DEFAULT_START_NO);
        }
        Integer pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        if(endNo != null && endNo > startNo){
            pageSize = endNo - startNo;
        }
        logger.info("query relevantCircle circleName:{}, startNo:{},pageSize:{} ",circleName,startNo,pageSize);
        baseResp = circleService.relevantCircle(circleName,startNo,pageSize);
        return baseResp;
    }

    /**
     * 新建圈子
     * @url http://ip:port/app_service/circle/insertCircle
     * @param userId 用户Id
     * @param circleTitle 圈子名称
     * @param circlephotos 圈子图片
     * @param circlebrief 圈子简介
     * @param ptype 圈子类型
     * @param ispublic 是否所有人可见
     * @param needconfirm 加圈子是否需要圈主验证
     * @param creategoup 是否同时建龙信群
     * @author wangyongzhi
     * @return
     */
    @RequestMapping(value="insertCircle")
    public BaseResp<Object> insertCircle(String userId,String circleTitle,String circlephotos,String circlebrief,
                                         String ptype,Boolean ispublic,Boolean needconfirm,Boolean creategoup){
        logger.info("insert Circle userId:{} circleTitle:{} circlephotos:{} circlebrief:{} ptype:{} ispublic:{} needconfirm:{} creategoup:{}",
                userId,circleTitle,circlephotos,circlebrief,ptype,ispublic,needconfirm,creategoup);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(StringUtils.hasBlankParams(userId,circleTitle,circlephotos,circlebrief,ptype)){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            return baseResp;
        }
        if(ispublic == null){
            ispublic = true;
        }
        if(needconfirm == null){
            needconfirm = true;
        }
        if(creategoup == null){
            creategoup = false;
        }
        //校验用户是否有该权限
        UserInfo userInfo = null;
        baseResp = userBehaviourService.hasPrivilege(userInfo,Constant.PrivilegeType.createCircle,null);
        if(baseResp.getCode() != 0){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_88,Constant.RTNINFO_SYS_88);
        }
        //校验兴趣圈名是否可用
        boolean flag = circleService.checkCircleTitle(circleTitle);
        if(flag){
            baseResp = circleService.insertCircle(userId,circleTitle,circlephotos,circlebrief,ptype,ispublic,needconfirm,creategoup);
        }else{
            baseResp = baseResp.initCodeAndDesp(Constant.STATUS_SYS_80, Constant.RTNINFO_SYS_80);
        }
        return baseResp;
    }

    /**
     * 更新兴趣圈信息
     * @url http://ip:port/app_service/circle/updateCircleInfo
     * @param circleId 兴趣圈id
     * @param userId 当前用户id
     * @param circlePhotos 兴趣圈图片
     * @param circleBrief 兴趣圈简介
     * @param needConfirm 是否需要验证 boolean
     * @return
     */
    @RequestMapping(value="updateCircleInfo")
    public BaseResp<Object> updateCircleInfo(Long circleId,String userId,String circlePhotos,String circleBrief,Boolean needConfirm){
        logger.info("circleId={},userId={},circlephotos={},circlebrief={}",circleId,userId,circlePhotos,circleBrief);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(circleId == null || (StringUtils.isEmpty(userId) && StringUtils.isEmpty(circlePhotos) && StringUtils.isEmpty(circleBrief) && needConfirm == null)){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            return baseResp;
        }
        baseResp = circleService.updateCircleInfo(circleId,userId,circlePhotos,circleBrief,null,needConfirm);
        return baseResp;
    }

    /**
     * 更改兴趣圈公告
     * @url http://ip:port/app_service/circle/updateCircleNotice
     * @param circleId 兴趣圈id
     * @param userId 当前登录用户Id
     * @param circleNotice 兴趣圈公告
     * @param isNotice boolean 是否@圈子成员
     * @return
     */
    @RequestMapping(value="updateCircleNotice")
    public BaseResp<Object> updateCircleNotice(Long circleId,String userId,String circleNotice,Boolean isNotice){
        logger.info("circleId={},userId={},circleNotice={},isNotice={}",circleId,userId,circleNotice,isNotice);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(circleId == null || StringUtils.hasBlankParams(userId,circleNotice)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = circleService.updateCircleInfo(circleId,userId,null,null,circleNotice,null);
        if(baseResp.getCode() == 0 && isNotice != null && isNotice){
            UserMsg userMsg = new UserMsg();
            userMsg.setCreatetime(new Date());
            userMsg.setFriendid(Long.parseLong(userId));
            userMsg.setMsgtype("11");
//            userMsg.setSnsid(new Long(circleId));
            userMsg.setRemark("圈子系统公告更新了,请及时关注圈子动态哦!");
            userMsg.setIsdel("0");
            userMsg.setIsread("0");
            userMsg.setGtype("3");
            userMsg.setGtypeid(new Long(circleId));
            userMsg.setTitle("申请加入圈子");
            //gtype  0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统 
			//10：榜中  11 圈子中  12 教室中  13:教室批复作业
            userMsg.setMtype("11");

            //通知所有用户
            List<Long> userIdList = circleService.findCircleMemberId(circleId);
            userMsgService.batchInsertUserMsg(userIdList,userMsg);
        }
        return baseResp;
    }

    /**
     * 查询圈子成员 包含成员在兴趣圈中赞,花 等信息
     * @url http://ip:port/app_service/circle/selectCircleMember
     * @param userid 当前登录用户id
     * @param circleId 圈子id
     * @param startNo
     * @param endNo
     * @return
     */
    @RequestMapping(value="selectCircleMember")
    public BaseResp<Object> selectCircleMember(Long userid,Long circleId,Integer startNo,Integer endNo,String username){
        logger.info("select circleMember circleId:{} startNo:{} endNo:{} username:{}",circleId,startNo,endNo,username);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(circleId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(startNo == null){
            startNo = Integer.parseInt(Constant.DEFAULT_START_NO);
        }
        Integer pageSize= Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        if(endNo != null && endNo > startNo){
            pageSize = endNo - startNo;
        }
        if(userid == -1){
            userid = null;
        }
        baseResp = circleService.selectCircleMember(userid,circleId,username,startNo,pageSize,true);

        return baseResp;
    }

    /**
     * 查询圈子中成员的基本信息 不包含赞 花 信息
     * @param circleId 兴趣圈id
     * @param startNo 开始下标
     * @param endNo 结束下标
     * @return
     */
    @RequestMapping(value="selectCircleMemberBaseInfo")
    public BaseResp<Object> selectCircleMemberBaseInfo(Long userid,Long circleId,Integer startNo,Integer endNo){
        logger.info("circleId={},startNo={},endNo={}",circleId,startNo,endNo);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(circleId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(startNo == null || startNo < 0){
            startNo =Integer.parseInt(Constant.DEFAULT_START_NO);
        }
        Integer pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        if(endNo != null && endNo > startNo){
            pageSize = endNo - startNo;
        }
        baseResp = circleService.selectCircleMember(userid,circleId,null,startNo,pageSize,false);

        return baseResp;
    }

    /**
     * 加入兴趣圈
     * @url http://ip:port/app_service/circle/insertCircleMembers
     * @param circleId 兴趣圈Id
     * @param userId 用户Id
     * @param remark 备注
     * @return
     */
    @RequestMapping(value="insertCircleMembers")
    public BaseResp<Object> insertCircleMembers(Long circleId,String userId,String remark){
        logger.info("insert circleMembers circleId:{}  userId:{}",circleId,userId);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(circleId == null || StringUtils.isBlank(userId)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }

        baseResp = circleService.insertCircleMember(circleId,userId,remark);
        return baseResp;
    }

    /**
     * 退出圈子
     * @url http://ip:port/app_service/circle/removeCircleMembers
     * @param circleId 兴趣圈id
     * @param userId 用户id
     * @return
     */
    @RequestMapping(value="removeCircleMembers")
    public BaseResp<Object> removeCircleMembers(Long circleId,String userId){
        logger.info("remove circleMembers circleId:{}  userId:{}",circleId,userId);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(circleId == null || StringUtils.isBlank(userId)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }

        baseResp = circleService.removeCircleMembers(circleId,userId);
        return baseResp;
    }

    /**
     * 查看兴趣圈某个成员详情
     * @url http://ip:port/app_service/circle/circleMemberDetail
     * @param circleId 兴趣圈ID
     * @param userId 用户Id
     * @param currentUserId 当前登录用户Id
     * @return
     */
    @RequestMapping(value="circleMemberDetail")
    public BaseResp<Object> circleMemberDetail(Long circleId,Long userId,Long currentUserId){
        logger.info("circleId={},userId={},currentUserId={}",circleId,userId,currentUserId);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(circleId == null || userId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        logger.info("query circleMember Detail circleId:{} userId:{} currentUserId:{}",circleId,userId,currentUserId);
        baseResp = circleService.circleMemberDetail(circleId,userId,currentUserId);

        return baseResp;
    }

    /**
     * 查看兴趣圈中好友的所有进步
     * @url http://ip:port/app_service/circle/circleMemberImprove
     * @param circleId 兴趣圈id
     * @param userId 用户id
     * @param currentUserId 当前登录用户
     * @param startNo 开始下标
     * @param endNo 结束下标
     * @return
     */
    @RequestMapping(value="circleMemberImprove")
    public BaseResp<Object> circleMemberImprove(Long circleId,Long userId,Long currentUserId,Integer startNo,Integer endNo){
        logger.info("circleId={},userId={},currentUserId={},startNo={},endNo={}",circleId,userId,currentUserId,startNo,endNo);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(circleId == null || userId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(startNo == null){
            startNo = Integer.parseInt(Constant.DEFAULT_START_NO);
        }
        Integer pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        if(endNo != null && endNo > startNo){
            pageSize = endNo -startNo;
        }
        List<Improve> improveCircleList = improveService.findCircleMemberImprove(circleId,userId,currentUserId,startNo,pageSize);
        baseResp.setData(improveCircleList);
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
    }

    /**
     * 查询兴趣圈信息
     * @url http://ip:port/app_service/circle/circleDetail
     * @param userid 用户id
     * @param circleId 兴趣圈id
     * @return
     */
    @RequestMapping(value="circleDetail")
    public BaseResp<Object> circleDetail(Long userid,Long circleId){
        logger.info("circleDetail userid:{} circleId={}",userid,circleId);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(circleId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = circleService.circleDetail(userid,circleId);

        return baseResp;
    }

    /**
     * 查询圈子的 进步
     * @url http://ip:port/app_service/circle/improveCircleList
     * @param circleId 圈子id
     * @param userId 当前登录用户id
     * @param orderby 0.按时间倒序排列 1.按热度排列
     * @param startNo
     * @param endNo
     * @return
     */
    @RequestMapping(value="improveCircleList")
    public BaseResp<Object> improveCircleList(String circleId,String userId,String orderby,Integer startNo,Integer endNo){
        logger.info("circleId={},userId={},orderby={},startNo={},endNo={}",circleId,userId,orderby,startNo,endNo);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(StringUtils.hasBlankParams(circleId,userId)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(startNo == null || startNo < 0){
            startNo = Integer.parseInt(Constant.DEFAULT_START_NO);
        }
        Integer pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        if(endNo != null && endNo > startNo){
            pageSize = endNo - startNo;
        }
        List<Improve> improveList = new ArrayList<Improve>();
        improveList = improveService.selectCircleImproveList(userId,circleId,Constant.IMPROVE_LIST_ALL,orderby,startNo,pageSize);
        baseResp.setData(improveList);

        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
    }

    /**
     * 圈主审核成员的加圈请求
     * @url http://ip:port/app_service/circle/improveCircleList
     * @param userId 当前登录用户id 用于校验权限
     * @param circleMembersId 成员在圈子中的id
     * @param confirmFlag  是否同意入圈 true代表同意 false代表不同意
     * @return
     */
    @RequestMapping(value="confirmInsertCircleMember")
    public BaseResp<Object> confirmInsertCircleMember(Long userId,Integer circleMembersId,Boolean confirmFlag){
        logger.info("userId={},circleMembersId={},confirmFlag={}",userId,circleMembersId,confirmFlag);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userId == null || circleMembersId == null || confirmFlag == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }

        baseResp = circleService.confirmInsertCircleMember(userId,circleMembersId,confirmFlag);

        return baseResp;
    }

    /**
     * 查询圈子列表
     * @url http://ip:port/app_service/circle/circleList
     * @param userid 用户id
     * @param pType 十项分类id
     * @param keyword 关键字
     * @param startNum 开始下标
     * @param pageSize
     * @return
     */
    @RequestMapping(value="circleList")
    public BaseResp<Object> circleList(Long userid,Integer pType,String keyword,Integer startNum,Integer pageSize){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(pType == null && StringUtils.isEmpty(keyword)){
            pType = 10;
        }
        if(startNum == null){
            startNum = 0;
        }
        if(pageSize == null){
            pageSize = 15;
        }
        if(userid != null && Constant.VISITOR_UID.equals(userid+"")){
            userid = null;
        }
        return this.circleService.circleList(userid,pType,keyword,startNum,pageSize);
    }

}

package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.Improve;
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
        logger.info("insert Circle userId:{} circleTitle:{} circlephotos:{} circlebrief:{}",0,circleTitle,circlephotos,circlebrief);
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
        baseResp = userBehaviourService.hasPrivilege(Long.parseLong(userId),null,USER_PRIVILEGE_ADD_CIRCLE);
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
     * @param circlephotos 兴趣圈图片
     * @param circlebrief 兴趣圈简介
     * @return
     */
    @RequestMapping(value="updateCircleInfo")
    public BaseResp<Object> updateCircleInfo(Integer circleId,String userId,String circlephotos,String circlebrief){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(circleId == null || StringUtils.hasBlankParams(userId,circlephotos,circlebrief)){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            return baseResp;
        }
        baseResp = circleService.updateCircleInfo(circleId,userId,circlephotos,circlebrief,null);
        return baseResp;
    }

    /**
     * 更改兴趣圈通知
     * @param circleId 兴趣圈id
     * @param userId 当前登录用户Id
     * @param circleNotice 兴趣圈公告
     * @return
     */
    @RequestMapping(value="updateCircleNotice")
    public BaseResp<Object> updateCircleNotice(Integer circleId,String userId,String circleNotice,Boolean isNotice){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(circleId == null || StringUtils.hasBlankParams(userId,circleNotice)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = circleService.updateCircleInfo(circleId,userId,null,null,circleNotice);
        if(baseResp.getCode() == 0 && isNotice != null && isNotice){
            UserMsg userMsg = new UserMsg();
            userMsg.setCreatetime(new Date());
            userMsg.setFriendid(Long.parseLong(userId));
            userMsg.setMsgtype("11");
            userMsg.setSnsid(new Long(circleId));
            userMsg.setRemark("圈子系统公告更新了,请及时关注圈子动态哦!");
            userMsg.setIsdel("0");
            userMsg.setIsread("0");
            userMsg.setGtype("3");
            userMsg.setMtype("2");

            //通知所有用户
            List<Long> userIdList = circleService.findCircleMemberId(circleId);
            userMsgService.batchInsertUserMsg(userIdList,userMsg);
        }
        return baseResp;
    }

    /**
     * 查询圈子成员 包含成员在兴趣圈中赞,花 等信息
     * @url http://ip:port/app_service/circle/selectCircleMember
     * @param circleId 圈子id
     * @param startNo
     * @param endNo
     * @return
     */
    @RequestMapping(value="selectCircleMember")
    public BaseResp<Object> selectCircleMember(Long circleId,Integer startNo,Integer endNo,String username){
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
        baseResp = circleService.selectCircleMember(circleId,username,startNo,pageSize,true);

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
    public BaseResp<Object> selectCircleMemberBaseInfo(Long circleId,Integer startNo,Integer endNo){
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
        baseResp = circleService.selectCircleMember(circleId,null,startNo,pageSize,false);

        return baseResp;
    }

    /**
     * 加入兴趣圈
     * @url http://ip:port/app_service/circle/insertCircleMembers
     * @param circleId 兴趣圈Id
     * @param userId 用户Id
     * @return
     */
    @RequestMapping(value="insertCircleMembers")
    public BaseResp<Object> insertCircleMembers(Long circleId,String userId){
        logger.info("insert circleMembers circleId:{}  userId:{}",circleId,userId);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(circleId == null || StringUtils.isBlank(userId)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }

        baseResp = circleService.insertCircleMember(circleId,userId);
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
     * 查看兴趣圈成员详情
     * @url http://ip:port/app_service/circle/circleMemberDetail
     * @param circleId 兴趣圈ID
     * @param userId 用户Id
     * @param currentUserId 当前登录用户Id
     * @return
     */
    @RequestMapping(value="circleMemberDetail")
    public BaseResp<Object> circleMemberDetail(Long circleId,Long userId,Long currentUserId){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(circleId == null || userId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        logger.info("query circleMember Detail circleId:{} userId:{} currentUserId:{}",circleId,userId,currentUserId);
        baseResp = circleService.circleMemberDetail(circleId,userId);

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
     * @param circleId 兴趣圈id
     * @return
     */
    @RequestMapping(value="circleDetail")
    public BaseResp<Object> circleDetail(Long circleId){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(circleId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = circleService.circleDetail(circleId);

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
        improveList = improveService.selectCircleImproveList(userId,circleId,null,orderby,startNo,pageSize);
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
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(userId == null || circleMembersId == null || confirmFlag == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }

        baseResp = circleService.confirmInsertCircleMember(userId,circleMembersId,confirmFlag);

        return baseResp;
    }

    @RequestMapping(value="test")
    public BaseResp<Object> test(){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        circleService.test();
        return baseResp.ok();
    }


}

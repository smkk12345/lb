package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.*;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.UserMoneyDetailService;
import com.longbei.appservice.service.UserMsgService;
import com.netflix.discovery.converters.Auto;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户收益
 *
 * @author luye
 * @create 2017-08-14 下午5:21
 **/
@Service
public class UserInComeServiceImpl implements UserInComeService{

    private static Logger logger = LoggerFactory.getLogger(UserInComeServiceImpl.class);

    @Autowired
    private UserInComeMapper userInComeMapper;
    @Autowired
    private UserInComeDetailMapper userInComeDetailMapper;
    @Autowired
    private UserMongoDao userMongoDao;
    @Autowired
    private ClassroomMapper classroomMapper;
    @Autowired
    private UserInComeOrderMapper userInComeOrderMapper;
    @Autowired
    private IdGenerateService idGenerateService;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private UserMoneyDetailService userMoneyDetailService;
    @Autowired
    private UserMsgMapper userMsgMapper;

    /**
     * 添加教室收入,提现（包含明细的处理，消息的处理）
     * @param classroomId  教室id
     * @param originUserId 来源用户id
     * @param userId   教师id
     * @param origin     来源  0 教室学费，1 送礼物，2 提现 3 提现失败 4提现成功，5 退学费，6转入钱包
     * @param type 0 - 收入 1 - 支出
     * @param num  龙币数量
     * @return
     * @author luye
     * @create 2017-08-14 上午9:44
     */
    @Override
    public BaseResp<String> updateUserInCome(String classroomId,
                                             final String userId, String originUserId,
                                             final String origin, String type,
                                             final int num, String detailremarker) {

        BaseResp<String> baseResp = new BaseResp<>();
        //跟新 user_income
        baseResp = updateUserInCome(userId,num,type);
        if (!ResultUtil.isSuccess(baseResp)){
            return baseResp;
        }
        //添加明细
        baseResp = insertUserInComeDetail(userId,origin,num,type,classroomId,originUserId,detailremarker,type);

        return baseResp;
    }


    @Override
    public BaseResp insertUserInComeOrder(final String userid, final int num, String receiptUser,
                                          String receiptBank, String receiptNum, final String origin) {
        BaseResp baseResp = new BaseResp();
        baseResp = updateUserInCome(null,userid,"0",origin,"1",num,null);
        if (!ResultUtil.isSuccess(baseResp)){
            return baseResp;
        }
        UserInComeOrder userInComeOrder = new UserInComeOrder();
        userInComeOrder.setUioid(idGenerateService.getUniqueIdAsLong());
        userInComeOrder.setUserid(Long.parseLong(userid));
        userInComeOrder.setDetailid((Long) baseResp.getData());
        userInComeOrder.setNum(String.valueOf(num));
        userInComeOrder.setCreatetime(new Date());
        //转入钱包
        if ("6".equals(origin)){
            baseResp = userInComeToWallet(userid,userInComeOrder);
        }
        //银行卡，支付宝
        if ("2".equals(origin)){
            userInComeOrder.setUiostatus(0);
            userInComeOrder.setReceiptUser(receiptUser);
            userInComeOrder.setReceiptBank(receiptBank);
            userInComeOrder.setReceiptNum(receiptNum);
            int res = userInComeOrderMapper.insertSelective(userInComeOrder);
            if (res > 0){
                baseResp.initCodeAndDesp();
            }
        }
        if (ResultUtil.isSuccess(baseResp)){
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    sendUserInComeMessage(userid,num,origin);
                }
            });
        }
        return baseResp;
    }

    @Override
    public BaseResp<UserInCome> selectUserInCome(String userid) {
        BaseResp<UserInCome> baseResp = new BaseResp<>();
        try {
            UserInCome userInCome = userInComeMapper.selectByUserId(userid);
            baseResp.initCodeAndDesp();
            baseResp.setData(userInCome);
        } catch (Exception e) {
            logger.error("select userincome info userid={} is error:",userid,e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<UserInComeDetail> selectUserInComeDetail(String detailId,String detailType){
        BaseResp<UserInComeDetail> baseResp = new BaseResp<>();
        if ("0".equals(detailType)){
            UserInComeDetail userInComeDetail = userInComeDetailMapper.selectUserInComeInDetail(detailId);
            initUserInComeInfo(userInComeDetail);
            baseResp.initCodeAndDesp();
            baseResp.setData(userInComeDetail);
        }
        if ("1".equals(detailType)){
            UserInComeDetail userInComeDetail = userInComeDetailMapper.selectUserInComeOutDetail(detailId);
            baseResp.initCodeAndDesp();
            baseResp.setData(userInComeDetail);
        }
        return baseResp;
    }



    @Override
    public BaseResp<Page<UserInComeDetail>> selectUserInComeDetailList
            (UserInComeDetail userInComeDetail, Integer pageNo, Integer pageSize, boolean istotalinfo) {
        BaseResp<Page<UserInComeDetail>> baseResp = new BaseResp<>();
        Page<UserInComeDetail> page = new Page<>(pageNo,pageSize);
        try {
            int totalCount = userInComeDetailMapper.selectCount(userInComeDetail);
            List<UserInComeDetail> list = userInComeDetailMapper.selectList(userInComeDetail,
                    pageSize * (pageNo - 1),pageSize);
            if ("0".equals(userInComeDetail.getDetailtype())){
                for (UserInComeDetail userInComeDetail1 : list){
                    initUserInComeInfo(userInComeDetail1);
            }
            }
            if ("1".equals(userInComeDetail.getDetailtype())){
                for (UserInComeDetail userInComeDetail1 : list){
                    int itype = Integer.parseInt(userInComeDetail1.getItype());
                    if (itype >= 2 && itype < 4){
                        //TODO 这个地方可以从 user_income_order 中通过 detailid 获取 后期再说吧
                        UserInComeDetail user = userInComeDetailMapper.selectUserInComeOutDetail
                                (String.valueOf(userInComeDetail1.getDetailid()));
                        if (null != user){
                            BeanUtils.copyProperties(userInComeDetail1,user);
                        }
                    }
                }
            }
            Page.setPageNo(pageNo,totalCount,pageSize);
            page.setTotalCount(totalCount);
            page.setList(list);
            baseResp.initCodeAndDesp();
            baseResp.setData(page);
            Map<String,Object> map = new HashedMap();
            if (istotalinfo){
                map.put("userInCome",selectUserInCome(userInComeDetail.getUserid()+"").getData());
            }
            if (!StringUtils.isBlank(userInComeDetail.getCsourcetype())){
                map.put("totalnum",null==userInComeDetailMapper.selectTotalCoin(userInComeDetail.getCsourcetype())?0:userInComeDetailMapper.selectTotalCoin(userInComeDetail.getCsourcetype()));
            }
            baseResp.setExpandData(map);
        } catch (Exception e) {
            logger.error("selectUserInComeDetailList is error:",e);
        }
        return baseResp;
    }


    @Override
    public BaseResp<Page<UserInComeOrder>> selectUserIncomeOrderList(String receiptUser, String receiptNum,
                                                                     String nickname, String uiostatus,
                                                                     Integer pageNo, Integer pagesize) {

        BaseResp<Page<UserInComeOrder>> baseResp = new BaseResp<>();
        Page<UserInComeOrder> page = new Page<>(pageNo,pagesize);
        List<String> userids = new ArrayList<>();
        try {
            if (!StringUtils.isBlank(nickname)){
                AppUserMongoEntity appUserMongoEntity = new AppUserMongoEntity();
                appUserMongoEntity.setNickname(nickname);
                List<AppUserMongoEntity> list = userMongoDao.getAppUsers(appUserMongoEntity);
                for (AppUserMongoEntity appuser : list){
                    userids.add(String.valueOf(appuser.getUserid()));
                }
            }
            UserInComeOrder userInComeOrder = new UserInComeOrder();
            userInComeOrder.setReceiptUser(receiptUser);
            userInComeOrder.setReceiptNum(receiptNum);
            userInComeOrder.setUiostatus(Integer.parseInt(uiostatus));
            userInComeOrder.setUserids(userids);
            int totalCount = userInComeOrderMapper.selectCount(userInComeOrder);
            List<UserInComeOrder> list = userInComeOrderMapper.selectList(userInComeOrder,pagesize*(pageNo - 1),pagesize);
            for(int i=0;i<list.size();i++) {
                AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(list.get(i).getUserid()+"");
                list.get(i).setAppUserMongoEntity(appUserMongoEntity);
            }
            page.setTotalCount(totalCount);
            page.setList(list);
            baseResp.initCodeAndDesp();
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("selectUserIncomeOrderList uiostatus={} is error:",uiostatus,e);
        }

        return baseResp;
    }


    @Override
    public BaseResp updateUserIncomeOrderStatus(String uioid, String uiostatus, String deeloption) {
        BaseResp baseResp = new BaseResp();

        final UserInComeOrder uorder = userInComeOrderMapper.selectByPrimaryKey(uioid);
        if (null == uorder){
            return baseResp;
        }
        String date= DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
        UserInComeOrder userInComeOrder = new UserInComeOrder();
        userInComeOrder.setUioid(Long.parseLong(uioid));
        userInComeOrder.setUiostatus(Integer.parseInt(uiostatus));
        userInComeOrder.setDealoption(deeloption);
        userInComeOrder.setUpdatetime(new Date());
        try {
            int res = 0;
            UserInComeDetail userInComeDetail = new UserInComeDetail();
            final String oldnum = uorder.getNum();
            if ("2".equals(uiostatus) || "3".equals(uiostatus)){
                res = userInComeMapper.updateOutGoByUserId(String.valueOf(uorder.getUserid())
                        ,Integer.parseInt(oldnum)*-1,new Date());
                userInComeDetail.setDetailstatus("2");
                threadPoolTaskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        sendUserInComeMessage(String.valueOf(uorder.getUserid()),Integer.parseInt(oldnum),"3");
                    }
                });
            }
            if (res > 0){
                userInComeOrderMapper.updateByPrimaryKeySelective(userInComeOrder);
                userInComeDetail.setDetailid(uorder.getDetailid());
                userInComeDetail.setUpdatetime(new Date());
                if ("4".equals(uiostatus)){
                    userInComeDetail.setDetailstatus("1");
                    threadPoolTaskExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            sendUserInComeMessage(String.valueOf(uorder.getUserid()),Integer.parseInt(oldnum),"4");
                        }
                    });
                }
                userInComeDetailMapper.updateByPrimaryKeySelective(userInComeDetail);
                baseResp.initCodeAndDesp();
            }
        } catch (Exception e) {
            logger.error("updateUserIncomeOrderStatus uioid={} uiostatus={} is error:",uioid,uiostatus,e);
        }

        return baseResp;
    }

    /**
     *
     * @param userid 用户id
     * @param num  数量
     * @param type  0 - 收入 1 - 支出
     * @return
     */
    private BaseResp updateUserInCome(String userid,Integer num,String type){
        BaseResp baseResp = new BaseResp();
        if (StringUtils.hasBlankParams(userid,type)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        //收入
        if ("0".equals(type)){
            baseResp = updateOrSaveUserInCome(userid,num);
        }
        //支出
        if ("1".equals(type)){
            baseResp = updateUserInComeOutGo(userid,num);
        }
        return baseResp;
    }


    private BaseResp updateOrSaveUserInCome(String userid,Integer num){
        int res = 0;
        BaseResp baseResp = new BaseResp();
        try {
            UserInCome userInCome = userInComeMapper.selectByUserId(userid);
            if (null == userInCome){
                UserInCome userInCome1 = new UserInCome();
                userInCome1.setUserid(Long.parseLong(userid));
                userInCome1.setTotal(num);
                userInCome1.setCreatetime(new Date());
                res = userInComeMapper.insertSelective(userInCome1);
            } else {
                res = userInComeMapper.updateTotalByUserId(userid,num,new Date());
            }
        } catch (Exception e) {
            logger.error("updateOrSaveUserInCome userid={} num={} is error:",userid,num,e);
        }
        if (res > 0){
            baseResp.initCodeAndDesp();
        }
        return baseResp;
    }



    private BaseResp updateUserInComeOutGo(String userid,Integer num){
        BaseResp baseResp = new BaseResp();
        int res = 0;
        try {
            res = userInComeMapper.updateOutGoByUserId(userid,num,new Date());
        } catch (Exception e) {
            logger.error("updateUserInComeOutGo userid={} num={} is error:",userid,num,e);
        }
        if (res > 0){
            baseResp.initCodeAndDesp();
        } else {
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_01,"提取失败，收益不足");
        }
        return baseResp;
    }


    private BaseResp insertUserInComeDetail(String userid,String itype,int num,
                                            String businesstype,String businessid,
                                            String originuserid,String remarker,
                                            String detailtype){

        BaseResp baseResp = new BaseResp();

        try {
            UserInComeDetail userInComeDetail = new UserInComeDetail();
            Long detailid = idGenerateService.getUniqueIdAsLong();
            userInComeDetail.setDetailid(detailid);
            userInComeDetail.setUserid(Long.parseLong(userid));
            userInComeDetail.setItype(itype);
            userInComeDetail.setNum(num);
            userInComeDetail.setBusinesstype(businesstype);
            userInComeDetail.setBusinesstid(Long.parseLong(businessid==null?"0":businessid));
            userInComeDetail.setOriginuserid(Long.parseLong(originuserid));
            userInComeDetail.setRemarker(remarker);
            userInComeDetail.setDetailtype(detailtype);
            userInComeDetail.setCreatetime(new Date());
            if ("0".equals(detailtype)){
                userInComeDetail.setDetailstatus("0");
                if ("0".equals(businesstype)){
                    Classroom classroom = classroomMapper.selectByPrimaryKey(Long.parseLong(businessid));
                    userInComeDetail.setCsourcetype(classroom==null?"1":classroom.getSourcetype());
                }
            }
            if ("1".equals(detailtype) && "5".equals(itype)){
                userInComeDetail.setDetailstatus("0");
            }
            int res = userInComeDetailMapper.insertSelective(userInComeDetail);
            if (res > 0){
                baseResp.initCodeAndDesp();
                baseResp.setData(detailid);
            }
        } catch (Exception e) {
            logger.error("insertUserInComeDetail userid={} is error:",userid,e);
        }
        return baseResp;
    }


    /**
     * 发送消息
     * @param userid  用户id
     * @param num     龙币数量
     * @param tyep    消息类型 2 - 提现到银行卡，支付宝  6 - 提现到钱包
     */
    private void sendUserInComeMessage(String userid,int num,String tyep){
        String remark = "";
        String title = "龙币提现";
        //结算中消息
        if ("2".equals(tyep)){
            remark = "您申请提现的"+ num + "个龙币，正在结算中，我们将以最快的速度为你处理。";
        }
        //结算失败消息
        if ("3".equals(tyep)){
            remark = "您申请提现的"+ num + "个龙币，结算失败龙币已经退还到您的收益余额中，给您造成的不变尽请谅解，详细信息清登陆龙杯官网进行查看。";
        }
        //结算成功消息
        if ("4".equals(tyep)){
            remark = "您申请提现的"+ num + "个龙币，结算成功，请您留意个人账户的交易信息。";
        }
        //转入钱包
        if ("6".equals(tyep)){
            remark = "您申请提现的"+ num + "个龙币，已转入您的钱包。";
        }
        userMsgMapper.insertSelective(createInviteUserMsg(userid,remark,title));
    }

    private UserMsg createInviteUserMsg(String  userid,String remark,String title){
        UserMsg userMsg = new UserMsg();
        userMsg.setFriendid(Long.parseLong(Constant.SQUARE_USER_ID));
        userMsg.setUserid(Long.parseLong(userid));
        //mtype 0 系统消息     1 对话消息   2:@我消息      用户中奖消息在@我      未中奖消息在通知消息
        userMsg.setMtype("0");
        userMsg.setMsgtype("64");
        //gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统
        //10：榜中  11 圈子中  12 教室中  13:教室批复作业
        userMsg.setGtype("12");
        userMsg.setIsdel("0");
        userMsg.setIsread("0");
        userMsg.setCreatetime(new Date());
        userMsg.setUpdatetime(new Date());
//        userMsg.setSnsid(rank.getRankid());
        userMsg.setRemark(remark);
        userMsg.setTitle(title);
        return userMsg;
    }


    private BaseResp userInComeToWallet(String userid,UserInComeOrder userInComeOrder){
        BaseResp baseResp = new BaseResp();
        if (null == userInComeOrder.getNum()){
            return baseResp;
        }
        try {
            userInComeOrder.setUiostatus(4);
            int res = userInComeOrderMapper.insertSelective(userInComeOrder);
            if (res > 0){
                baseResp = userMoneyDetailService.insertPublic
                        (Long.parseLong(userid),"15",Integer.parseInt(userInComeOrder.getNum()),0);
            }
            if (ResultUtil.isSuccess(baseResp)){
                UserInComeDetail userInComeDetail = new UserInComeDetail();
                userInComeDetail.setDetailid(userInComeOrder.getDetailid());
                //结算成功
                userInComeDetail.setDetailstatus("1");
                userInComeDetail.setUpdatetime(new Date());
                //更新明细状态
                userInComeDetailMapper.updateByPrimaryKeySelective(userInComeDetail);
            }
        } catch (Exception e) {
            logger.error("userInComeToWallet userid={} is error:",userid,e);
        }
        return baseResp;
    }


    private void initUserInComeInfo(UserInComeDetail userInComeDetail){
        if (null == userInComeDetail){
            return;
        }
        if ("0".equals(userInComeDetail.getBusinesstype())){
            Classroom classroom = classroomMapper.selectByPrimaryKey(userInComeDetail.getBusinesstid());
            userInComeDetail.setClassroom(classroom);
            AppUserMongoEntity appUserMongoEntity =
                    userMongoDao.getAppUser(String.valueOf(userInComeDetail.getOriginuserid()));
            userInComeDetail.setAppUserMongoEntity(appUserMongoEntity);
        }
    }

}

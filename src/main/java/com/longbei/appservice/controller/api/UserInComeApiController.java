package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.UserInCome;
import com.longbei.appservice.entity.UserInComeDetail;
import com.longbei.appservice.entity.UserInComeOrder;
import com.longbei.appservice.service.impl.UserInComeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 用户收益接口
 *
 * @author luye
 * @create 2017-08-14 下午4:52
 **/
@RestController
@RequestMapping("/api/userincome")
public class UserInComeApiController {


    private static Logger logger = LoggerFactory.getLogger(UserInComeApiController.class);

    @Autowired
    private UserInComeService userInComeService;


    /**
     * 获取用户收益
     * @param userid 用户id Y
     * @return
     */
    @RequestMapping(value = "info",method = RequestMethod.GET)
    public BaseResp<UserInCome> selectUserInCome(String userid){
        logger.info("selectUserInCome userid={}",userid);
        BaseResp<UserInCome> baseResp = new BaseResp<>();
        if (StringUtils.isBlank(userid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = userInComeService.selectUserInCome(userid);
        } catch (Exception e) {
            logger.error("selectUserInCome userid={} is error:",userid,e);
        }
        return baseResp;
    }


    /**
     * 获取用户收益信息明细列表（参数后面带Y的为必传，N为可选）
     * @param userid  用户id Y
     * @param itype   明细具体类型  0 教室学费，1 送礼物，2 提现  3 提现失败返还，4 退学费，5转入钱包 N
     * @param detailstatus 明细状态 0 - 结算中 1 - 成功 2 - 失败 N
     * @param detailtype 明细类型 0 - 收入 1 - 支出 Y
     * @param period 时间段  0 - 近1周 1 - 近1个月 2 - 近3个月 N
     * @param pageNo  分页 N
     * @param pageSize  分页  N
     * @return
     */
    @RequestMapping(value = "detaillist",method = RequestMethod.POST)
    public BaseResp<Page<UserInComeDetail>> selectUserInComeInfoList(String userid,
                                                          String itype,
                                                          String detailstatus,
                                                          String detailtype,
                                                          String period,
                                                          String pageNo,
                                                          String pageSize){
        logger.info("userid={},detailtype={}",userid,detailtype);
        BaseResp<Page<UserInComeDetail>> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(userid,detailtype)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if (StringUtils.isBlank(pageNo)){
            pageNo = "1";
        }
        if (StringUtils.isBlank(pageSize)){
            pageSize = Constant.DEFAULT_PAGE_SIZE;
        }
        try {
            UserInComeDetail userInComeDetail = new UserInComeDetail();
            userInComeDetail.setUserid(Long.parseLong(userid));
            userInComeDetail.setItype(itype);
            userInComeDetail.setDetailstatus(detailstatus);
            userInComeDetail.setDetailtype(detailtype);
            userInComeDetail.setCreatetime(dealWithPeriod(period));
            baseResp = userInComeService.selectUserInComeDetailList(userInComeDetail,
                    Integer.parseInt(pageNo),Integer.parseInt(pageSize),true);
        } catch (Exception e) {
            logger.error("selectUserInComeInfo userid={},detailtype={} is error:",userid,detailtype);
        }
        return baseResp;
    }


    /**
     * 获取结算列表
     * @param receiptUser  收款人
     * @param receiptNum   收款账号
     * @param nickname     昵称
     * @param uiostatus    结算状态 0 - 申请结算。1 - 运营处理同意 2 - 运营处理不同意 3 - 财务处理不同意 4 - 财务出来同意，完成处理
     * @param pageNo       分页
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "settlelist",method = RequestMethod.POST)
    public BaseResp<Page<UserInComeOrder>> selectUserIncomeOrderList(String receiptUser, String receiptNum,
                                                                     String nickname, String uiostatus,
                                                                     String pageNo, String pageSize) {
        BaseResp<Page<UserInComeOrder>> baseResp = new BaseResp<>();

        if (StringUtils.isBlank(uiostatus)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if (StringUtils.isBlank(pageNo)){
            pageNo = "1";
        }
        if (StringUtils.isBlank(pageSize)){
            pageSize = Constant.DEFAULT_PAGE_SIZE;
        }

        try {
            baseResp = userInComeService.selectUserIncomeOrderList(receiptUser,receiptNum,nickname,uiostatus,Integer.parseInt(pageNo),
                    Integer.parseInt(pageSize));
        } catch (Exception e) {
            logger.error("controller selectUserIncomeOrderList uiostatus={} is error:",uiostatus,e);
        }

        return baseResp;
    }

    /**
     * 获取自有教室收益列表
     * @param sourcetype  运营收益 0
     * @param monmey  龙币数量
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "selflist",method = RequestMethod.GET)
    public BaseResp<Page<UserInComeDetail>> selectUserInComeDetailBySoureType
            (String sourcetype,String monmey,String pageNo,String pageSize){
        BaseResp<Page<UserInComeDetail>> baseResp = new BaseResp<>();
        if (StringUtils.isBlank(sourcetype)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if (StringUtils.isBlank(pageNo)){
            pageNo = "1";
        }
        if (StringUtils.isBlank(pageSize)){
            pageSize = Constant.DEFAULT_PAGE_SIZE;
        }
        UserInComeDetail userInComeDetail = new UserInComeDetail();
        userInComeDetail.setCsourcetype(sourcetype);
        if(null != monmey && !"".equals(monmey)) {
            userInComeDetail.setNum(Integer.parseInt(monmey));
        }
        userInComeDetail.setDetailtype("0");
        userInComeDetail.setBusinesstype("0");
        try {
            baseResp = userInComeService.selectUserInComeDetailList(userInComeDetail,
                    Integer.parseInt(pageNo),Integer.parseInt(pageSize),false);
        } catch (Exception e) {
            logger.error("selectUserInComeDetailBySoureType sourcetype={} is error:",sourcetype,e);
        }
        return baseResp;
    }



    /**
     * 更新用户结算订单审核状态
     * @param uioid  结算单id
     * @param uiostatus  结算单状态
     * @param deeloption  结算意见
     * @return
     */
    @RequestMapping(value = "updateorder",method = RequestMethod.POST)
    public BaseResp updateUserIncomeOrderStatus(String uioid,String uiostatus,String deeloption){
        BaseResp baseResp = new BaseResp();
        if (StringUtils.hasBlankParams(uioid,uiostatus)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = userInComeService.updateUserIncomeOrderStatus(uioid,uiostatus,deeloption);
        } catch (Exception e) {
            logger.error("controller updateUserIncomeOrderStatus uioid={} uiostatus={} is error:",uioid,uiostatus,e);
        }
        return baseResp;
    }





    /**
     * 获取收益明细详细信息
     * @param detailid  明细id  Y
     * @param detailtype  明细类型 0 - 收入 1 - 支出  Y
     * @return
     */
    @RequestMapping(value = "detail",method = RequestMethod.POST)
    public BaseResp<UserInComeDetail> selectUserInComeDetailInfo(String detailid,String detailtype){

        BaseResp<UserInComeDetail> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(detailid,detailtype)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = userInComeService.selectUserInComeDetail(detailid,detailtype);
        } catch (Exception e) {
            logger.error("selectUserInComeDetailInfo detailId={} detailType={} is error:",detailid,detailtype,e);
        }
        return baseResp;

    }


    /**
     * 收益结算
     * @param userid  用户id
     * @param num     龙币数量
     * @param receiptUser  收款人
     * @param receiptBank  收款银行
     * @param receiptNum   收款账号
     * @param origin 来源  2 提现  6 转入钱包
     * @return
     */
    @RequestMapping(value = "settle",method = RequestMethod.POST)
    public BaseResp settleUserInCome(String userid,String num,
                                   String receiptUser,String receiptBank,
                                   String receiptNum,String origin){
        BaseResp baseResp = new BaseResp();
        if (StringUtils.hasBlankParams(userid,origin,num)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if ("2".equals(origin)){
            if (StringUtils.hasBlankParams(receiptUser,receiptBank,receiptNum)){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }
        }
        try {
            baseResp = userInComeService.insertUserInComeOrder(userid,Integer.parseInt(num),
                    receiptUser,receiptBank,receiptNum,origin);
        } catch (Exception e) {
            logger.error("settleUserInCome userid={} num={} origin={} is error",userid,num,origin,e
            );
        }
        return baseResp;
    }




    private Date dealWithPeriod(String period){
        if (StringUtils.isBlank(period)){
            return null;
        }
        Date date = new Date();
        switch (period){
            case "0" :
                date = DateUtils.addDays(date,-7);
                break;
            case "1" :
                date = DateUtils.addDays(date,-30);
                break;
            case "2" :
                date = DateUtils.addDays(date,-90);
                break;
            default:
                break;
        }
        return date;
    }



}

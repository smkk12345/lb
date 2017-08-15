package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.UserInCome;
import com.longbei.appservice.entity.UserInComeDetail;
import com.longbei.appservice.service.impl.UserInComeService;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

/**
 * 用户收益接口
 *
 * @author luye
 * @create 2017-08-14 下午4:52
 **/
@RestController
@RequestMapping("userincome")
public class UserInComeController {


    private static Logger logger = LoggerFactory.getLogger(UserInComeController.class);

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
        } catch (NumberFormatException e) {
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

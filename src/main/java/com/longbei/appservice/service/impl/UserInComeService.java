package com.longbei.appservice.service.impl;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.*;

/**
 * 教室老师收益相关
 *
 * @author luye
 * @create 2017-08-14 上午9:44
 **/
public interface UserInComeService {


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
    BaseResp<String> updateUserInCome(String classroomId,
                                      String userId,
                                      String originUserId,
                                      String origin,
                                      String type,
                                      int num,
                                      String detailremarker);


    /**
     * 收益结算
     * @param userid  用户id
     * @param num     龙币数量
     * @param receiptUser  收款人
     * @param receiptBank  收款银行
     * @param receiptNum   收款账号
     * @param origin 来源  0 教室学费，1 送礼物，2 提现 3 提现失败 4提现成功，5 退学费，6转入钱包
     * @return
     */
    BaseResp insertUserInComeOrder(String userid,int num,
                                   String receiptUser,String receiptBank,
                                   String receiptNum,String origin);




    /**
     * 获取教师收益
     * @param userid  教师id
     * @return
     */
    BaseResp<UserInCome> selectUserInCome(String userid);


    /**
     * 获取收益明细详细信息
     * @param detailId  明细id
     * @param detailType  明细类型 0 - 收入 1 - 支出
     * @return
     */
    BaseResp<UserInComeDetail> selectUserInComeDetail(String detailId,String detailType);


    /**
     * 获取教师收益明细列表（分页）
     * @param userInComeDetail 检索条件 （支持明细类型，明细状态，明细时间）
     * @param istotalinfo 是否需要获取用户收益数额
     * @param pageNo
     * @param pageSize
     * @return
     */
    BaseResp<Page<UserInComeDetail>> selectUserInComeDetailList(UserInComeDetail userInComeDetail,
                                                                Integer pageNo,
                                                                Integer pageSize,
                                                                boolean istotalinfo);

    /**
     * 获取用户提现申请列表
     * @param receiptUser  收款人
     * @param receiptNum   收款账号
     * @param nickname     用户昵称
     * @param uiostatus    结算单状态 0 - 申请结算。1 - 运营处理同意
     *                     2 - 运营处理不同意 3 - 财务处理不同意
     *                     4 - 财务出来同意，完成处理  -1 - 已结算
     * @param pageNo       分页
     * @param pagesize
     * @return
     */
    BaseResp<Page<UserInComeOrder>> selectUserIncomeOrderList(String receiptUser,
                                                              String receiptNum,
                                                              String nickname,
                                                              String uiostatus,
                                                              Integer pageNo,
                                                              Integer pagesize);


    /**
     * 更新用户结算订单审核状态
     * @param uioid  结算单id
     * @param uiostatus  结算单状态
     * @param deeloption  结算意见
     * @return
     */
    BaseResp updateUserIncomeOrderStatus(String uioid,String uiostatus,String deeloption);


}

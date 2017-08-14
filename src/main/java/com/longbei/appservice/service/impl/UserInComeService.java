package com.longbei.appservice.service.impl;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.Classroom;
import com.longbei.appservice.entity.UserInComeDetail;
import com.longbei.appservice.entity.UserIncome;
import com.longbei.appservice.entity.UserInfo;

/**
 * 教室老师收益相关
 *
 * @author luye
 * @create 2017-08-14 上午9:44
 **/
public interface UserInComeService {


    /**
     * 添加教室收入
     * @param classroom  教室实体
     * @param originUserId 来源用户id
     * @param userInfo   教师实体
     * @param origin     来源  0 教室学费，1 送礼物，2 提现 3 提现失败返还 4提现成功，5 退学费，6转入钱包
     * @param num
     * @return
     */
    BaseResp<String> updateUserInCome(Classroom classroom, UserInfo userInfo,String originUserId,String origin,int num);






    /**
     * 获取教师收益
     * @param userid  教师id
     * @return
     */
    BaseResp<UserIncome> selectUserInCome(String userid);


    /**
     * 获取教师收益明细列表（分页）
     * @param userInComeDetail 检索条件 （支持明细类型，明细状态，明细时间）
     * @param pageNo
     * @param pageSize
     * @return
     */
    BaseResp<Page<UserInComeDetail>> selectUserInComeDetailList(UserInComeDetail userInComeDetail,Integer pageNo,Integer pageSize);


    /**
     * 查看用户收益明细
     * @param detailId
     * @return
     */
    BaseResp<UserInComeDetail> selectUserIncomeDetail(String detailId);


}

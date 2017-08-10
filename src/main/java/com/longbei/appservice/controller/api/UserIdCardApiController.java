package com.longbei.appservice.controller.api;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.UserIdcard;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.service.UserIdcardService;
import com.longbei.appservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 用户实名认证
 *
 * @author luye
 * @create 2017-04-14 下午2:32
 **/
@RestController
@RequestMapping(value = "api/useridcard")
public class UserIdCardApiController {

    private static Logger logger = LoggerFactory.getLogger(UserIdCardApiController.class);

    @Autowired
    private UserIdcardService userIdcardService;
    @Autowired
    private UserService userService;


    /**
     * 获取用户实名认证列表
     * @param userIdcard
     * @param pageno
     * @param pagesize
     * @return
     * @author luye
     */
    @RequestMapping(value = "list")
    public BaseResp<Page<UserIdcard>> selectUserIdCardListPage(@RequestBody UserIdcard userIdcard,
                                                               String pageno,String pagesize){
        logger.info("selectUserIdCardList for adminservice and userIdcard={},pageno={},pagesize={}", JSON.toJSON(userIdcard),pageno,pagesize);
        if (StringUtils.isBlank(pageno)){
            pageno = "1";
        }
        if (StringUtils.isBlank(pagesize)){
            pagesize = Constant.DEFAULT_PAGE_SIZE;
        }
        BaseResp<Page<UserIdcard>> baseResp = new BaseResp<>();

        try {
            baseResp = userIdcardService.selectUserIdCardListPage(userIdcard,Integer.parseInt(pageno),
                    Integer.parseInt(pagesize));
        } catch (Exception e) {
            logger.error("select user idcard list  is error:",e);
        }

        return baseResp;

    }

    /**
     * 获取用户实名认证信息列表数量
     * @param userIdcard
     * @return
     */
    @RequestMapping(value = {"selectUserIdCardListNum"})
    public BaseResp<Object> selectRankAcceptAwardListNum(@RequestBody UserIdcard userIdcard){
        logger.info("selectUserIdCardListNum for adminservice and userIdcard ={}", JSON.toJSONString(userIdcard));
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try {
            baseResp = userIdcardService.selectUserIdCardListNum(userIdcard);
        } catch (Exception e) {
            logger.error("selectUserIdCardListNum for adminservice and userIdcard ={}", JSON.toJSONString(userIdcard), e);
        }
        return  baseResp;
    }

    /**
     * 获取用户认证详情
     * @param userid
     * @return
     * @author luye
     */
    @RequestMapping(value = "info")
    public BaseResp<UserIdcard> getUserIdCardInfo(String userid){
        logger.info("userid={}", userid);
        BaseResp<UserIdcard> baseResp = new BaseResp<>();
        if (StringUtils.isBlank(userid)){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = userIdcardService.userSafety(Long.parseLong(userid));
        } catch (Exception e) {
            logger.error("get user id card info is error:",e);
        }
        return baseResp;
    }

    /**
     * 添加审核结果
     * @return
     */
    @RequestMapping(value = "checksubmit")
    public BaseResp<Object> submitCheck(@RequestBody UserIdcard userIdcard){
        logger.info("userIdcard={}", JSON.toJSON(userIdcard));
        BaseResp<Object> baseResp = new BaseResp<>();
        if (null == userIdcard ){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp  = userIdcardService.update(userIdcard);
        } catch (Exception e) {
            logger.error("submit useridcard check is error:",e);
        }
        return baseResp;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/applyIdCardValidate", method = RequestMethod.POST)
    @ResponseBody
    public BaseResp<Object> applyIdCardValidate(@RequestParam("userid") String userid,
                                                @RequestParam("realname") String realname, @RequestParam("idcard") String idcard,
                                                @RequestParam("idcardimage") String idcardimage) {
        logger.info("userid={},realname={},idcard={},idcardimage={}", userid, realname,idcard,idcardimage);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(userid, realname, idcard, idcardimage)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            // 修改userInfo表真实姓名
            UserInfo userInfo = new UserInfo();
            userInfo.setRealname(realname);
            userInfo.setUserid(Long.parseLong(userid));
            userService.updateUserInfo(userInfo);

            UserIdcard record = new UserIdcard();
            record.setIdcard(idcard);
            record.setIdcardimage(idcardimage);
            //是否验证了身份证号码 0  为验证 1  验证通过 2 验证不通过
            record.setValidateidcard("0");
            record.setUserid(Long.parseLong(userid));
            record.setApplydate(new Date());
            record.setRealname(realname);
            // 添加身份证验证 先判断是否验证过，已验证修改
            baseResp = userIdcardService.insert(record);
        } catch (Exception e) {
            logger.error("applyIdCardValidate userid={},msg={}", userid, e);
        }
        return baseResp;
    }


}

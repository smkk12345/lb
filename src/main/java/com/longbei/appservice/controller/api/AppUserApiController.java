package com.longbei.appservice.controller.api;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.*;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户详情接口
 * Created by yinxc on 2017/3/13.
 */
@RestController
@RequestMapping(value = "api/user")
public class AppUserApiController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserLevelService userLevelService;
    @Autowired
    private UserRelationService userRelationService;
    @Autowired
    private SysSensitiveService sysSensitiveService;
    @Autowired
    private UserSpecialcaseService userSpecialcaseService;
    @Autowired
    private UserPlDetailService userPlDetailService;
    @Autowired
    private UserMoneyDetailService userMoneyDetailService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RankAcceptAwardService rankAcceptAwardService;
    @Autowired
    private RankService rankService;
    @Autowired
    private UserIdcardService userIdcardService;
    @Autowired
    private SysProtectnamesService sysProtectnamesService;

    private static Logger logger = LoggerFactory.getLogger(AppUserApiController.class);






    @RequestMapping(value = "login",method = RequestMethod.POST)
    public BaseResp<UserInfo> login(String username,String password){
        logger.info("username={},password={}",username,password);
        BaseResp<UserInfo> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(username, password)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = userService.login(username,password);
        } catch (Exception e) {
            logger.error("pc login is error:",e);
        }
        return baseResp;
    }


    /**
	 * @author yinxc
	 * 获取用户信息---屏蔽私密的字段token
	 * 2017年3月14日
	 * @param userid
	 */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "userDetail")
    public BaseResp<UserInfo> userDetail(String userid){
        logger.info("userid={}",userid);
        BaseResp<UserInfo> baseResp = new BaseResp<UserInfo>();
        if (StringUtils.hasBlankParams(userid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = userService.selectInfoMore(Long.parseLong(userid),0);
        } catch (Exception e) {
            logger.error("userDetail userid = {}", userid, e);
        }
        return baseResp;
    }

    /**
     * 仅查询用户基本信息
     * @param userid
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "selectJustInfo")
    public BaseResp<UserInfo> selectJustInfo(String userid){
        logger.info("userid={}",userid);
        BaseResp<UserInfo> baseResp = new BaseResp<UserInfo>();
        if (StringUtils.hasBlankParams(userid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            BaseResp<Object> baseRes = userService.selectByUserid(Long.parseLong(userid));

            UserInfo userInfo = (UserInfo)baseRes.getData();
            //十项全能
            BaseResp<Object> baseResp1 = userPlDetailService.selectUserPerfectListByUserId(Long.parseLong(userid),0,15);
            //充值总龙币数
            BaseResp<Integer> baseResp2 = userMoneyDetailService.selectMoneyNum(Long.parseLong(userid),"0");
            //总订单数
            BaseResp<Integer> baseResp3 = orderService.selectTotalOrderNum(userid);
            //订单总进步币
            BaseResp<Integer> baseResp4 = orderService.selectTotalOrderCoinNum(userid);
            //总获奖数
            int awardnum = rankAcceptAwardService.userRankAcceptAwardCount(Long.parseLong(userid), "0");
            //参与的榜总数
            BaseResp<Integer> baseResp5 = rankService.selectownRankCount(Long.parseLong(userid),1);
            //身份证号
            UserIdcard userIdCard = userIdcardService.selectByUserid(userid);
            //是否为榜单达人
            RankMembers rankMembers = new RankMembers();
            rankMembers.setUserid(Long.parseLong(userid));
            rankMembers.setStatus(1);
            rankMembers.setIsfashionman("1");
            BaseResp<List<RankMembers>> baseResp6 = rankService.selectRankMemberList(rankMembers);
            //查询好友数量
            Integer friendCount = userRelationService.selectFriendsCount(Long.parseLong(userid));

            Map<String,Object> map = new HashedMap();
            if (ResultUtil.isSuccess(baseResp1)){
                List<UserPlDetail> plList = (List<UserPlDetail>) baseResp1.getData();
                map.put("pl",plList);
            }
            map.put("buymoney",baseResp2.getData());
            map.put("totalorder",baseResp3.getData());
            map.put("totalordercoin",baseResp4.getData());
            map.put("awardnum",awardnum);
            map.put("totalrank",baseResp5.getData());
            if(null != userIdCard) {
                map.put("idcard", userIdCard.getIdcard());
            }else {
                map.put("idcard", null);
            }
            if(baseResp6.getData().size()>0){
                map.put("isRankFashion","1");
            }else {
                map.put("isRankFashion","0");
            }
            map.put("friendCount", friendCount);

            baseResp.setData(userInfo);
            baseResp.setExpandData(map);
            baseResp.initCodeAndDesp();
        } catch (Exception e) {
            logger.error("userDetail userid = {}", userid, e);
        }
        return baseResp;
    }
    /**
	 * @author yinxc
	 * 获取用户等级详情信息---用户享受的折扣
	 * 2017年3月14日
	 * @param userid
	 */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "selectUserLevel")
    public BaseResp<UserLevel> selectUserLevel(String userid){
        logger.info("userid={}",userid);
        BaseResp<UserLevel> baseResp = new BaseResp<UserLevel>();
        if (StringUtils.hasBlankParams(userid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = userLevelService.selectByUserid(Long.parseLong(userid));
        } catch (Exception e) {
            logger.error("selectUserLevel userid = {}", userid, e);
        }
        return baseResp;
    }





    /**
     * http://ip:port/appservice/api/user/sms
     * 短信验证
     *  operateType 0 注册 1 修改密码 2 找回密码 3 第三方绑定手机号 4 安全验证
     * @param response
     * @return 正确返回 code 0 错误返回相应code 和 描述
     */
    @RequestMapping(value = "/sms")
    @ResponseBody
    public BaseResp<Object> sms(HttpServletRequest request, HttpServletResponse response) {
        String mobile = request.getParameter("mobile");
        String operateType = request.getParameter("operateType");
        operateType = operateType == null ? "0" : operateType;
        logger.info("sms and mobile={},operateType={}", mobile, operateType);
        if(StringUtils.isBlank(mobile)){
            return new BaseResp<>(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            return userService.sms(mobile, operateType);
        } catch (Exception e) {
            logger.error(" sms error and msg={}", e);
        }
        return new BaseResp<>();
    }

    /**
     * 通过userid获取用户昵称
     * @param userid
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/nickname")
    @ResponseBody
    public BaseResp<Map> getNickNameById(String userid){
        BaseResp<Map> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(userid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        BaseResp<Object> baseResp1 = userService.selectByUserid(Long.parseLong(userid));
        if (ResultUtil.isSuccess(baseResp1)){
            UserInfo userInfo = (UserInfo) baseResp1.getData();
            if (null != userInfo){
                baseResp.initCodeAndDesp();
                Map<String,String> map = new HashedMap();
                map.put("nickname",userInfo.getNickname());
                map.put("username",userInfo.getUsername());
                baseResp.setData(map);
            }
        }
        return baseResp;
    }

    /**
     * http://ip:port/appservice/api/user/registerbasic
     * h5注册
     * @param request
     * @param response
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/registerbasic")
    @ResponseBody
    public BaseResp<Object> registerbasic(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String inviteuserid = request.getParameter("inviteuserid");
        String randomCode = request.getParameter("randomCode");
        String nickname = request.getParameter("nickname");
        logger.info("registerbasic params username={},password={},inviteuserid={},randomCode={}",
                username, password,inviteuserid,randomCode);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(username, password, randomCode)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        baseResp = userService.checkSms(username, randomCode);
        if (baseResp.getCode() != Constant.STATUS_SYS_00) {
            return baseResp;
        }
        try {
            return userService.registerbasic(username, password, inviteuserid,null,null,null,nickname);
        } catch (Exception e) {
            logger.error("registerbasic error and msg = {}", e);
        }
        return baseResp;
    }


    /**
     * 获取用户列表
     * @param userInfo
     * @param validateidcard //是否验证了身份证号码 0是未提交信息 1是验证中 2验证通过 3验证不通过
     * @param order
     * @param ordersc
     * @param pageno
     * @param pagesize
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/userlist")
    @ResponseBody
    public BaseResp<Page<UserInfo>> selectUserList(@RequestBody UserInfo userInfo,String validateidcard, String order,
                                                   String ordersc, String pageno, String pagesize){
        logger.info("userInfo={},validateidcard={},order={},ordersc={},pageno={},pagesize={}", JSON.toJSONString(userInfo),validateidcard,order,ordersc,pageno,pagesize);
        BaseResp<Page<UserInfo>> baseResp = new BaseResp<>();
        if (StringUtils.isBlank(ordersc)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if (StringUtils.isBlank(pageno)){
            pageno = "1";
        }
        if (StringUtils.isBlank(pagesize)){
            pagesize = Constant.DEFAULT_PAGE_SIZE;
        }
        try {
            baseResp = userService.selectUserList(userInfo,validateidcard,order,ordersc,Integer.parseInt(pageno),Integer.parseInt(pagesize));
        } catch (Exception e) {
            logger.error("select user list for pc is error:",e);
        }
        return baseResp;

    }

    /**
     * 更新用户状态 达人，封号等
     * @param userInfo
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/updateuser")
    @ResponseBody
    public BaseResp<Object> updateUserStatus(@RequestBody UserInfo userInfo){
        logger.info("userInfo={}", JSON.toJSONString(userInfo));
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            baseResp = userService.updateUserStatus(userInfo);
        } catch (Exception e) {
            logger.error("update user status is error:",e);
        }
        return baseResp;
    }

    /**
     * @Description: 查询好友列表 通过好友id
     * @param @param userid
     * @param updateTime 上次同步的时间,如果是获取整个好友列表,则不需要传该参数或仅限于传0
     *                  如果传入updateTime ,请传入格式为 2017-03-16 10:00:00 的时间格式
     * @param @return
     * @auther smkk
     * @currentdate:2017年1月20日
     * @update by smkk on 2017-05-12  该方法暂时不用了
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "selectListByUserId")
    @ResponseBody
    public BaseResp<Object> selectListByUserId(String userid ,Integer startNum,Integer pageSize,String updateTime){
        logger.info("userid={},startNum={},pageSize={},updateTime={}",userid,startNum,pageSize,updateTime);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(userid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        if(startNum != null && startNum < 0){
            startNum = 0;
        }
        if(null == pageSize){
            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }

        Date updateDate = null;
        if(StringUtils.isNotEmpty(updateTime) && !"0".equals(updateTime)){
            try{
                updateDate = DateUtils.formatDate(updateTime,null);
            }catch (Exception e){
                logger.error("select friend list by userId userId:{} startNum:{} pageSize:{} updateTime;{}",userid,startNum,pageSize,updateTime);
                updateDate = null;
            }
        }
        try {
            baseResp = userRelationService.selectListByUserId(Long.parseLong(userid), startNum, pageSize,updateDate);
        } catch (Exception e) {
            logger.error("selectListByUserId userid = {}, startNum = {}, pageSize = {}", userid, startNum, pageSize, e);
        }
        return baseResp;
    }


    /**
     *
     * @param friendid 消息推送者id
     * @param userids 消息接受者id
     * @param businesstype
     * @param businessid
     * @param remark
     * @param title
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "sendmessage")
    @ResponseBody
    public BaseResp<Object> sendMessagesBatch(String friendid, String[] userids, String businesstype,
                                        String businessid, String remark,String title){
        logger.info("friendid={},userids={},businesstype={},businessid={},remark={},title={}",friendid,JSON.toJSONString(userids),businesstype,businessid,remark,title);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(userids)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = userService.sendMessagesBatch(friendid,userids,businesstype,businessid,remark,title);
            return baseResp;
        } catch (Exception e) {
            logger.error("sendMessage is error", e);
        }
        return baseResp;
    }

    /**
     * 更改用户龙币余额
     * @param userid
     * @param totalMoney
     * @param totalPrice  消耗的龙币数量
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "updateMoney")
    public BaseResp updateTotalmoneyByUserid(Long userid, Integer totalMoney, Integer totalPrice){
        logger.info("userid={},totalMoney={},totalPrice={}",userid,totalMoney,totalPrice);
        BaseResp baseResp = new BaseResp();
        try {
            baseResp =userService.updateTotalmoneyByUserid(userid,totalPrice);
        } catch (Exception e) {
            logger.error("update Totalmoney By Userid {} is error", userid, e);
        }
        return baseResp;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/selectSensitive")
    public BaseResp<SysSensitive> selectSensitive() {
        BaseResp<SysSensitive> baseResp = new BaseResp<>();
        try {
            baseResp = sysSensitiveService.selectSensitive();
            return baseResp;
        } catch (Exception e) {
            logger.error("selectSensitive for adminservice ",e);
        }
        return baseResp;
    }

    @RequestMapping(value = "/updateSensitiveWords")
    public BaseResp<Object> updateSensitiveWords(String words,String sensitiveId) {
        logger.info("updateSensitiveWords for adminservice and words={},sensitiveId={}", words,sensitiveId);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.isBlank(words)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = sysSensitiveService.updateSensitiveWords(words,sensitiveId);
        } catch (Exception e) {
            logger.error("updateSensitiveWords for adminservice and words={},sensitiveId={}", words,sensitiveId,e);

        }
        return baseResp;
    }

    /**
     * @Description: 批量发送短信
     * @param mobiles 手机号码列表
     * @param template 短信模版id
     * @auther IngaWu
     * @currentdate:2017年7月18日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "smsBatch")
    @ResponseBody
    public BaseResp<Object> smsBatch(@RequestBody List<String> mobiles,String template){
        logger.info("smsBatch and mobiles={},template={}",JSON.toJSONString(mobiles),template);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.isBlank(mobiles.toString())) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
                baseResp = userService.smsBatch(mobiles,template);
            return baseResp;
        } catch (Exception e) {
            logger.error("smsBatch and mobiles={},template={}",JSON.toJSONString(mobiles),template,e);
        }
        return baseResp;
    }

    @RequestMapping("isMoneyEnough")
    public BaseResp isMoneyEnough(int totalPrice,long userid){
        logger.info("isMoneyEnough  totalPrice={},userid={}", totalPrice,userid);
        BaseResp baseResp = new BaseResp<>();
        try {
            baseResp = userService.isMoneyEnough(totalPrice,userid);
        } catch (Exception e) {
            logger.error("isMoneyEnough totalPrice={} userid error:",totalPrice,userid,e);
        }
        return baseResp;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/selectUserSpecialcase")
    public BaseResp<UserSpecialcase> selectUserSpecialcase() {
        BaseResp<UserSpecialcase> baseResp = new BaseResp<>();
        try {
            baseResp = userSpecialcaseService.selectUserSpecialcase();
            return baseResp;
        } catch (Exception e) {
            logger.error("selectUserSpecialcase for adminservice ",e);
        }
        return baseResp;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/updateUserSpecialcase")
    public BaseResp<Object> updateUserSpecialcase(@RequestBody UserSpecialcase userSpecialcase) {
        logger.info("updateUserSpecialcases and userSpecialcase={}", JSON.toJSONString(userSpecialcase));
        BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(userSpecialcase.getNoRegisterLimit(),userSpecialcase.getNoSwitchLogin(),userSpecialcase.getCreateuid()+"")){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = userSpecialcaseService.updateUserSpecialcase(userSpecialcase);
        } catch (Exception e) {
            logger.error("updateUserSpecialcases for adminservice and userSpecialcase={}", JSON.toJSONString(userSpecialcase),e);
        }
        return baseResp;
    }

    /**
     * 查询受保护的昵称
     * @author IngaWu
     * @currentdate:2017年8月25日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/selectProtectnames")
    public BaseResp<SysProtectnames> selectProtectnames() {
        BaseResp<SysProtectnames> baseResp = new BaseResp<>();
        try {
            baseResp = sysProtectnamesService.selectProtectnames();
            return baseResp;
        } catch (Exception e) {
            logger.error("selectProtectnames for adminservice ",e);
        }
        return baseResp;
    }

    /**
     * 编辑受保护的昵称
     * @title updateProtectNames
     * @param  nicknames 昵称集合
     * @param  protectNamesId
     * @author IngaWu
     * @currentdate:2017年8月25日
     */
    @RequestMapping(value = "/updateProtectNames")
    public BaseResp<Object> updateProtectNames(String nicknames,String protectNamesId) {
        logger.info("updateProtectNames for adminservice and nicknames={},protectNamesId={}", nicknames,protectNamesId);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.isBlank(nicknames)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = sysProtectnamesService.updateProtectNames(nicknames);
        } catch (Exception e) {
            logger.error("updateProtectNames for adminservice and nicknames={},protectNamesId={}", nicknames,protectNamesId,e);

        }
        return baseResp;
    }
}

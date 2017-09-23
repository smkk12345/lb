package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.LiveGiftDetail;
import com.longbei.appservice.entity.UserFlowerDetail;
import com.longbei.appservice.service.LiveGiftService;
import com.longbei.appservice.service.UserFlowerDetailService;
import com.longbei.appservice.service.UserImpCoinDetailService;
import com.longbei.appservice.service.UserMoneyDetailService;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 钱相关
 *
 * @author luye
 * @create 2017-02-20 下午4:17
 **/
@RestController
@RequestMapping(value = "money")
public class MoneyController {


	@Autowired
	private UserImpCoinDetailService userImpCoinDetailService;
	@Autowired
	private UserMoneyDetailService userMoneyDetailService;
	@Autowired
	private UserFlowerDetailService userFlowerDetailService;
	@Autowired
    private LiveGiftService liveGiftService;
	
	private static Logger logger = LoggerFactory.getLogger(MoneyController.class);
	
	
	/**
    * @Title: http://ip:port/app_service/money/selectLiveGift
    * @Description: 我的礼物
    * @param @param userid
    * @param @param classroomid 教室id   可为null---查我的礼物列表
    * @auther yinxc
    * @currentdate:2017年9月5日
    */
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "selectLiveGift")
    public BaseResp<List<Map<String,String>>> selectLiveGift(String userid, String classroomid) {
		logger.info("selectLiveGift userid = {}, classroomid = {}",userid, classroomid);
		BaseResp<List<Map<String,String>>> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			baseResp = liveGiftService.selectGiftSumList(Long.parseLong(userid), classroomid);
        } catch (Exception e) {
            logger.error("selectLiveGift userid = {}, classroomid = {}", 
            		userid, classroomid, e);
        }
  		return baseResp;
    }
	
	/**
    * @Title: http://ip:port/app_service/money/selectLiveGiftDetail
    * @Description: 我的礼物明细
    * @param @param userid 当前登录者id
    * @param @param classroomid 教室id   可为null---查我的礼物明细
    * @param @param startNo   pageSize
    * @auther yinxc
    * @currentdate:2017年9月5日
    */
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "selectLiveGiftDetail")
    public BaseResp<List<LiveGiftDetail>> selectLiveGiftDetail(String userid, String classroomid, 
    		Integer startNo, Integer pageSize) {
		logger.info("selectOwnGiftList userid = {}, classroomid = {}, startNo = {}, pageSize = {}", 
            		userid, classroomid, startNo, pageSize);
		BaseResp<List<LiveGiftDetail>> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
		int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
		if(null != startNo){
			sNo = startNo.intValue();
		}
		if(null != pageSize){
			sSize = pageSize.intValue();
		}
  		try {
  			baseResp = liveGiftService.selectOwnGiftList(Long.parseLong(userid), classroomid, sNo, sSize);
        } catch (Exception e) {
            logger.error("selectOwnGiftList userid = {}, classroomid = {}, startNo = {}, pageSize = {}", 
            		userid, classroomid, startNo, pageSize, e);
        }
  		return baseResp;
    }
	
	
	/**
    * @Title: http://ip:port/app_service/money/selectGiftDetailList
    * @Description: 收到的单个礼物类型明细
    * @param @param userid 当前登录者id
    * @param @param classroomid 教室id   可为null---查收到的单个礼物类型明细
    * @param @param giftid 礼物类型id
    * @param @param startNo   pageSize
    * @auther yinxc
    * @currentdate:2017年9月5日
    */
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "selectGiftDetailList")
    public BaseResp<List<LiveGiftDetail>> selectGiftDetailList(String userid, String classroomid, String giftid, 
    		Integer startNo, Integer pageSize) {
		logger.info("selectGiftListByGiftid userid = {}, classroomid = {}, giftid = {}, startNo = {}, pageSize = {}", 
        		userid, classroomid, giftid, startNo, pageSize);
		BaseResp<List<LiveGiftDetail>> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, giftid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
		int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
		if(null != startNo){
			sNo = startNo.intValue();
		}
		if(null != pageSize){
			sSize = pageSize.intValue();
		}
  		try {
  			baseResp = liveGiftService.selectGiftListByGiftid(Long.parseLong(userid), classroomid, Long.parseLong(giftid), sNo, sSize);
        } catch (Exception e) {
            logger.error("selectGiftListByGiftid userid = {}, classroomid = {}, giftid = {}, startNo = {}, pageSize = {}", 
            		userid, classroomid, giftid, startNo, pageSize, e);
        }
  		return baseResp;
    }
	
	
	/**
    * @Title: http://ip:port/app_service/money/selectWallet
    * @Description: 我的钱包
    * @param @param userid
    * @auther yinxc
    * @currentdate:2017年2月25日
    */
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "selectWallet")
    public BaseResp<Object> selectWallet(@RequestParam("userid") String userid) {
		logger.info("userid={}",userid);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			baseResp = userImpCoinDetailService.selectWallet(Long.parseLong(userid));
        } catch (Exception e) {
            logger.error("selectWallet userid = {}, msg = {}", userid, e);
        }
  		return baseResp;
    }
	
	
	/**
    * @Title: http://ip:port/app_service/money/selectImpCoinDetail
    * @Description: 获取进步币明细
    * @param @param userid
    * @param @param startNum   pageSize
    * @auther yinxc
    * @currentdate:2017年2月25日
    */
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "selectImpCoinDetail")
    public BaseResp<Object> selectImpCoinDetail(String userid, Integer startNum, Integer pageSize) {
		logger.info("userid={},startNum={},pageSize={}",userid,startNum,pageSize);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
  			int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
  			if(null != startNum){
  				sNo = startNum.intValue();
  			}
  			if(null != pageSize){
  				sSize = pageSize.intValue();
  			}
  			baseResp = userImpCoinDetailService.selectListByUserid(Long.parseLong(userid), sNo, sSize);
        } catch (Exception e) {
            logger.error("selectImpCoinDetail userid = {}, startNum = {}, pageSize = {}", userid, startNum, pageSize, e);
        }
  		return baseResp;
    }
	
	
	
	
	
	/**
    * @Title: http://ip:port/app_service/money/selectMoneyDetail
    * @Description: 获取龙币明细
    * @param @param userid
    * @param @param startNo   pageSize
    * @auther yinxc
    * @currentdate:2017年2月27日
    */
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "selectMoneyDetail")
    public BaseResp<Object> selectMoneyDetail(String userid, Integer startNum, Integer pageSize) {
		logger.info("userid={},startNum={},pageSize={}",userid,startNum,pageSize);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
  			int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
  			if(null != startNum){
  				sNo = startNum.intValue();
  			}
  			if(null != pageSize){
  				sSize = pageSize.intValue();
  			}
  			baseResp = userMoneyDetailService.selectListByUserid(Long.parseLong(userid), sNo, sSize);
        } catch (Exception e) {
            logger.error("selectMoneyDetail userid = {}, startNo = {}, pageSize = {}", userid, startNum, pageSize, e);
        }
  		return baseResp;
    }
	
	
	/**
    * @Title: http://ip:port/app_service/money/selectFlowerDetail
    * @Description: 获取用户收到的鲜花明细
    * @param @param userid
    * @param @param friendid 当前登录者id
    * @param @param startNum   pageSize
    * @auther yinxc
    * @currentdate:2017年4月14日
    */
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "selectFlowerDetail")
    public BaseResp<List<UserFlowerDetail>> selectFlowerDetail(String userid, String friendid, Integer startNum, Integer pageSize) {
		logger.info("userid={},friendid={},startNum={},pageSize={}",userid,friendid,startNum,pageSize);
		BaseResp<List<UserFlowerDetail>> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, friendid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
  			int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
  			if(null != startNum){
  				sNo = startNum.intValue();
  			}
  			if(null != pageSize){
  				sSize = pageSize.intValue();
  			}
			//origin： 0:龙币兑换;  1:赠与---龙币兑换    2:进步币兑换    3:被赠与---龙币兑换
			//						4:赠与---进步币兑换    5:被赠与---进步币兑换
  			baseResp = userFlowerDetailService.selectListByUseridAndOrigin(Long.parseLong(userid), 
  					Long.parseLong(friendid), "3", sNo, sSize);
        } catch (Exception e) {
            logger.error("selectMoneyDetail userid = {}, startNum = {}, pageSize = {}", userid, startNum, pageSize, e);
        }
  		return baseResp;
    }
	


}

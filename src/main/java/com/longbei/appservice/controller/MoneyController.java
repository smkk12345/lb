package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.service.UserImpCoinDetailService;
import com.longbei.appservice.service.UserMoneyDetailService;

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
	
	private static Logger logger = LoggerFactory.getLogger(MoneyController.class);
	
	
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
    * @param @param startNo   pageSize
    * @auther yinxc
    * @currentdate:2017年2月25日
    */
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "selectImpCoinDetail")
    public BaseResp<Object> selectImpCoinDetail(String userid, Integer startNo, Integer pageSize) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
  			int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
  			if(null != startNo){
  				sNo = startNo.intValue();
  			}
  			if(null != pageSize){
  				sSize = pageSize.intValue();
  			}
  			baseResp = userImpCoinDetailService.selectListByUserid(Long.parseLong(userid), sNo, sSize);
        } catch (Exception e) {
            logger.error("selectImpCoinDetail userid = {}, startNo = {}, pageSize = {}", userid, startNo, pageSize, e);
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
    public BaseResp<Object> selectMoneyDetail(String userid, Integer startNo, Integer pageSize) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
  			int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
  			if(null != startNo){
  				sNo = startNo.intValue();
  			}
  			if(null != pageSize){
  				sSize = pageSize.intValue();
  			}
  			baseResp = userMoneyDetailService.selectListByUserid(Long.parseLong(userid), sNo, sSize);
        } catch (Exception e) {
            logger.error("selectMoneyDetail userid = {}, startNo = {}, pageSize = {}", userid, startNo, pageSize, e);
        }
  		return baseResp;
    }
	


}

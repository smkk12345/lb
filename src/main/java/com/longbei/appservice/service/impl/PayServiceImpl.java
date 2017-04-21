package com.longbei.appservice.service.impl;

import java.util.Map;

import com.longbei.appservice.service.api.productservice.IProductBasicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.service.PayService;
import com.longbei.appservice.service.UserMoneyDetailService;
import com.longbei.pay.weixin.res.ResponseHandler;

import net.sf.json.JSONArray;

@Service("payService")
public class PayServiceImpl implements PayService {
	
	@Autowired
	private UserMoneyDetailService userMoneyDetailService;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private IProductBasicService iProductBasicService;

	private static Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

	
	/**
	 * 微信支付
	 * @author yinxc
	 * @param @param orderid 订单编号
	 * @param @param userid 
	 * 2017年3月20日
	 */
	@Override
	public BaseResp<Object> wxPayMainPage(Long userid, String orderid) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			baseResp = iProductBasicService.wxPayMainPage(orderid);
		}catch (Exception e){
			logger.error("wxPayMainPage orderid = {}", orderid, e);
		}
		return baseResp;
	}

	
	
	/**
	 * 支付宝支付
	 * @author yinxc
	 * @param @param orderid 订单编号
	 * @param @param userid 
	 * 2017年3月20日
	 */
	@Override
	public BaseResp<Object> signature(Long userid, String orderid) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			baseResp = iProductBasicService.signature(userid, orderid);
		}catch (Exception e){
			logger.error("signature userid = {}, orderid = {}", userid, orderid, e);
		}
		return baseResp;
	}


	/**
	 * 支付宝支付回调
	 * @author yinxc
	 * @param @param orderType 2：购买龙币
	 * 2017年3月20日
	 */
	@Override
	public BaseResp<Object> verifyali(Long userid, String orderType, Map<String, String> resMap) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			Double total_fee = Double.parseDouble(resMap.get("total_fee"))/AppserviceConfig.yuantomoney;
			insertMoney(total_fee.intValue(), userid, Constant.USER_MONEY_BUY);
			baseResp = iProductBasicService.verifyali(orderType, resMap);
		}catch (Exception e){
			logger.error("verifyali userid = {}, orderType = {}, resMap = {}", 
					userid, orderType, JSONArray.fromObject(resMap).toString(), e);
		}
		return baseResp;
	}
	
	/**
	 * 微信支付回调
	 * @author yinxc
	 * @param @param orderType 2：购买龙币
	 * 2017年3月21日
	 */
	@Override
	public BaseResp<Object> verifywx(Long userid, String orderType, String price, ResponseHandler resHandler) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			//购买成功后，添加龙币----
			Double total_fee = Double.parseDouble(price)/AppserviceConfig.yuantomoney;
			insertMoney(total_fee.intValue(), userid, Constant.USER_MONEY_BUY);
			baseResp = iProductBasicService.verifywx(orderType, resHandler);
		}catch (Exception e){
			logger.error("verifywx userid = {}, orderType = {}, price = {}, resHandler.smap = {}", 
					userid, orderType, price, JSONArray.fromObject(resHandler.getSmap()).toString(), e);
		}
		return baseResp;
	}

	@Override
	public void testWx() {
		iProductBasicService.testWx();
	}

	/**
	 * @author yinxc
	 * 添加龙币明细
	 * 2017年3月21日
	 * @param baseResp 
	 * @param origin ： 来源   0:充值  购买     1：购买礼物(花,钻)  2:兑换商品时抵用进步币
	 * 					3：设榜单    4：赞助榜单    5：赞助教室 
	 */
	private void insertMoney(int buynum, Long userid, String origin){
		//修改用户龙币数量
		userInfoMapper.updateMoneyAndFlowerByUserid(userid, buynum, 0);
//		Map<String, Object> expandData = baseResp.getExpandData();
		//购买数量
//		String buynums = (String) expandData.get("buynum");
//		if(!StringUtils.isBlank(buynums)){
			userMoneyDetailService.insertPublic(userid, origin, buynum, 0);
			//修改userinfo信息
//			userInfoMapper.updateMoneyAndFlowerByUserid(userid, Integer.parseInt(buynums), 0);
//		}
	}

}

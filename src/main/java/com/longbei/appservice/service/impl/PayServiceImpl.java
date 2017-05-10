package com.longbei.appservice.service.impl;

import java.util.Map;

import com.longbei.appservice.service.api.productservice.IProductBasicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.entity.ProductOrders;
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
	public String verifyali(Long userid, String orderType, Map<String, String> resMap) {
		try{
			BaseResp<ProductOrders> resp = iProductBasicService.getOrder(resMap.get("out_trade_no"));
			if(ResultUtil.isSuccess(resp)){
				ProductOrders productOrders = resp.getData();
				if(null != productOrders){
					if (!"0".equals(productOrders.getOrderstatus())) {
						return "SUCCESS";
					}else{
						BaseResp<Object> baseResp = iProductBasicService.verifyali(orderType, resMap);
						if(ResultUtil.isSuccess(baseResp)){
							Double total_fee = Double.parseDouble(resMap.get("total_fee"))/AppserviceConfig.yuantomoney;
							logger.info("verifyali total_fee = {}", total_fee);
							logger.info("verifyali total_fee.intValue() = {}", total_fee.intValue());
							//添加龙币
							insertMoney(total_fee.intValue(), userid, Constant.USER_MONEY_BUY);
							return "SUCCESS";
						}
					}
				}else{
					logger.info(resMap.get("out_trade_no") + "--支付失败，不存在的订单");
					return "fail";
				}
			}
		}catch (Exception e){
			logger.error("verifyali userid = {}, orderType = {}, resMap = {}", 
					userid, orderType, JSONArray.fromObject(resMap).toString(), e);
		}
		return "fail";
	}
	
	/**
	 * 微信支付回调
	 * @author yinxc
	 * @param @param orderType 2：购买龙币
	 * 2017年3月21日
	 */
	@Override
	public String verifywx(Long userid, String orderType, ResponseHandler resHandler) {
		try{
			String out_trade_no = resHandler.getSmap().get("out_trade_no").toString();
			logger.info("verifywx out_trade_no = {}", out_trade_no);
			BaseResp<ProductOrders> resp = iProductBasicService.getOrder(out_trade_no);
			if(ResultUtil.isSuccess(resp)){
				ProductOrders productOrders = resp.getData();
				if(null != productOrders){
					if (!"0".equals(productOrders.getOrderstatus())) {
						return "SUCCESS";
					}else{
						BaseResp<Object> baseResp = iProductBasicService.verifywx(out_trade_no);
						if(ResultUtil.isSuccess(baseResp)){
							//购买成功后，添加龙币----
							Double price = Double.parseDouble(productOrders.getPrice())/100;
							logger.info("verifywx price = {}", price);
							Double total_fee = price/AppserviceConfig.yuantomoney;
							logger.info("verifywx total_fee = {}", total_fee);
							insertMoney(total_fee.intValue(), userid, Constant.USER_MONEY_BUY);
							return "SUCCESS";
						}
					}
				}else{
					logger.info(out_trade_no + "--支付失败，不存在的订单");
					return "fail";
				}
			}
			
		}catch (Exception e){
			logger.error("verifywx userid = {}, orderType = {}, resHandler.smap = {}", 
					userid, orderType, JSONArray.fromObject(resHandler.getSmap()).toString(), e);
		}
		return "fail";
	}

	@Override
	public void testWx() {
		iProductBasicService.testWx();
	}

	/**
	 * @author yinxc
	 * 添加龙币明细
	 * 2017年3月21日
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

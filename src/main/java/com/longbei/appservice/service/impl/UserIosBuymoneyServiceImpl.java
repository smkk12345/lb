package com.longbei.appservice.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.longbei.appservice.common.persistence.CustomizedPropertyConfigurer;
import com.longbei.appservice.common.utils.*;
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.entity.ProductOrders;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.service.UserBehaviourService;
import com.longbei.appservice.service.UserMoneyDetailService;
import com.longbei.appservice.service.api.productservice.IProductBasicService;
import com.longbei.pay.util.UtilDate;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.UserIosBuymoneyMapper;
import com.longbei.appservice.entity.UserIosBuymoney;
import com.longbei.appservice.service.UserIosBuymoneyService;

@Service
public class UserIosBuymoneyServiceImpl implements UserIosBuymoneyService {

	@Autowired
    private UserIosBuymoneyMapper userIosBuymoneyMapper;
	@Autowired
	private UserBehaviourService userBehaviourService;
	@Autowired
	private UserMoneyDetailService userMoneyDetailService;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private IProductBasicService iProductBasicService;

    private static Logger logger = LoggerFactory.getLogger(UserIosBuymoneyServiceImpl.class);
	
	@Override
	public BaseResp<List<UserIosBuymoney>> selectMoneyAllList() {
		BaseResp<List<UserIosBuymoney>> baseResp = new BaseResp<>();
        try{
        	List<UserIosBuymoney> list = userIosBuymoneyMapper.selectMoneyAllList();
        	if(null != list && list.size()>0){
        		baseResp.setData(list);
        	}else{
        		baseResp.setData(new ArrayList<UserIosBuymoney>());
        	}
        	baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch (Exception e){
            logger.error("selectMoneyAllList ", e);
        }
        return baseResp;
	}

	@Override
	public BaseResp<Map<String, String>> buyIOSMoney(long userid, String productid, String payloadData) {
		BaseResp<Map<String,String>> baseResp = new BaseResp<>();
		String param = "{\"receipt-data\":\"" + payloadData + "\"}";
		String paramStr = DecodesUtils.getFromBase64(payloadData);
		logger.info("buyIOSFlower userid={},productid={}", userid, productid);
		Map<String, String> map = new HashMap<String, String>();
		if (!validateIOSBUY(param, map)) {
			return baseResp;
		}
		UserIosBuymoney userIosBuymoney = null;
		if (map.get("paytype").equals(Constant.PAY_TYPE_05)) {
			String product_id = (String) map.get("product_id");
			userIosBuymoney = userIosBuymoneyMapper.selectByProductid(product_id);
		} else {
			userIosBuymoney = userIosBuymoneyMapper.selectByProductid(productid);
		}
		int number = userIosBuymoney.getMoney() + userIosBuymoney.getAppmoney();
		map.put("addmoney", number+"");
		map.put("price",userIosBuymoney.getPrice()+"");
		try {
			UserInfo userInfo = userInfoMapper.selectInfoMore(userid);

			//人民币兑换龙币比例       
  		    int yuantomoney = AppserviceConfig.yuantomoney;
  		    //price 获取真实价格    已分为单位
  		    int minute = userIosBuymoney.getMoney()/yuantomoney*100;
  		    String price = minute + "";
  		    
			BaseResp<ProductOrders> baseResp1 = iProductBasicService.buyMoney(userid, number,userInfo.getUsername(),
					map.get("paytype"), map.get("transaction_id"), price);
			if(ResultUtil.isSuccess(baseResp1)){
				BaseResp<Object> baseResp2 = userMoneyDetailService.insertPublic(userid,"0",number,0);
				if(ResultUtil.isSuccess(baseResp2)){
					return baseResp.initCodeAndDesp();
				}else
					return baseResp;
			}
		} catch (Exception e) {
			logger.error("buyIOSFlower error msg={}", e);
			return baseResp;
		}
		return baseResp;
	}


	private boolean validateIOSBUY(String param, Map<String, String> map) {
		boolean result = false;

//		String urlStr = CustomizedPropertyConfigurer.getContextProperty("IOS_BUYFLOWER_PRO");
		String urlStr = AppserviceConfig.ios_buyflower_pro;
		byte bArr[] = param.getBytes();
		JSONObject js = null;
		try{
			String urlReturn = HttpUtils.postRequestForByte(urlStr, bArr);
			js = JSONObject.fromObject(urlReturn);
			logger.info("IOSBUYFLOWERPRO return {}",js.toString());
		}catch (Exception e){
			logger.error("error for http ",e);
		}

		try{
			if (null != js) {
				String status = js.getString("status");
				String bundle_id = "";
				if (!Constant.IOS_DEV_BUYFLOWER.equals(status)) {
					result = validateIOSBUYPro(param,map,js);
				}else {
//					String urlStrTest = CustomizedPropertyConfigurer.getContextProperty("IOS_BUYFLOWER");
					String urlStrTest = AppserviceConfig.ios_buyflower;
					String urlReturnTest = HttpUtils.postRequestForByte(urlStrTest, bArr);
					JSONObject jsTest = JSONObject.fromObject(urlReturnTest);
					logger.info("testIOSBuyFlower return json={}",jsTest.toString());
					result = validateIOSTest(param,map,jsTest);
				}
			} else {
				result = false;
			}
		}
		catch (Exception e){
			logger.error("validateIOSBUY",e);
		}
		return result;
	}
	/**
	 * IOS 处理测试环境买花
	 * @param param
	 * @param map
	 * @param jsTest
	 * @return
	 */
	private boolean validateIOSTest(String param, Map<String, String> map,JSONObject jsTest){
		boolean result = true;
		String status = jsTest.getString("status");
		if(!status.equals("0")){
			logger.info("buyIOSFlower resultData.toString()= " + jsTest.toString());
			result = false;
			return result;
		}
		JSONObject jsonObject = jsTest.getJSONObject("receipt");
		String bundle_id = jsonObject.getString("bundle_id");
		if(!StringUtils.isBlank(bundle_id)){
			if(!bundle_id.equals("longbei.org.DragonCup"))
				return false;
		}
		map.put("paytype", "3");
		map.put("transaction_id", "");
		return result;
	}

	/**
	 * IOS 处理生产环境买花
	 * @param param
	 * @param map
	 * @param js
	 * @return
	 */
	private boolean validateIOSBUYPro(String param, Map<String, String> map,JSONObject js){
		boolean result = false;
		String status = js.getString("status");
		if(!status.equals("0")){
			logger.info("buyIOSFlower resultData.toString()= " + js.toString());
			result = false;
			return result;
		}
		JSONObject jsonObject = js.getJSONObject("receipt");
		String bundle_id = jsonObject.getString("bundle_id");
		if(!StringUtils.isBlank(bundle_id)){
			if(!bundle_id.equals("longbei.org.DragonCup")){
				logger.info("buyIOSFlower resultData.toString()= " + js.toString());
				result = false;
				return result;
			}
		}
		JSONArray innapp = jsonObject.getJSONArray("in_app");
		if(null == innapp||innapp.size()==0){
			result = false;
		}else{
			JSONObject inappJs = JSONObject.fromObject(innapp.get(0));
			String transaction_id = inappJs.getString("transaction_id");
			String product_id = inappJs.getString("product_id");
			map.put("product_id",product_id);
			//查询数据库中是否存在  不存在 成功
//			Orders oldOrder = orderService.getByOrderSign(transaction_id);
			if (null != null) {
				map.put("status", Constant.STATUS_SYS_00+"");
				map.put("rtnInfo", Constant.RTNINFO_SYS_00);
				map.put("transaction_id",transaction_id);
				return false;
			}else{
				map.put("transaction_id",transaction_id);
				result = true;
			}
		}
		map.put("paytype", "4");
		return result;
	}

}

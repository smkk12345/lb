package com.longbei.appservice.common.expand;

import com.longbei.appservice.common.persistence.CustomizedPropertyConfigurer;
import com.longbei.appservice.config.AliServiceConfig;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class AlidayuSmsUtils {
	private static String aliUrl;
	private static String appKey;
	private static String appSecret;
	private static String smsType;
	private static String signName;
	private static String templateCode;
	
//	private static void initFromPropertiesFile() {
//		if(!StringUtils.isBlank(aliUrl)&&!StringUtils.isBlank(appKey)
//				&&!StringUtils.isBlank(appSecret)&&!StringUtils.isBlank(smsType)){
//			return;
//		}		
//		aliUrl = CustomizedPropertyConfigurer.getContextProperty("aliUrl");
//		appKey = CustomizedPropertyConfigurer.getContextProperty("appKey");
//		appSecret = CustomizedPropertyConfigurer.getContextProperty("appSecret");
//		smsType = CustomizedPropertyConfigurer.getContextProperty("smsType");
//		signName = CustomizedPropertyConfigurer.getContextProperty("signName");
//		templateCode = CustomizedPropertyConfigurer.getContextProperty("templateCode");
//		
//		if (StringUtils.isBlank(aliUrl) || StringUtils.isBlank(appKey)){
//			return; // Context not initialized
//		}
//	}
	
	public static String sendMsgValidate(String mobile, String validateCode,String operateName) {
//		initFromPropertiesFile();
		JSONObject jObj = new JSONObject();
		jObj.put("code", validateCode);
		jObj.put("product", operateName);
		TaobaoClient client = new DefaultTaobaoClient(AliServiceConfig.aliUrl, AliServiceConfig.appKey, AliServiceConfig.appSecret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("123456");
		req.setSmsType(AliServiceConfig.smsType);
		req.setSmsFreeSignName(AliServiceConfig.signName);
		req.setSmsParamString(jObj.toString());
		req.setRecNum(mobile);
		req.setSmsTemplateCode(AliServiceConfig.templateCode);
		try {
			AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
			if(!StringUtils.isBlank(rsp.getErrorCode())){
				rsp = client.execute(req);
				if(!StringUtils.isBlank(rsp.getErrorCode())){
					rsp = client.execute(req);
				}
			}
			if(!StringUtils.isBlank(rsp.getErrorCode())){
				return rsp.getErrorCode();
			}
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}

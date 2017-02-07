package com.longbei.appservice.common.expand;

import org.apache.commons.lang3.StringUtils;

import com.longbei.appservice.config.AliServiceConfig;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JPush {
	public static final String REGID = "1104a89792aaa3447e4";
	public static final String ALERT = "警告2";
	public static final String TITLE = "标题2";
	public static final String CONTENT = "测试消息文本2";
	public static JPushClient jpushClient = null;
	private static final Logger logger = LoggerFactory.getLogger(JPush.class);
	
	public static String apiKey = "";
	public static String masterKey = ""; 
	public static boolean isproduction = false;
	
	public static void main(String[] args) {
//		messagePush(Constant.JPUSH_API_KEY, Constant.JPUSH_MASTER_KEY, REGID, ALERT, TITLE, CONTENT);
//		messagePush("a436cf75a3cb2debcdbc1294", "588062101de0b620b681fccc", "171976fa8a83a0a2c31", "发送aaaaa", "", "");
	}

	private static void initFromPropertiesFile() {
		if(!StringUtils.isBlank(apiKey)&&!StringUtils.isBlank(masterKey)){
			return;
		}
		apiKey = AliServiceConfig.jpush_api_key;
		masterKey = AliServiceConfig.jpush_master_key;
		String production = AliServiceConfig.isproduction;
		//false:开发环境   ture:生产环境
		if(!StringUtils.isBlank(production) && "true".equals(production)){
			isproduction = true;
		}

		if (StringUtils.isBlank(apiKey) || StringUtils.isBlank(masterKey)){
//			log.error(MessageTemplate.print(MessageTemplate.FILE_ACCESS_MSG, new String[] { "config.properties" }));
			return; // Context not initialized
		}
	}
	
	public static void messagePushAll(String alias, String alert, String title, String content) {
		initFromPropertiesFile();
		jpushClient = new JPushClient(masterKey, apiKey);
		PushPayload payload = push_android_and_ios_alert(alias, alert, title, content);
		try {
			jpushClient.sendPush(payload);
		} catch (APIConnectionException e) {
			logger.debug("连接错误，请重试：{}",e);
		} catch (APIRequestException e) {
			logger.debug("极光推送服务器故障：{}",e);
		}
	}

	public static void messagePush(String regid, String alert, String title, String content) {
		jpushClient = new JPushClient(masterKey, apiKey);
		PushPayload payload = push_android_iso_alert_with_title(regid, alert, title, content);
		try {
			jpushClient.sendPush(payload);
		} catch (APIConnectionException e) {
			logger.debug("连接错误，请重试：{}",e);
		} catch (APIRequestException e) {
			logger.debug("极光推送服务器故障：{}",e);
		}
	}
	
	public static void messageIosPush(String regid, String alert, String title, String content) {
		jpushClient = new JPushClient(masterKey, apiKey);
		PushPayload payload = push_ios_alert_with_title(regid, alert, title, content);
		try {
			jpushClient.sendPush(payload);
		} catch (APIConnectionException e) {
			logger.debug("连接错误，请重试：{}",e);
		} catch (APIRequestException e) {
			logger.debug("极光推送服务器故障：{}",e);
		}
	}
	
	public static void messageAndroidPush(String regid, String alert, String title, String content) {
		jpushClient = new JPushClient(masterKey, apiKey);
		//PushPayload payload = push_android_and_ios_alert(alias, alert, title, content);
		PushPayload payload = push_android_alert_with_title(regid, alert, title, content);
		try {
			PushResult result = jpushClient.sendPush(payload);
		} catch (APIConnectionException e) {
			logger.debug("连接错误，请重试：{}",e);
		} catch (APIRequestException e) {
			logger.debug("极光推送服务器故障：{}",e);
		}
	}

	/**
	 * 针对别名推送
	 * 
	 * @return
	 */
	public static PushPayload push_android_and_ios_alert(String alias, String alert, String title, String content) {
		return PushPayload.newBuilder()
				.setOptions(Options.newBuilder()
						.setTimeToLive(864000)
						.setApnsProduction(isproduction).build())// test:false， product:true
				.setPlatform(Platform.android_ios())// 设置接受的平台
				.setAudience(Audience.alias(alias))// Audience设置为all，说明采用广播方式推送，所有用户都可以接收到
				.setNotification(Notification.newBuilder().setAlert(alert)
						.addPlatformNotification(AndroidNotification.newBuilder().setTitle(title).addExtra("content", content).build())
						.addPlatformNotification(IosNotification.newBuilder()
								.setBadge(1)
								.addExtra("title", title)
								.addExtra("content", content).build())
						.build())
				.build();
	}

	/**
	 * 依据regid向Android或者IOS推送
	 * 
	 * @return
	 */
	public static PushPayload push_android_iso_alert_with_title(String regid, String alert, String title, String content) {
		return PushPayload.newBuilder()
				.setOptions(Options.newBuilder().setApnsProduction(false).build())// test:false， product:true
				.setPlatform(Platform.android_ios())
				.setAudience(Audience.registrationId(regid))
				.setNotification(Notification.newBuilder().setAlert(alert)
						.addPlatformNotification(AndroidNotification.newBuilder().setTitle(title).addExtra("content", content).build())
						.addPlatformNotification(IosNotification.newBuilder().addExtra("title", title).addExtra("content", content).build())
						.build())
				.build();
	}

	/**
	 * 依据regid向Android推送
	 * 
	 * @return
	 */
	public static PushPayload push_android_alert_with_title(String regid, String alert, String title, String content) {
		return PushPayload.newBuilder().setPlatform(Platform.android())
				.setAudience(Audience.registrationId(regid))
				.setNotification(Notification.newBuilder().addPlatformNotification(AndroidNotification.newBuilder()
						.setAlert(alert).setTitle(title).addExtra("content", content).build()).build())
				.build();
	}

	/**
	 * 依据regid向IOS推送
	 * 
	 * @return
	 */
	public static PushPayload push_ios_alert_with_title(String regid, String alert, String title, String content) {
		return PushPayload.newBuilder()
				.setOptions(Options.newBuilder().setApnsProduction(false).build())// test:false， product:true
				.setPlatform(Platform.ios()).setAudience(Audience.registrationId(regid))
				.setNotification(Notification.newBuilder().addPlatformNotification(IosNotification.newBuilder()
						.setAlert(alert).addExtra("title", title).addExtra("content", content).build()).build())
				.build();
	}
}

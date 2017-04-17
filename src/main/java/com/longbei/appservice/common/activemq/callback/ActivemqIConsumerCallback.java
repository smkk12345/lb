package com.longbei.appservice.common.activemq.callback;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.activemq.IActiveMq.IConsumerCallback;

import javax.jms.MapMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Consumer callback处理函数
 * Created by yinxiong on 2016/6/30.
 */
public class ActivemqIConsumerCallback implements IConsumerCallback {

	private static Logger logger = LoggerFactory.getLogger(ActivemqIConsumerCallback.class);
	
	@Override
	public void onReceive(MapMessage message) {
		//TODO 用loggger
		logger.info("onReceive message = {}", JSON.toJSON(message).toString());
//		System.out.print(message.toString());
	}
}

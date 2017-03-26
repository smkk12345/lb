package com.longbei.appservice.common.activemq.callback;

import com.longbei.appservice.common.activemq.IActiveMq.IConsumerCallback;

import javax.jms.MapMessage;

/**
 * Consumer callback处理函数
 * Created by yinxiong on 2016/6/30.
 */
public class ActivemqIConsumerCallback implements IConsumerCallback {

	@Override
	public void onReceive(MapMessage message) {
		System.out.print(message.toString());
	}
}

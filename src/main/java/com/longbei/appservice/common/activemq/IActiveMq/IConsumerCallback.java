package com.longbei.appservice.common.activemq.IActiveMq;

import javax.jms.MapMessage;

/**
 * Created by yinxiong on 2016/6/30.
 */
public interface IConsumerCallback {
	/**
	 * 收到消息后回调
	 */
	public void onReceive(MapMessage message);
}

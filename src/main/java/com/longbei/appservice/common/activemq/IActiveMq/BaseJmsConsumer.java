package com.longbei.appservice.common.activemq.IActiveMq;

import javax.jms.MapMessage;

/**
 * Created by yinxiong on 2016/6/30.
 */
public interface BaseJmsConsumer {

	public MapMessage receiveMsg(IConsumerCallback callback);
}

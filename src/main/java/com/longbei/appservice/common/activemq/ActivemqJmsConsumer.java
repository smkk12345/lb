package com.longbei.appservice.common.activemq;

import com.longbei.appservice.common.activemq.IActiveMq.BaseJmsConsumer;
import com.longbei.appservice.common.activemq.IActiveMq.IConsumerCallback;
import org.springframework.jms.core.support.JmsGatewaySupport;
import javax.jms.MapMessage;

/**
 * smkk
 */
public class ActivemqJmsConsumer extends JmsGatewaySupport implements BaseJmsConsumer {

	@Override
	public MapMessage receiveMsg(IConsumerCallback consumerCallback) {

		MapMessage message = (MapMessage) getJmsTemplate().receive();

		if (null != message) {
			consumerCallback.onReceive(message);
		}
		return message;
	}
}

package com.longbei.appservice.common.activemq;

import org.springframework.jms.core.JmsTemplate;

import javax.jms.*;

/**
 * Created by yinxiong on 2016/6/30.
 */
public class BaseActiveMQJmsTemplate extends JmsTemplate{
	private Session session;

	public BaseActiveMQJmsTemplate() {
		super();
	}

	public BaseActiveMQJmsTemplate(ConnectionFactory connectionFactory) {
		super(connectionFactory);
	}

	public void doSend(MessageProducer producer, Message message) throws JMSException {
		if (isExplicitQosEnabled()) {
			producer.send(message, getDeliveryMode(), getPriority(), getTimeToLive());
		} else {
			producer.send(message);
		}
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
}

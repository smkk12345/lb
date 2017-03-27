package com.longbei.appservice.common.activemq;

import com.longbei.appservice.common.activemq.IActiveMq.BaseJmsProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.support.JmsGatewaySupport;
import org.springframework.util.StringUtils;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.List;

/**
 * Created by yinxiong on 2016/6/30.
 */
public class ActivemqJmsProducer extends JmsGatewaySupport implements BaseJmsProducer {
	Logger logger = LoggerFactory.getLogger(ActivemqJmsProducer.class);

	/**
	 * 发送格式: [type]:[domainName]:[domainIds]
	 * @param action
	 * @param domainName
	 * @param ids
	 */
	@Override
	public void sendMsg(final String action, final String domainName, final String ids) {
		getJmsTemplate().send(new MessageCreator() {
			private Message message;

			public Message createMessage(Session session) throws JMSException {
				message = session.createMapMessage();
				message.setStringProperty("action", action);
				message.setStringProperty("domainName", domainName);
				message.setStringProperty("ids", ids);


				logger.debug("send message to broker: {}", message.toString());

				return message;
			}
		});
	}

	/**
	 * 发送格式:  [type]:[domainName]:[domainIds]
	 * @param action
	 * @param domainName
	 * @param idList
	 */
	@Override
	public void sendMsg(final String action, final String domainName, final List<String> idList) {
		sendMsg(action, domainName, StringUtils.collectionToDelimitedString(idList, ","));
	}

	/**
	 * 发送格式:  [type]:[domainName]:[domainIds]
	 * @param action
	 * @param claszz
	 * @param ids
	 */
	public void sendMsg(final String action, final Class<?> claszz, final String ids) {
		sendMsg(action, claszz.getName(), ids);
	}

	/**
	 * 发送格式:  [type]:[domainName]:[domainIds]
	 * @param action
	 * @param claszz
	 * @param idList
	 */
	@Override
	public void sendMsg(final String action, final Class<?> claszz, final List<String> idList) {
		sendMsg(action, claszz.getName(), StringUtils.collectionToDelimitedString(idList, ","));
	}

}

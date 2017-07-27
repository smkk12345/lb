package com.longbei.appservice.common.service.mq.send;

import com.longbei.appservice.common.activemq.BaseActiveMQJmsTemplate;
import com.longbei.appservice.common.activemq.IActiveMq.BaseJmsProducer;
import com.netflix.discovery.converters.Auto;
import org.apache.activemq.command.ActiveMQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.*;

/**
 * 广播消息发送
 * Created by luye on 2017/1/18.
 */
@Service
public class TopicMessageSendService {

    private  static Logger logger = LoggerFactory.getLogger(TopicMessageSendService.class);
    /**
     * 发送一条消息到指定的队列（目标）
     * @param topicName 队列名称
     * @param message 消息内容
     */
    @Autowired
    @Qualifier("commontopic")
    private Topic topic;


    @Autowired
    private BaseActiveMQJmsTemplate baseActiveMQJmsTemplate;

    public void send(String action, String domain, final String message){
        baseActiveMQJmsTemplate.send(topic, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session
                        .createTextMessage(message);
                return textMessage;
            }
        });
    }


}

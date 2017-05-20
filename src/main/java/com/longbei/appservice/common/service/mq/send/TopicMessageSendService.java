package com.longbei.appservice.common.service.mq.send;

import com.longbei.appservice.common.activemq.IActiveMq.BaseJmsProducer;
import com.netflix.discovery.converters.Auto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

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
    @Qualifier("topicJmsProducer")
    private BaseJmsProducer topicJmsProducer;

    public void send(String action, String domain, String message){
        topicJmsProducer.sendMsg(action,domain,message);
    }


}

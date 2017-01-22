package com.longbei.appservice.common.service.mq.send;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * 广播消息发送
 * Created by luye on 2017/1/18.
 */
//@Service
public class TopicMessageSendService {

    private  static Logger logger = LoggerFactory.getLogger(TopicMessageSendService.class);
    /**
     * 发送一条消息到指定的队列（目标）
     * @param topicName 队列名称
     * @param message 消息内容
     */
    public void send(String topicName,final String message){

    }


}

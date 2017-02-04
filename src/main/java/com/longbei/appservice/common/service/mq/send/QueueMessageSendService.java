package com.longbei.appservice.common.service.mq.send;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import javax.jms.Queue;

/**
 * 队列消息发送
 * Created by luye on 2017/1/18.
 */
@Service
public class QueueMessageSendService {

    private  static Logger logger = LoggerFactory.getLogger(QueueMessageSendService.class);

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;//使用JmsMessagingTemplate将消息放入队列

    @Autowired
    @Qualifier("addqueue")
    private Queue addqueue;

    @Autowired
    @Qualifier("updatequeue")
    private Queue updatequeue;

    /**
     * 向添加队列发送消息
     * @param message
     */
    public void sendAddMessage(String message) {
        this.jmsMessagingTemplate.convertAndSend(this.addqueue, message);
    }

    /**
     * 向更新的队列发送消息
     * @param message
     */
    public void sendUpdateMessage(String message) {
        this.jmsMessagingTemplate.convertAndSend(this.updatequeue, message);
    }









}

package com.longbei.appservice.common.service.mq.reciver;

import com.longbei.appservice.config.ActiveMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.Message;
import javax.jms.MessageListener;


/**
 * mq 添加消息接收
 * Created by luye on 2017/1/18.
 */
@Service
public class AddMessageReceiveService{

    private static Logger logger = LoggerFactory.getLogger(AddMessageReceiveService.class);

    @JmsListener(destination="${spring.activemq.queue.name.add}")
    public void receiveMessage(String msg){
        System.out.println("监听接收到的消息是:"+msg);//打印队列内的消息
    }

}

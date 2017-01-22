package com.longbei.appservice.common.service.mq.reciver;


import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * mq 更新消息接收
 * Created by luye on 2017/1/18.
 */
@Service
public class UpdateMessageReceiveService{

    @JmsListener(destination="${spring.activemq.queue.name.update}")
    public void receiveMessage(String msg){
        System.out.println("监听接收到的消息是:"+msg);//打印队列内的消息
    }

}

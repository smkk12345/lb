package com.longbei.appservice.common.service.mq.reciver;


import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * mq 广播消息处理
 * Created by luye on 2017/1/18.
 */
//@Service
public class TopicMessageReciverService implements MessageListener{

    @Override
    public void onMessage(Message message) {
        //业务处理
    }
}

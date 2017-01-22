package com.longbei.appservice.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTempTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.Queue;
import javax.jms.Topic;

/**
 * activemq 配置
 *
 * @author luye
 * @create 2017-01-22 上午9:46
 **/
@Configuration
@EnableJms
public class ActiveMQConfig {

    @Value("${spring.activemq.queue.name.add}")
    private String addqueue;
    @Value("${spring.activemq.queue.name.update}")
    private String updatequeue;
    @Value("${spring.activemq.topic.name.common}")
    private String topiccommon;


    @Bean(name = "addqueue")
    public Queue addQueue(){
        return new ActiveMQQueue(addqueue);
    }

    @Bean(name = "updatequeue")
    public Queue updateQueue(){
        return new ActiveMQQueue(updatequeue);
    }

    @Bean
    public Topic topic(){
        return new ActiveMQTempTopic(topiccommon);
    }

    public String getAddqueue() {
        return addqueue;
    }

    public void setAddqueue(String addqueue) {
        this.addqueue = addqueue;
    }

    public String getUpdatequeue() {
        return updatequeue;
    }

    public void setUpdatequeue(String updatequeue) {
        this.updatequeue = updatequeue;
    }

    public String getTopiccommon() {
        return topiccommon;
    }

    public void setTopiccommon(String topiccommon) {
        this.topiccommon = topiccommon;
    }
}

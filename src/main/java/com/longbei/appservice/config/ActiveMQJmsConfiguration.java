package com.longbei.appservice.config;

import com.longbei.appservice.common.activemq.ActivemqJmsProducer;
import com.longbei.appservice.common.activemq.BaseActiveMQJmsTemplate;
import com.longbei.appservice.common.activemq.IActiveMq.BaseJmsProducer;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTempTopic;

import javax.jms.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

/**
 * Created by lixiaojun on 2016/6/23.
 */
@Configuration
@EnableJms
public class ActiveMQJmsConfiguration {
    Logger logger = LoggerFactory.getLogger(ActiveMQJmsConfiguration.class);

    private String brokerURL = "tcp://192.168.1.22:61616";

    private boolean pubSubDomain = true;

    private int maxConnections = 100;

    private String defaultDestjnation="testsmkk";

    private String userName;

    private String password;

    @Value("${spring.activemq.queue.name.add}")
    private String addqueue;
    @Value("${spring.activemq.queue.name.update}")
    private String updatequeue;
    @Value("${spring.activemq.topic.name.common}")
    private String topiccommon;


    private static BaseActiveMQJmsTemplate jmsTemplate;

//    private static PooledConnectionFactory pooledConnectionFactory;

    private static ActiveMQConnectionFactory activeMQConnectionFactory;

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

    /**
     * 初始化PooledConnectionFactory
     * @return
     */
//    @Bean
//    public PooledConnectionFactory pooledConnectionFactoryBean() {
//        if (null != pooledConnectionFactory) {
//            return pooledConnectionFactory;
//        }
//        pooledConnectionFactory = new PooledConnectionFactory();
//        pooledConnectionFactory.setConnectionFactory(ConnectionFactoryBean());
//        pooledConnectionFactory.setMaxConnections(maxConnections);
//        logger.debug("oooo init pooledConnectionFactoryBean ....");
//
//        return pooledConnectionFactory;
//    }

    @Bean(name = "activeMQConnectionFactory")
    public ConnectionFactory ConnectionFactoryBean(){
        if(null != activeMQConnectionFactory){
            return activeMQConnectionFactory;
        }
        activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(brokerURL);

        if (null != userName && userName.length() > 0) {
            activeMQConnectionFactory.setUserName(userName);
            activeMQConnectionFactory.setPassword(password);
        }
        return activeMQConnectionFactory;
    }

    /**
     *
     * @return
     */
    @Bean(name="jmsTemplate")
    public BaseActiveMQJmsTemplate baseActiveMQJmsTemplateBean() {
        if (null != jmsTemplate) {
            return jmsTemplate;
        }
        jmsTemplate = new BaseActiveMQJmsTemplate();
        jmsTemplate.setConnectionFactory(ConnectionFactoryBean());
        logger.debug("oooo init elasticsearchJmsTemplateBean ....");
        return jmsTemplate;
    }

    public BaseActiveMQJmsTemplate initDestination(Queue queue){
        jmsTemplate = baseActiveMQJmsTemplateBean();
        jmsTemplate.setDefaultDestination(queue);
        return jmsTemplate;
    }


    @Bean(name="improveJmsProducer")
    public BaseJmsProducer baseJmsProducerBean() {
        ActivemqJmsProducer producer = new ActivemqJmsProducer();
        producer.setJmsTemplate(initDestination(addQueue()));
        logger.debug("oooo init improveJmsProducer ....");
        return producer;
    }

//    @Bean(name="improveJmsConsumer")
//    public BaseJmsConsumer baseJmsConsumerBean() {
//        ActivemqJmsConsumer consumer = new ActivemqJmsConsumer();
//        consumer.setJmsTemplate(initDestination(new ActiveMQQueue(defaultDestjnation)));
//        logger.debug("oooo init improveJmsConsumer ....");
//
//        return consumer;
//    }

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
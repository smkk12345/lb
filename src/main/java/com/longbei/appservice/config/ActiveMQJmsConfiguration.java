package com.longbei.appservice.config;

import com.longbei.appservice.common.activemq.ActivemqJmsConsumer;
import com.longbei.appservice.common.activemq.ActivemqJmsProducer;
import com.longbei.appservice.common.activemq.BaseActiveMQJmsTemplate;
import com.longbei.appservice.common.activemq.IActiveMq.BaseJmsConsumer;
import com.longbei.appservice.common.activemq.IActiveMq.BaseJmsProducer;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;

/**
 * Created by lixiaojun on 2016/6/23.
 */
@Configuration
public class ActiveMQJmsConfiguration {
    Logger logger = LoggerFactory.getLogger(ActiveMQJmsConfiguration.class);

    private String brokerURL = "tcp://192.168.1.22:61616";

    private boolean pubSubDomain = true;

    private int maxConnections = 100;

    private String defaultDestjnation="testsmkk";

    private String userName;

    private String password;


    private static BaseActiveMQJmsTemplate jmsTemplate;

    private static PooledConnectionFactory pooledConnectionFactory;

    /**
     * 初始化PooledConnectionFactory
     * @return
     */
    @Bean
    public PooledConnectionFactory pooledConnectionFactoryBean() {
        if (null != pooledConnectionFactory) {
            return pooledConnectionFactory;
        }

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(brokerURL);

        if (null != userName && userName.length() > 0) {
            activeMQConnectionFactory.setUserName(userName);
            activeMQConnectionFactory.setPassword(password);
        }

        pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(activeMQConnectionFactory);
        pooledConnectionFactory.setMaxConnections(maxConnections);

        logger.debug("oooo init pooledConnectionFactoryBean ....");

        return pooledConnectionFactory;
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
        jmsTemplate.setConnectionFactory(pooledConnectionFactoryBean());
        logger.debug("oooo init elasticsearchJmsTemplateBean ....");
        return jmsTemplate;
    }

    public BaseActiveMQJmsTemplate initDestination(ActiveMQQueue queue){
        jmsTemplate = baseActiveMQJmsTemplateBean();
        jmsTemplate.setDefaultDestination(queue);
        return jmsTemplate;
    }


    @Bean(name="improveJmsProducer")
    public BaseJmsProducer baseJmsProducerBean() {
        ActivemqJmsProducer producer = new ActivemqJmsProducer();
        producer.setJmsTemplate(initDestination(new ActiveMQQueue(defaultDestjnation)));

        logger.debug("oooo init improveJmsProducer ....");
        return producer;
    }

    @Bean(name="improveJmsConsumer")
    public BaseJmsConsumer baseJmsConsumerBean() {
        ActivemqJmsConsumer consumer = new ActivemqJmsConsumer();
        consumer.setJmsTemplate(initDestination(new ActiveMQQueue(defaultDestjnation)));

        logger.debug("oooo init improveJmsConsumer ....");
        return consumer;
    }


}
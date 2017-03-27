package com.longbei.appservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by longbei on 2016/9/9.
 */
@SuppressWarnings("static-access")
@Configuration
@ConfigurationProperties
@PropertySource({"classpath:appservice.properties"})
public class AppserviceConfig {


    public static String host_user_service;
    public static String host_outernet_service;
    public static String host_product_service;
    
    //龙币兑换花比例       龙币兑换进步币比例
    public static int moneytoflower;
    public static int moneytocoin;

    @Value("${service.outernet}")
    public void setHost_outernet_service(String host_outernet_service) {
        this.host_outernet_service = host_outernet_service;
    }
    @Value("${service.product}")
    public void setHost_product_service(String host_product_service) {
        this.host_product_service = host_product_service;
    }
    @Value("${service.user}")
    public void setHost_user_service(String host_user_service) {
        this.host_user_service = host_user_service;
    }
    
    
	@Value("${moneytoflower}")
	public void setMoneytoflower(int moneytoflower) {
    	this.moneytoflower = moneytoflower;
	}
	
    @Value("${moneytocoin}")
	public void setMoneytocoin(int moneytocoin) {
    	this.moneytocoin = moneytocoin;
	}

}

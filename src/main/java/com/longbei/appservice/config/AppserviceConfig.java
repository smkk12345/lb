package com.longbei.appservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by longbei on 2016/9/9.
 */
@SuppressWarnings("static-access")
@Configuration
@ConfigurationProperties
//@PropertySource({"classpath:appservice.properties"})
public class AppserviceConfig {


    public static String host_user_service;
    public static String host_outernet_service;
    public static String host_product_service;

    //人民币兑换龙币比例
    public static double yuantomoney;
    //花兑换龙币比例
    public static double flowertomoney;
    //龙币兑换进步币比例
    public static double moneytocoin;
    //花兑换进步币比例
    public static double flowertocoin;
    
    public static String ios_buyflower;
    
    public static String ios_buyflower_pro;

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
    
    
    @Value("${yuantomoney}")
	public void setYuantomoney(double yuantomoney) {
    	this.yuantomoney = yuantomoney;
	}
    
	@Value("${flowertomoney}")
	public void setFlowertomoney(double flowertomoney) {
    	this.flowertomoney = flowertomoney;
	}
	
    @Value("${moneytocoin}")
	public void setMoneytocoin(double moneytocoin) {
    	this.moneytocoin = moneytocoin;
	}
    
    @Value("${flowertocoin}")
	public void setFlowertocoin(double flowertocoin) {
    	this.flowertocoin = flowertocoin;
	}
    
    @Value("${ios_buyflower}")
	public void setIos_buyflower(String ios_buyflower) {
    	this.ios_buyflower = ios_buyflower;
	}
    
    @Value("${ios_buyflower_pro}")
	public void setIos_buyflower_pro(String ios_buyflower_pro) {
    	this.ios_buyflower_pro = ios_buyflower_pro;
	}

    public static String oss_media;

    public void setOss_media(String oss_media) {
        this.oss_media = oss_media;
    }

    public static String longbei_helper;

    public void setLongbei_helper(String longbei_helper){
        this.longbei_helper = longbei_helper;
    }


}

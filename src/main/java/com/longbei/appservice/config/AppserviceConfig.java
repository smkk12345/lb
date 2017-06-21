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
    public static double yuantomoney;//系统初始化
    //花兑换龙币比例
    public static double flowertomoney;//系统初始化
    //龙币兑换进步币比例
    public static double moneytocoin;//系统初始化
    //花兑换进步币比例
    public static double flowertocoin;//系统初始化

    public static String shareip;//系统初始化

    public static String shareport;//系统初始化
    
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
    

    @Value("${ios_buyflower}")
	public void setIos_buyflower(String ios_buyflower) {
    	this.ios_buyflower = ios_buyflower;
	}
    
    @Value("${ios_buyflower_pro}")
	public void setIos_buyflower_pro(String ios_buyflower_pro) {
    	this.ios_buyflower_pro = ios_buyflower_pro;
	}

    public static String oss_media;

    @Value("${oss_media}")
    public void setOss_media(String oss_media) {
        this.oss_media = oss_media;
    }

    /**
     * h5页面  帮助中心  分享  帮主名片片
     * add  by smkk
     */
    public static String h5_helper;
    public static String h5_rankcard;
    public static String h5_share_improve_detail;
    public static String h5_share_rank_detail;
    public static String h5_share_rank_award;
    public static String h5_share_rank_improve;
    public static String h5_share_goal_detail;
    public static String h5_share_circle_detail;//圈子详情
    public static String h5_share_invite;
    public static String h5_agreementurl;
    public static String h5_levelprivilege;
    public static String articleurl;


    public static String pcurl;
    @Value("${pcurl}")
    public void setPcurl(String pcurl) {
        this.pcurl = pcurl;
    }
}

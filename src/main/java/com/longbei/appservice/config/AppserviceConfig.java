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
    public static String h5_share_invite;
    public static String h5_agreementurl;
    public static String h5_levelprivilege;
    public static String articleurl;

    @Value("${articleurl}")
    public static void setArticleurl(String articleurl) {
        AppserviceConfig.articleurl = articleurl;
    }

    @Value("${h5.helper}")
    public void setH5_helper(String h5_helper){
        this.h5_helper = h5_helper;
    }

    @Value("${h5.rankcard}")
    public void setH5_rankCard(String h5_rankcard){
        this.h5_rankcard = h5_rankcard;
    }
    @Value("${h5.share.improve.detail}")
    public void setH5_share_improve_detail(String h5_share_improve_detail){
        this.h5_share_improve_detail = h5_share_improve_detail;
    }
    @Value("${h5.share.rank.detail}")
    public void setH5_share_rank_detail(String h5_share_rank_detail){
        this.h5_share_rank_detail = h5_share_rank_detail;
    }
    @Value("${h5.share.rank.award}")
    public void setH5_share_rank_award(String h5_share_rank_award){
        this.h5_share_rank_award = h5_share_rank_award;
    }
    @Value("${h5.share.rank.improve}")
    public void setH5_share_rank_improve(String h5_share_rank_improve){
        this.h5_share_rank_improve = h5_share_rank_improve;
    }
    @Value("${h5.share.goal.detail}")
    public void setH5_share_goal_detail(String h5_share_goal_detail){
        this.h5_share_goal_detail = h5_share_goal_detail;
    }
    @Value("${h5.agreementurl}")
    public void setH5_agreementurl(String h5_agreementurl){
        this.h5_agreementurl = h5_agreementurl;
    }

    @Value("${h5.share.invite}")
    public void setH5_share_invite(String h5_share_invite){
        this.h5_share_invite = h5_share_invite;
    }
}

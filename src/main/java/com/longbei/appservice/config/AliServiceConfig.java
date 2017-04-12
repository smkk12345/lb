package com.longbei.appservice.config;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * oss  alidayu
 * Created by longbei on 2016/9/12.
 */
@Configuration
@ConfigurationProperties
public class AliServiceConfig {

//    private static Logger logger = LoggerFactory.getLogger(AliServiceConfig.class);

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.accessKeyId}")
    private String accessKeyId;

    @Value("${oss.secretAccessKey}")
    private String secretAccessKey;

    @Value("${oss.bucketName}")
    private String bucketName;


    public static String appKey;
    public static String appSecret;
    public static String aliUrl;
    public static String signName;
    public static String smsType;
    public static String templateCode;
    
    public static String rongyun_key;
    public static String rongyun_secret;
    
    //Jpush
    public static String jpush_api_key;
    public static String jpush_master_key;
    public static String isproduction;

    public static OSSClient ossClient;   //ossclient

    @Bean
    public OSSClient createOssClient(){
        if(null != ossClient){
            return ossClient;
        }
        ossClient = new OSSClient(endpoint,accessKeyId,secretAccessKey);
        return ossClient;
    }


    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

	public static String getAppKey() {
		return appKey;
	}

	public static void setAppKey(String appKey) {
		AliServiceConfig.appKey = appKey;
	}

	public static String getAppSecret() {
		return appSecret;
	}

	public static void setAppSecret(String appSecret) {
		AliServiceConfig.appSecret = appSecret;
	}

	public static String getAliUrl() {
		return aliUrl;
	}

	public static void setAliUrl(String aliUrl) {
		AliServiceConfig.aliUrl = aliUrl;
	}

	public static String getSignName() {
		return signName;
	}

	public static void setSignName(String signName) {
		AliServiceConfig.signName = signName;
	}

	public static String getTemplateCode() {
		return templateCode;
	}

	public static void setTemplateCode(String templateCode) {
		AliServiceConfig.templateCode = templateCode;
	}

	public static String getSmsType() {
		return smsType;
	}

	public static void setSmsType(String smsType) {
		AliServiceConfig.smsType = smsType;
	}

	public static String getRongyun_key() {
		return rongyun_key;
	}

	public static void setRongyun_key(String rongyun_key) {
		AliServiceConfig.rongyun_key = rongyun_key;
	}

	public static String getRongyun_secret() {
		return rongyun_secret;
	}

	public static void setRongyun_secret(String rongyun_secret) {
		AliServiceConfig.rongyun_secret = rongyun_secret;
	}

	public static String getJpush_api_key() {
		return jpush_api_key;
	}

	public static void setJpush_api_key(String jpush_api_key) {
		AliServiceConfig.jpush_api_key = jpush_api_key;
	}

	public static String getJpush_master_key() {
		return jpush_master_key;
	}

	public static void setJpush_master_key(String jpush_master_key) {
		AliServiceConfig.jpush_master_key = jpush_master_key;
	}

	public static String getIsproduction() {
		return isproduction;
	}

	public static void setIsproduction(String isproduction) {
		AliServiceConfig.isproduction = isproduction;
	}
    
	
}

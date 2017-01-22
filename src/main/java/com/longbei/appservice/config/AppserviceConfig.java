package com.longbei.appservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by longbei on 2016/9/9.
 */
@Configuration
@ConfigurationProperties
@PropertySource({"classpath:appservice.properties"})
public class AppserviceConfig {

    public static String oss_cdnurl;
    public static String page_size;

    /*
        alidayu 相关配置
         */
    public static String smsType;
    public static String signName;
    public static String templateCode;
    /**
     * oss 配置
     */
    public static String bucketName;
    public static String ossurl;
    
    /**
     * web_server   路径配置
     * @return
     */
    public static String web_server;
    
    public static String getWeb_server() {
        return web_server;
    }

    public static void setWeb_server(String web_server) {
        AppserviceConfig.web_server = web_server;
    }

    public static String getBucketName() {
        return bucketName;
    }

    public static void setBucketName(String bucketName) {
        AppserviceConfig.bucketName = bucketName;
    }

    public static String getSmsType() {
        return smsType;
    }

    public static void setSmsType(String smsType) {
        AppserviceConfig.smsType = smsType;
    }

    public static String getSignName() {
        return signName;
    }

    public static void setSignName(String signName) {
        AppserviceConfig.signName = signName;
    }

    public static String getTemplateCode() {
        return templateCode;
    }

    public static void setTemplateCode(String templateCode) {
        AppserviceConfig.templateCode = templateCode;
    }

    public static String getOss_cdnurl() {
        return oss_cdnurl;
    }

    public static void setOss_cdnurl(String oss_cdnurl) {
        AppserviceConfig.oss_cdnurl = oss_cdnurl;
    }

    public static String getPage_size() {
        return page_size;
    }

    public static void setPage_size(String page_size) {
        AppserviceConfig.page_size = page_size;
    }

    public static String getOssurl() {
        return ossurl;
    }

    public static void setOssurl(String ossurl) {
        AppserviceConfig.ossurl = ossurl;
    }
}

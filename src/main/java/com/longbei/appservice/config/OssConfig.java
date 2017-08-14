package com.longbei.appservice.config;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * oss
 *
 * @author luye
 * @create 2017-07-19 下午7:30
 **/
@Configuration
public class OssConfig {

    @Value("${alimedia.endpoint}")
    private String endpoint;
    @Value("${alimedia.accessKeyId}")
    private String accessKeyId;
    @Value("${alimedia.accessKeySecret}")
    private String secretAccessKey;

    public static String inputurl;

    public static String bucketName;

    public static String inputBacketName;

    public static String url;

    @Bean(name = "ossClient")
    public OSSClient getOSSClient(){
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,secretAccessKey);
        return ossClient;
    }


    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }

    public  String getBucketName() {
        return bucketName;
    }

    @Value("${alimedia.bucketName}")
    public  void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public static String getInputBacketName() {
        return inputBacketName;
    }

    @Value("${alimedia.inputBacketName}")
    public void setInputBacketName(String inputBacketName) {
        this.inputBacketName = inputBacketName;
    }

    public String getUrl() {
        return url;
    }

    @Value("${alimedia.url}")
    public void setUrl(String url) {
        this.url = url;
    }

    public String getInputurl() {
        return inputurl;
    }

    @Value("${alimedia.inputurl}")
    public void setInputurl(String inputurl) {
        this.inputurl = inputurl;
    }
}

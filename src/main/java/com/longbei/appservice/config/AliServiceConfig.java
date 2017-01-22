package com.longbei.appservice.config;

import com.aliyun.oss.OSSClient;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * oss  alidayu
 * Created by longbei on 2016/9/12.
 */
@Configuration
public class AliServiceConfig {

    private static Logger logger = LoggerFactory.getLogger(AliServiceConfig.class);

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.accessKeyId}")
    private String accessKeyId;

    @Value("${oss.secretAccessKey}")
    private String secretAccessKey;

    @Value("${oss.bucketName}")
    private String bucketName;

    @Value("${alidayu.appKey}")
    private String appKey;

    @Value("${alidayu.appSecret}")
    private String appSecret;

    @Value("${alidayu.aliUrl}")
    private String aliUrl;

    public static OSSClient ossClient;   //ossclient
    public static TaobaoClient alidayuclient; //alidayu

    @Bean
    public OSSClient createOssClient(){
        if(null != ossClient){
            return ossClient;
        }
        ossClient = new OSSClient(endpoint,accessKeyId,secretAccessKey);
        return ossClient;
    }

    @Bean
    public TaobaoClient createTaobaoClient(){
        if(null != alidayuclient){
            return alidayuclient;
        }
        alidayuclient = new DefaultTaobaoClient(aliUrl, appKey, appSecret);
        return alidayuclient;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}

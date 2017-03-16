package com.longbei.appservice.config;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${service.user}")
    private String host_user_service;
    @Value("${service.outernet}")
    private String host_outernet_service;
    @Value("${service.product}")
    private String host_product_service;

    public void setHost_outernet_service(String host_outernet_service) {
        this.host_outernet_service = host_outernet_service;
    }

    public String getHost_outernet_service() {
        return host_outernet_service;
    }

    public String getHost_product_service() {
        return host_product_service;
    }

    public String getHost_user_service() {
        return host_user_service;
    }

    public void setHost_product_service(String host_product_service) {
        this.host_product_service = host_product_service;
    }

    public void setHost_user_service(String host_user_service) {
        this.host_user_service = host_user_service;
    }
}

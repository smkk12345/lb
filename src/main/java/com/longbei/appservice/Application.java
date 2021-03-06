package com.longbei.appservice;

import com.longbei.appservice.service.impl.MediaResourceServiceImpl;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.data.redis.cache.RedisCacheManager;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(com.longbei.appservice.config.AppserviceConfig.class)
@EnableSwagger2
@ServletComponentScan
@EnableDiscoveryClient
@EnableCaching
@EnableFeignClients(basePackages =
        {"com.longbei.appservice.service.api.outernetservice",
                "com.longbei.appservice.service.api.userservice" ,
                        "com.longbei.appservice.service.api.productservice",
                "com.longbei.appservice.service.api.authservice",
                "com.longbei.appservice.service.api.staticresourceservice"})
public class Application extends SpringBootServletInitializer {
    private static Logger logger = Logger.getLogger(Application.class);



    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

//    @Autowired
//    public void configRedisCacheManger(RedisCacheManager rd){
//        rd.setDefaultExpiration(100L);
//    }


    /**
     * Start
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

//        MediaResourceServiceImpl.initStopService();
        logger.info("SpringBoot Start Success");
    }

}

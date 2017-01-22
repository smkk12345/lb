package com.longbei.appservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by longbei on 2016/9/9.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()  // 选择那些路径和api会生成document
//                .paths(PathSelectors.regex("/business/*"))
                .apis(RequestHandlerSelectors.basePackage("com.longbei.controller")) // 对所有api进行监控
//                .paths() // 对所有路径进行监控
                .build()
                .apiInfo(getApiInfo());
    }
    private ApiInfo getApiInfo() {
        ApiInfo apiInfo = new ApiInfo("龙杯公众平台API",//大标题
                "",//小标题
                "V_1.0",//版本
                "NO terms of service",
                "luye",//作者
                null,//链接显示文字
                null//网站链接
        );

        return apiInfo;
    }

}
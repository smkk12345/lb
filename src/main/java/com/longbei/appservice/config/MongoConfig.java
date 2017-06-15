//package com.longbei.appservice.config;
//
//import com.mongodb.MongoClientURI;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.mongodb.MongoDbFactory;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
//import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
//import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
//import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
//import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
//
//import java.net.URLEncoder;
//import java.net.UnknownHostException;
//
///**
// * Created by lixb on 2017/6/14.
// */
////@Configuration
//public class MongoConfig {
//
//    //@Value("${mongo.host}")
//    @Value("${spring.data.mongodb.uri1}")
//    private String MONGO_COMMON_URL;
//    @Value("${spring.data.mongodb.uri2}")
//    private String MONGO_TIMELINE_URL;
//
//    @Bean
//    public MongoMappingContext mongoMappingContext() {
//        MongoMappingContext mappingContext = new MongoMappingContext();
//        return mappingContext;
//    }
//
//    @Bean //使用自定义的typeMapper去除写入mongodb时的“_class”字段
//    public MappingMongoConverter mappingMongoConverter1() throws Exception {
//        DefaultDbRefResolver dbRefResolver = new DefaultDbRefResolver(this.dbFactory1());
//        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, this.mongoMappingContext());
//        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
//        return converter;
//    }
//
//    @Bean //使用自定义的typeMapper去除写入mongodb时的“_class”字段
//    public MappingMongoConverter mappingMongoConverter2() throws Exception {
//        DefaultDbRefResolver dbRefResolver = new DefaultDbRefResolver(this.dbFactory2());
//        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, this.mongoMappingContext());
//        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
//        return converter;
//    }
//
//    @Bean
//    @Primary
//    public MongoDbFactory dbFactory1() throws UnknownHostException {
//        return new SimpleMongoDbFactory(new MongoClientURI(MONGO_COMMON_URL));
//    }
//
//    @Bean
//    @Primary
//    public MongoDbFactory dbFactory2() throws UnknownHostException {
//        return new SimpleMongoDbFactory(new MongoClientURI(MONGO_COMMON_URL));
//    }
//
//    @Bean
//    public MongoTemplate mongoTemplate() throws Exception {
//        return new MongoTemplate(this.dbFactory1(), this.mappingMongoConverter1());
//    }
//
//    @Bean
//    public MongoTemplate timeLineMongoTemplate() throws Exception {
//        return new MongoTemplate(this.dbFactory2(), this.mappingMongoConverter2());
//    }
//
//
//}

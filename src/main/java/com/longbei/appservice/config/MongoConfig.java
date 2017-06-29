//package com.longbei.appservice.config;
//
//import com.mongodb.MongoClientURI;
//import org.springframework.beans.factory.annotation.Qualifier;
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
///**
// * Created by lixb on 2017/6/14.
// */
//@Configuration
//public class MongoConfig {
//
////    uri0: mongodb://longbeitest:longbeitest@192.168.1.45:27017/longbei0 #基础数据 appuser
////    uri1: mongodb://longbeitest:longbeitest@192.168.1.45:27017/longbei1 #动态
////    uri2: mongodb://longbeitest:longbeitest@192.168.1.45:27017/longbei2 #好友
////    uri3: mongodb://longbeitest:longbeitest@192.168.1.45:27017/longbei3 #关注
////    uri4: mongodb://longbeitest:longbeitest@192.168.1.45:27017/longbei4 #广场
////    uri5: mongodb://longbeitest:longbeitest@192.168.1.45:27017/longbei5 #我的
////    uri6: mongodb://longbeitest:longbeitest@192.168.1.45:27017/longbei6 #熟人
////    uri7: mongodb://longbeitest:longbeitest@192.168.1.45:27017/longbei7 #进步详情
//
//    @Value("${spring.data.mongodb.uri0}")
//    private String MONGO_URL0;
//    @Value("${spring.data.mongodb.uri1}")
//    private String MONGO_URL1;
//    @Value("${spring.data.mongodb.uri2}")
//    private String MONGO_URL2;
//    @Value("${spring.data.mongodb.uri3}")
//    private String MONGO_URL3;
//    @Value("${spring.data.mongodb.uri4}")
//    private String MONGO_URL4;
//    @Value("${spring.data.mongodb.uri5}")
//    private String MONGO_URL5;
//    @Value("${spring.data.mongodb.uri6}")
//    private String MONGO_URL6;
//    @Value("${spring.data.mongodb.uri7}")
//    private String MONGO_URL7;
//
//    private MongoTemplate createMongoTemplate(String mongoName) throws Exception{
//        MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(new MongoClientURI(mongoName));
//        DefaultDbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
//        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
//        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
//        return new MongoTemplate(mongoDbFactory,  converter);
//    }
//
//    @Primary
//    @Bean(name = "mongoTemplate")
//    public MongoTemplate mongoTemplate0() throws Exception {
//        return createMongoTemplate(MONGO_URL0);
//    }
//
//    @Bean
//    @Qualifier("mongoTemplateDynamic")
//    public MongoTemplate mongoTemplate1() throws Exception {
//        return createMongoTemplate(MONGO_URL1);
//    }
//
//    @Bean
//    @Qualifier("mongoTemplateFriend")
//    public MongoTemplate mongoTemplate2() throws Exception {
//        return createMongoTemplate(MONGO_URL2);
//    }
//
//    @Bean
//    @Qualifier("mongoTemplateLike")
//    public MongoTemplate mongoTemplate3() throws Exception {
//        return createMongoTemplate(MONGO_URL3);
//    }
//
//    @Bean
//    @Qualifier("mongoTemplateSquare")
//    public MongoTemplate mongoTemplate4() throws Exception {
//        return createMongoTemplate(MONGO_URL4);
//    }
//
//    @Bean
//    @Qualifier("mongoTemplateMain")
//    public MongoTemplate mongoTemplate5() throws Exception {
//        return createMongoTemplate(MONGO_URL5);
//    }
//
//    @Bean
//    @Qualifier("mongoTemplateAcquaintance")
//    public MongoTemplate mongoTemplate6() throws Exception {
//        return createMongoTemplate(MONGO_URL6);
//    }
//
//    @Bean
//    @Qualifier("mongoTemplateImprove")
//    public MongoTemplate mongoTemplate7() throws Exception {
//        return createMongoTemplate(MONGO_URL7);
//    }
//
//}

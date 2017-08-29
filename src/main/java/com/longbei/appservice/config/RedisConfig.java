package com.longbei.appservice.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.longbei.appservice.common.constant.RedisCacheNames;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * redis config info
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport{

    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator() {
            /**
             * 对参数进行拼接后MD5
             */
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(".").append(method.getName());

                StringBuilder paramsSb = new StringBuilder();
                for (Object param : params) {
                    // 如果不指定，默认生成包含到键值中
                    if (param != null) {
                        paramsSb.append(param.toString());
                    }
                }
                if (paramsSb.length() > 0) {
                    sb.append("_").append(paramsSb);
                }
                return sb.toString();
            }
        };
    }


    @Bean
    public CacheManager cacheManager(RedisTemplate<Object, Object> redisCacheTemplate, RedisCacheNames redisCacheKeys) {
        RedisCacheManager rcm = new RedisCacheManager(redisCacheTemplate);
        // 设置缓存默认过期时间（全局的）
        rcm.setDefaultExpiration(1800);// 30分钟

        // 根据key设定具体的缓存时间，key统一放在常量类redisCacheKeys中
        rcm.setExpires(redisCacheKeys.getExpiresMap());
        List<String> cacheNames = new ArrayList<String>(redisCacheKeys.getExpiresMap().keySet());
        rcm.setCacheNames(cacheNames);

        return rcm;
    }


    @Bean(name = "redisTemplate")
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate(redisConnectionFactory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        template.setEnableTransactionSupport(false);
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    @ConditionalOnMissingBean(name = "redisCacheTemplate")
    public RedisTemplate<Object, Object> redisCacheTemplate(
            RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        template.setEnableTransactionSupport(false);
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

}
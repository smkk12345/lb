package com.longbei.appservice.dao.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * Created by smkk on 17/2/4.
 */
@Repository("springJedisDao")
public class SpringJedisDao {
    @Autowired
    private StringRedisTemplate redisTemplate;
    private static Logger logger = LoggerFactory.getLogger(SpringJedisDao.class);
    //无过期时间
    public boolean set(String key,String value){
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key,value);
            result = true;
        }catch (Exception e){
            logger.error("redis set error key={},value={},msg={}",key,value,e);
        }
        return result;
    }

    //带过期时间
    public boolean set(String key,String value,long timeout){
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key,value,timeout);
            result = true;
        }catch (Exception e){
            logger.error("redis set error key={},value={},timeout={},msg={}",key,value,timeout,e);
        }
        return result;
    }

    //带过期时间类型
    public boolean set(String key,String value,long timeout, TimeUnit unit){
        try {
            redisTemplate.opsForValue().set(key,value,timeout,unit);
        }catch (Exception e){
            logger.error("redis set error key={},value={},timeout={},unit={},msg={}",key,value,timeout,unit,e);
            return false;
        }
        return true;
    }
    //通过key获取value
    public String get(String key){
        String result = null;
        try{
            result = redisTemplate.opsForValue().get(key);
        }catch (Exception e){
            logger.error("redis get error key={},msg={}",key,e);
        }
        return result;
    }
    //删除key
    public boolean del(String key){
        boolean result = false;
        try{
            redisTemplate.delete(key);
            result = true;
        }catch (Exception e){
            logger.error("redis del error key={},msg={}",key,e);
        }
        return result;
    }

}

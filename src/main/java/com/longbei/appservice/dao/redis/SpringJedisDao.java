package com.longbei.appservice.dao.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
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
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(key,value);
            result = true;
        }catch (Exception e){
            logger.error("redis set error key={},value={}",key,value,e);
        }
        return result;
    }

    //带过期时间
    public boolean set(String key,String value,long timeout){
        boolean result = false;
        try {
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(key,value,timeout,TimeUnit.SECONDS);
            result = true;
        }catch (Exception e){
            logger.error("redis set error key={},value={},timeout={}",key,value,timeout,e);
        }
        return result;
    }

    //带过期时间类型
    public boolean set(String key,String value,long timeout, TimeUnit unit){
        try {
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(key,value,timeout,unit);
        }catch (Exception e){
            logger.error("redis set error key={},value={},timeout={},unit={}",key,value,timeout,unit,e);
            return false;
        }
        return true;
    }
    //通过key获取value
    public String get(String key){
        String result = null;
        try{
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            result = valueOperations.get(key);
        }catch (Exception e){
            logger.error("redis get error key={}",key,e);
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
            logger.error("redis del error key={}",key,e);
        }
        return result;
    }
    
    //判断key是否存在
    public boolean hasKey(String key){
        boolean result = false;
        try{
            redisTemplate.hasKey(key);
            result = true;
        }catch (Exception e){
            logger.error("redis hasKey error key = {}",key,e);
        }
        return result;
    }
    
    //递增   递减
    public long increment(String key, long delta){
        try{
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            return valueOperations.increment(key, delta);
        }catch (Exception e){
            logger.error("redis increment error key = {}, delta = {}", key, delta, e);
        }
        return 0;
    }

    //设置key的过期时间
    public boolean expire(String key,long timeout){
        boolean result = false;
        try{
            redisTemplate.expire(key,timeout,TimeUnit.SECONDS);
            result = true;
        }catch (Exception e){
            logger.error("redis expire error key = {}", key, e);
        }
        return result;
    }

    //--------------Map-----------------//
    //put整个map
    public boolean putAll(String key, Map map){
        boolean result = false;
        try{
            HashOperations<String, String, String> options = redisTemplate.opsForHash();
            options.putAll(key,map);
            result = true;
        }catch (Exception e){
            logger.error("redis putAll error key={},map={}",key,map,e);
        }
        return result;
    }
    //带过期时间   单位是秒
    public boolean putAll(String key, Map map,long timeout){
        boolean result = false;
        try{
            HashOperations<String, String, String> options = redisTemplate.opsForHash();
            options.putAll(key,map);
            redisTemplate.expire(key,timeout,TimeUnit.SECONDS);
            result = true;
        }catch (Exception e){
            logger.error("redis putAll error key={},map={}",key,map,e);
        }
        return result;
    }
    //map中放单个键值对
    public  boolean put(String key,String hashKey,String hashValue){
        boolean result = false;
        try{
            HashOperations<String, String, String> options = redisTemplate.opsForHash();
            options.put(key,hashKey,hashValue);
            result = true;
        }catch (Exception e){
            logger.error("redis put error key={},hashKey={},hashValue={}",key,hashKey,hashValue,e);
        }
        return result;
    }
    //判断map中hashKey是否存在
    public boolean hasKey(String key,String hashKey){
        try{
            HashOperations<String, String, String> options = redisTemplate.opsForHash();
            return options.hasKey(key,hashKey);
        }catch (Exception e){
            logger.error("redis hasKey error key={},hashKey={}",key,hashKey,e);
        }
        return false;
    }
    //get  获取map中键的值
    public String getHashValue(String key,String hashKey){
        try{
            HashOperations<String, String, String> options = redisTemplate.opsForHash();
            return options.get(key,hashKey);
        }catch (Exception e){
            logger.error("redis get error key={},hashKey={}",key,hashKey,e);
        }
        return  null;
    }

    //multiGet 获取多个value
    public List<String> multiGet(String key, Collection<String> fields){
        try{
            HashOperations<String, String, String> options = redisTemplate.opsForHash();
            return options.multiGet(key,fields);
        }catch(Exception e){
            logger.error("redis multiGet error key={},hashKey={}",key,fields,e);
        }
        return null;
    }
    //delete 删除map中的key 可以是多个
    public long delete(String key,String... hashKeys){
        try{
            HashOperations<String, String, String> options = redisTemplate.opsForHash();
            return options.delete(key,hashKeys);
        }catch (Exception e){
            logger.error("redis delete error key={},hashKeys={}",key,hashKeys,e);
        }
        return 0;
    }
    //increment map中的key对应的value 做递增操作
    public long increment(String key,String hashKey,long delta){
        try {
            HashOperations<String, String, String> options = redisTemplate.opsForHash();
            return options.increment(key,hashKey,delta);
        }catch (Exception e){
            logger.error("redis delete error key={},hashKey={},delta={}",key,hashKey,delta,e);
        }
        return 0;
    }
    //keys 获取map中的所有的keys
    public Set<String> keys(String key){
        try{
            HashOperations<String, String, String> options = redisTemplate.opsForHash();
            return options.keys(key);
        }catch (Exception e){
            logger.error("redis keys error key={}",key,e);
        }
        return null;
    }
    //---------------------zset---------------------//
//    public boolean keys1(String key){
//        try{
//            redisTemplate.opsForZSet().add;
//            return options.keys(key);
//        }catch (Exception e){
//            logger.error("redis keys error key={},msg={}",key,e);
//        }
//        return null;
//    }
    //---------------------set---------------------//
    public long sAdd(String key,String... values){
        try{
            SetOperations<String,String> setOperations = redisTemplate.opsForSet();
            //the number of elements that were added to the set,
            // not including all the elements already present into the set.
            long n = setOperations.add(key,values);
            return n;
        }catch (Exception e){
            logger.error("redis set add error ",e);
        }
        return 0;
    }

    //redis set 移除
    public long sRem(String key,String... hashKeys){
        try {
            SetOperations<String,String> setOperations = redisTemplate.opsForSet();
            //the number of members that were removed from the set,
            // not including non existing members
            return setOperations.remove(key,hashKeys);

        }catch (Exception e){
            logger.error("redis set sRem error",e);
        }
        return 0;
    }

    //the cardinality (number of elements) of the set, or 0 if key does not exist.
    public long sCard(String key){
        try{
            SetOperations<String,String> setOperations = redisTemplate.opsForSet();
            long n = setOperations.size(key);
            return n;
        }catch(Exception e){
            logger.error("redis set sSize error ",e);
        }
        return 0;
    }

    //---------------------list--------------------//
    public void a(){
        try{
//            ListOperations<String,List> listListOperations = redisTemplate.opsForList();

        }catch (Exception e){

        }
    }


}

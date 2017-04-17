package com.longbei.appservice.common.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;
import java.util.Set;

/**
 * Created by longbei on 2016/9/8.
 */
@Repository
public class RedisDao {

    private Logger logger = LoggerFactory.getLogger(RedisDao.class);

    @Autowired
    protected RedisTemplate redisTemplate;

    private static String redisCode = "utf-8";

    /**
     * 通过key删除
     * @param keys
     */
    public long del(final String... keys) {
        try {
            return (long) redisTemplate.execute(new RedisCallback() {
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    long result = 0;
                    for (int i = 0; i < keys.length; i++) {
                        result = connection.del(keys[i].getBytes());

                    }
                    // TODO ,建议用if logger.IsDebugEnabled，这个地方适合用debug
                    if(logger.isDebugEnabled()){
                    	logger.debug("del {} from redis",keys);
                    }
                    return result;
                }
            });
        } catch (Exception e) {
            logger.error("del {} form redis is error : {}",keys,e);
            return 0;
        }
    }

    /**
     * 添加key value 并且设置存活时间(byte)
     * @param key
     * @param value
     * @param liveTime
     */
    public void set(final byte[] key, final byte[] value, final long liveTime) {
        try {
            redisTemplate.execute(new RedisCallback() {
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.set(key, value);
                    if (liveTime > 0) {
                        connection.expire(key, liveTime);
                    }
                 // TODO ,建议用if logger.IsDebugEnabled，这个地方适合用debug
                    if(logger.isDebugEnabled()){
                    	logger.debug("set key:{},value:{} with liveTime:{} to redis",key,value,liveTime);
                    }
                    return 1L;
                }
            });
        } catch (Exception e) {
            logger.error("set key:{},value:{} with liveTime:{} to redis is error : {}",key,value,liveTime,e);
        }
    }

    /**
     * 添加key value 并且设置存活时间
     * @param key
     * @param value
     * @param liveTime
     */
    public void set(String key, String value, long liveTime) {
        try {
            this.set(key.getBytes(), value.getBytes(), liveTime);
         // TODO ,建议用if logger.IsDebugEnabled，这个地方适合用debug
            if(logger.isDebugEnabled()){
            	logger.debug("set key:{},value:{} with liveTime:{} to redis",key,value,liveTime);
            }
        } catch (Exception e) {
            logger.error("set key:{},value:{} with liveTime:{} to redis is error : {}",key,value,liveTime,e);
        }
    }

    /**
     * 添加key value
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        try {
            this.set(key, value, 0L);
         // TODO ,建议用if logger.IsDebugEnabled，这个地方适合用debug
            if(logger.isDebugEnabled()){
            	logger.debug("set key:{},value:{}  to redis",key,value);
            }
        } catch (Exception e) {
            logger.error("set key:{},value:{} to redis is error : {}",key,value,e);
        }
    }

    /**
     * 添加key value (字节)(序列化)
     * @param key
     * @param value
     */
    public void set(byte[] key, byte[] value) {
        this.set(key, value, 0L);
    }

    /**
     * 获取redis value (String)
     * @param key
     * @return
     */
    public String get(final String key) {
        return (String) redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                	// TODO ,建议用if logger.IsDebugEnabled，这个地方适合用debug
                	if(logger.isDebugEnabled()){
                		logger.debug("get from redis by key:{}",key);
                	}
                    return new String(connection.get(key.getBytes()), redisCode);
                } catch (UnsupportedEncodingException e) {
                   logger.error("get from redis by key{} is erro {}",key,e);
                }
                return "";
            }
        });
    }

    /**
     * 通过正则匹配keys
     * @param pattern
     * @return
     */
    public Set keys(String pattern) {
        return redisTemplate.keys(pattern);

    }

    /**
     *  检查key是否已经存在
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return (boolean) redisTemplate.execute(new RedisCallback() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {

                return connection.exists(key.getBytes());
            }
        });
    }

    /**
     * 清空redis 所有数据
     * @return
     */
    public String flushDB() {
        return (String) redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "ok";
            }
        });
    }

    /**
     * 查看redis里有多少数据
     * @return
     */
    public long dbSize() {
        return (long) redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
    }

    /**
     * 检查是否连接成功
     * @return
     */
    public String ping() {
        return (String) redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {

                return connection.ping();
            }
        });
    }


}

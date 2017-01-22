package com.longbei.appservice.dao;

import com.longbei.appservice.common.utils.LoggerUtil;
import com.longbei.appservice.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.Map;
import java.util.Set;

//@Repository
public class JedisDao {
//	@Autowired
	private JedisPool jedisPool;

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	private Logger logger = LoggerFactory.getLogger(JedisDao.class);

	public void select(final int index) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.select(index);
			value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
		} catch (Exception e) {
			logger.warn("select {} = {}", index, value, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
	}

	public void flushAll() {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.flushAll();
			value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
		} catch (Exception e) {
			logger.warn("flushAll = {}", value, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
	}

	public long del(String key) {
		long value = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.del(key);
		} catch (Exception e) {
			logger.warn("del {}= {}", key, value, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}

	public long hset(final String key, final String field, final String value) {
		long rtnLong = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			rtnLong = jedis.hset(key, field, value);
		} catch (Exception e) {
			logger.warn("hset {} {} = {} {}", key, field, value, rtnLong, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return rtnLong;
	}
	
	public long hsetWithExpire(final String key, final String field, final String value,int expire) {
		long rtnLong = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			rtnLong = jedis.hset(key, field, value);
			jedis.expire(key, expire);
		} catch (Exception e) {
			logger.warn("hset {} {} = {} {}", key, field, value, rtnLong, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return rtnLong;
	}
	
	public long hdel(final String key, final String field) {
		long rtnLong = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			rtnLong = jedis.hdel(key, field);
		} catch (Exception e) {
			logger.warn("hdel {} {} = {} {}", key, field);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return rtnLong;
	}
	
	public long hzudel(final String key) {
		long rtnLong = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			rtnLong = jedis.hdel(key);
		} catch (Exception e) {
			logger.warn("hzudel {} {} = {} {}", key);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return rtnLong;
	}
	
	public String hget(final String key, final String field) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.hexists(key, field)) {
				value = jedis.hget(key, field);
				value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
			}
		} catch (Exception e) {
			logger.warn("hget {} {} = {}", key, field, value, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}

	public Map<String, String> hgetAll(final String key) {
		Map<String, String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.hgetAll(key);
			value = null == value ? null : value;
		} catch (Exception e) {
			logger.warn("hgetAll {} = {}", key, value, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}
	
	//取出map中的"like","flower"字段值
	public List<String> hmget(final String key,String likekey, String flowerkey) {
		List<String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.hmget(key, likekey, flowerkey);
			value = null == value ? null : value;
		} catch (Exception e) {
			logger.warn("hmget {} = {}", key, value, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}
	
	//HashMap存放Key自增  nember
	public long hmhincr(final String key, final String field, final long nember) {
		long value = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (!jedis.exists(key)) {
				value = jedis.hincrBy(key, field, nember);
				//2天过期
				expire(key, 60 * 30);
			}else{
				value = jedis.hincrBy(key, field, nember);
			}
		} catch (Exception e) {
			logger.warn("hmhincr {} {} = {}", key, field, value, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}
	//HashMap存放Key自增  nember
		public boolean exists(final String key) {
			Jedis jedis = null;
			try {
				jedis = getResource();
				if (!jedis.exists(key)) {
					return false;
				}else{
					return true;
				}
			} catch (Exception e) {
				logger.warn("exists key={} and msg={}", key, e);
			}finally{
				if(null != jedis){
					jedis.close();
					jedis = null;
				}
			}
			return false;
		}
	//取出map中的"like"或"flower"字段值
	public List<String> hmlikeget(final String key,String likekey) {
		List<String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.hmget(key, likekey);
			value = null == value ? null : value;
		} catch (Exception e) {
			logger.warn("hmlikeget {} = {}", key, value, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}
	
	//取出map中的好友备注
//	public List<String> hmContactRemarkget(final String key,String likekey) {
//		List<String> value = null;
//		Jedis jedis = null;
//		try {
//			jedis = getResource();
//			value = jedis.hmget(key, likekey);
//			value = null == value ? null : value;
//		} catch (Exception e) {
//			logger.warn("hmlikeget {} = {}", key, value, e);
//		}finally{
//			if(null != jedis){
//				jedis.close();
//				jedis = null;
//			}
//		}
//		return value;
//	}
	
	//存入redis中的好友备注
//	public String hmContactRemarkset(final String key, Map<String, String> map) {
//		String value = null;
//		Jedis jedis = null;
//		try {
//			jedis = getResource();
//			value = jedis.hmset(key, map);
//			expire(key, 60 * 5);
//			value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
//		} catch (Exception e) {
//			logger.warn("hmset {} {} = {}", key, map, value, e);
//		}finally{
//			if(null != jedis){
//				jedis.close();
//				jedis = null;
//			}
//		}
//		return value;
//	}
	
	public String hmset(final String key, Map<String, String> map) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.hmset(key, map);
			value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
		} catch (Exception e) {
			logger.warn("hmset {} {} = {}", key, map, value, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}
	
	public String hmsetWithExpire(final String key, Map<String, String> map,int expire) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.hmset(key, map);
			jedis.expire(key, expire);
			value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
		} catch (Exception e) {
			logger.warn("hmset {} {} = {}", key, map, value, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}
	
	public String setBytesWithExpire(final String key, byte[] bt,int expire) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.set(key.getBytes(), bt);
			jedis.expire(key.getBytes(), expire);
			value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
		} catch (Exception e) {
			logger.warn("hmset {} {} = {}", key.getBytes(), bt, value, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}
	
	public byte[] getBytes(final String key) {
		byte[] value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.get(key.getBytes());
		} catch (Exception e) {
			logger.warn("getBytes {} {} = {}", key, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}
	
	public long hincr(final String key, final String field) {
		long value = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.hexists(key, field)) {
				value = jedis.hincrBy(key, field, 1);
			}
		} catch (Exception e) {
			logger.warn("hincrBy {} {} = {}", key, field, value, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}

	public long hdecr(final String key, final String field) {
		long value = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.hexists(key, field)) {
				value = jedis.hincrBy(key, field, -1);
			}
		} catch (Exception e) {
			logger.warn("hincrBy {} {} = {}", key, field, value, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}

	public long sadd(final String key, final String member) {
		long value = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.sadd(key, member);
		} catch (Exception e) {
			logger.warn("sadd {} = {}", key, member, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}
	
	public String sadd(final String key, final String member,int sencends) {
		String value = null;
		Jedis jedis = null;
		try {
			
			jedis = getResource();
			
			if(sencends>0){
				value = jedis.setex(key, sencends, member);
			}else{
				value = jedis.set(key, member);
			}
		} catch (Exception e) {
			logger.warn("set {} = {}", key, member, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}
	
	
	//返回该menber是否在集合中  在 返回1
	public boolean sismember(final String key, final String member) {
		boolean value = false;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.sismember(key, member);
		} catch (Exception e) {
			logger.warn("sadd {} = {}", key, member, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}
	
	public Set<String> smembers(final String key) {
		Set<String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.smembers(key);
		} catch (Exception e) {
			logger.warn("smembers = {}", key, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}

	public long scard(final String key) {
		long value = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.scard(key);
		} catch (Exception e) {
			logger.warn("scard = {}", key, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}
	
	public long srem(final String key, final String member) {
		long value = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.srem(key, member);
		} catch (Exception e) {
			logger.warn("srem {] = {}", key, member, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}

	public long zadd(final String key, final long score, final String member) {
		long value = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.zadd(key, score, member);
		} catch (Exception e) {
			logger.warn("zadd {} {} = {}", key, member, score, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}

	/**
	 * 从有序集合中删除一个元素
	 */
	public long zrem(final String key, final String field){
		long value = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.zrem(key, field);
		} catch (Exception e) {
			logger.error("jedis错误：" + LoggerUtil.getTrace(e));
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}
	
	public Set<String> zrevrangeByScore(final String key, final int min, final int max, final int offset, final int count) {
		Set<String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.zrevrangeByScore(key, max, min, offset, count);
		} catch (Exception e) {
			logger.warn("zrevrangeByScore {} = {} {} {} {}", key, min, max, offset, count, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}

	public Set<Tuple> zrevrangeWithScores(final String key, final int start, final int end) {
		Set<Tuple> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.zrevrangeWithScores(key, start, end);
		} catch (Exception e) {
			logger.warn("zrevrangeWithScores {} = {} {}", key, start, end, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}

	public Jedis getResource() throws JedisException {
		Jedis jedis = null;
		try {
			jedis = getJedisPool().getResource();
		} catch (JedisException e) {
			logger.warn("getResource.", e);
			throw e;
		}
		return jedis;
	}
	
	//添加一个元素倒链表的低端
	public long rPush(final String key, final String str) {
		long value = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.rpush(key, str);
		} catch (Exception e) {
			logger.warn("rpush {} = {}", key, str, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}
	
	//添加一个元素倒链表的顶端
	public long lPush(final String key, final String str) {
		long value = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.lpush(key, str);
		} catch (Exception e) {
			logger.warn("lpush {} = {}", key, str, e);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}
	
	//添加一个元素倒链表的顶端
	public List<String> lrange(final String key, final long start,final long  end) {
		List<String> list = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			list = jedis.lrange(key, start, end);
		} catch (Exception e) {
			logger.warn("lrange {} = {}{}", key, start,end);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return list;
	}
	
	
	//获取集合中的排名,从小到大
	public long zrank(final String key, final String  member) {
		long value = -1;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.zrank(key, member);
		} catch (Exception e) {
			logger.warn("lrange {} = {}", key,member);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}
	
	
	//获取集合中的排名,从大到小
	public long zrevrank(final String key, final String  member) {
		long value = -1;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.zrevrank(key, member);
		} catch (Exception e) {
			logger.warn("zrevrank {} = {}----error msg = {    }", key,member,LoggerUtil.getTrace(e));
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}
	
	//Zrevrang返回从大到小的范围内的集合
	public Set<String> zrevrange(final String key, final int start, final int end) {
		Set<String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.zrevrange(key, start, end);
		} catch (Exception e) {
			logger.warn("lrange {} = {}{}", key,start,end);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}
	
	//添加key生存周期----单位是秒
	public long expire(String key, int sec){
		long value = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.expire(key, sec);
		} catch (Exception e) {
			logger.warn("expire key={} --- sec={}", key,sec);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}
	
	//set增量 --
	public double zincrby(String key, long sec, String field){
		double value = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.zincrby(key, sec,field);
		} catch (Exception e) {
			logger.warn("zincrby key={} --- sec={}", key,sec);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}
	
	//HINCRBY  map value 自增
	public long hincrby(String key, String field, long sec){
		long value = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = jedis.hincrBy(key, field, sec);
		} catch (Exception e) {
			logger.warn("hincrBy key={} --- sec={}", key,sec);
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return value;
	}
	
	/**
	* @Title: getString
	* @Description: 通过key获取redis中的值
	* @param @param key
	* @param @return 
	* @return String 获取到的值
	* @throws
	 */
	public String getString(String key){
		Jedis jedis = null;
		String result = null;
		try {
			jedis = getResource();
			result = jedis.get(key);
			logger.debug("get key {}",key);
		} catch (Exception e) {
			logger.warn("get key {},{}",key,LoggerUtil.getTrace(e));
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return result;	
	}
	
	/**
	 * 通过key和field获取zset中的分值
	 * @param key
	 * @param field
	 * @return
	 */
	public long zscore(String key,String field){
		Jedis jedis = null;
		Double result = 0.0;
		try {
			jedis = getResource();
			result = jedis.zscore(key, field);
		} catch (Exception e) {
			logger.error("zscore key {},{},{}",key,field,LoggerUtil.getTrace(e));
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return result.longValue();
	}
	
	public long zcard(String key){
		Jedis jedis = null;
		long result = 0;
		try {
			jedis = getResource();
			result = jedis.zcard(key);
		} catch (Exception e) {
			logger.error("zcard key {}，{}",key,LoggerUtil.getTrace(e));
		}finally{
			if(null != jedis){
				jedis.close();
				jedis = null;
			}
		}
		return result;
	}
	
}

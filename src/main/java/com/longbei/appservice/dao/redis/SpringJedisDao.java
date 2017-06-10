package com.longbei.appservice.dao.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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

    //keys 获取map中的所有的map
    public Map<String,String> entries(String key){
        try{
            HashOperations<String, String, String> options = redisTemplate.opsForHash();
            return options.entries(key);
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

    //判断value 是否是 keySet中的元素
    public boolean sIsMember(String key,String value){
        try{
            SetOperations<String,String> setOperations = redisTemplate.opsForSet();
            return setOperations.isMember(key,value);
        }catch(Exception e){
            logger.error("redis set sisMember error",e);
        }
        return true;
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

    /****************************** ZSET start ************************/
    /**
     * 往zset中放入一个value
     * @param key redis的key
     * @param value redis中保存的value
     * @param score value对应的分值
     * @return
     */
    public boolean zAdd(String key,String value,double score){
        try{
            ZSetOperations<String,String> zSetOperations = redisTemplate.opsForZSet();
            return zSetOperations.add(key,value,score);
        }catch(Exception e){
            logger.error("redis zset zadd error e:{}",e);
        }
        return false;
    }


//    public static void main(String[] args) {
//
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
//        System.out.println(dateFormat.format(new Date()));
//        try {
//            System.out.println(dateFormat.parse(dateFormat.format(new Date())).getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
////        // 指定一个日期
////        Date date = dateFormat.parse("2013-6-1 13:24:16");
////
////
////
////        // 初始化 (重置) Calendar 对象
////        Calendar calendar = Calendar.getInstance();
////        // 或者用 Date 来初始化 Calendar 对象
////        calendar.setTime(new Date());
////
////        calendar.add(Calendar.SECOND,0);
//
//
//    }




    /**
     * 从zset中删除一个value
     * @param key redis的key
     * @param value redis的key中要删除的value
     * @return
     */
    public boolean zRem(String key,String... value){
        try{
            ZSetOperations<String,String> zSetOperations = redisTemplate.opsForZSet();
            long row = zSetOperations.remove(key,value);
            return row > 0 ?true:false;
        }catch(Exception e){
            logger.error("redis zset remove error e:{}",e);
        }
        return false;
    }

    /**
     * 返回value在redis的key中的排名 有小到大的排名 分数最小的,排名为1
     * @param key redis中的key
     * @param value
     * @return
     */
    public long zRank(String key,String value){
        try{
            ZSetOperations<String,String> zSetOperations = redisTemplate.opsForZSet();
            return (zSetOperations.rank(key,value)+1);
        }catch(Exception e){
            logger.error("redis zset zRank error e:{}",e);
        }
        return 0;
    }

    /**
     * 返回value在redis的key中的排名,有大到小排名 分数最大的,排名为1
     * @param key redis中的key
     * @param value
     * @return
     */
    public long zRevRank(String key,String value){
        try{
            ZSetOperations<String,String> zSetOperations = redisTemplate.opsForZSet();
            return (zSetOperations.reverseRank(key,value)+1);
        }catch(Exception e){
            logger.error("redis zset zRevRank error e:{}",e);
        }
        return 0;
    }

    /**
     * 返回redis的key中的成员数
     * @param key
     * @return
     */
    public long zCard(String key){
        try{
            ZSetOperations<String,String> zSetOperations = redisTemplate.opsForZSet();
            return zSetOperations.zCard(key);
        }catch (Exception e){
            logger.error("redis zset zCard error e:{}",e);
        }
        return 0;
    }

    /**
     * 在redis的key中对应的value 增加 delta分值
     * @param key
     * @param value
     * @param delta
     * @return
     */
    public double zIncrby(String key,String value,double delta){
        try{
            ZSetOperations<String,String> zSetOperations = redisTemplate.opsForZSet();
            return zSetOperations.incrementScore(key,value,delta);
        }catch (Exception e){
            logger.error("redis zset zCard error e:{}",e);
        }
        return 0;
    }


    public double zIncrby(String key,String value,double delta,Long secend){
        try{
            ZSetOperations<String,String> zSetOperations = redisTemplate.opsForZSet();
            double d = zSetOperations.incrementScore(key,value,delta);
            redisTemplate.expire(key,secend,TimeUnit.SECONDS);
        }catch (Exception e){
            logger.error("redis zset zCard error e:{}",e);
        }
        return 0;
    }

    /**
     * 返回redis的key中从start到end结束的集合 start,end表示的是下标 下标从0开始 返回的数据中包含下标为start和end的值
     * 其中-1代表最后一个成员,-2代表倒数第二个成员 start=0 end=-1 代表返回整个有序集合
     * 注意:该接口返回的是有小到大的排列顺序 如需返回有大到小 请调用 zRevrange 接口
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zRange(String key,long start,long end){
        try{
            ZSetOperations<String,String> zSetOperations = redisTemplate.opsForZSet();
            return zSetOperations.range(key,start,end);
        }catch (Exception e){
            logger.error("redis zset zRange error e:{}",e);
        }
        return new HashSet<String>();
    }

    public Map<String,Double> zRangeWithScores(String key,long start,long end){
        try{
            ZSetOperations<String,String> zSetOperations = redisTemplate.opsForZSet();
            Set<ZSetOperations.TypedTuple<String>> set = zSetOperations.rangeWithScores(key,start,end);
            Map<String,Double> idAndScores = new HashMap<>();
            for (ZSetOperations.TypedTuple<String> z : set){
                idAndScores.put(z.getValue(),z.getScore());
            }
            return idAndScores;
        }catch (Exception e){
            logger.error("redis zset zRange error e:{}",e);
        }
        return new HashMap<>();
    }

    /**
     * 返回redis的key中从start到end结束的集合 start,end表示的是下标 下标从0开始 返回的数据中包含下标为start和end的值
     * 其中-1代表最后一个成员,-2代表倒数第二个成员 start=0 end=-1 代表返回整个有序集合
     * 注意:该接口返回的是有大到小的排列顺序 如需返回有小到大 请调用 zRange 接口
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zRevrange(String key,long start,long end){
        try{
            ZSetOperations<String,String> zSetOperations = redisTemplate.opsForZSet();
            return zSetOperations.reverseRange(key,start,end);
        }catch (Exception e){
            logger.error("redis zset zRange error e:{}",e);
        }
        return new HashSet<String>();
    }

    /**
     * 返回value在key中排名的分数值
     * @param key
     * @param value
     * @return
     */
    public double zScore(String key,String value){
        try{
            ZSetOperations<String,String> zSetOperations = redisTemplate.opsForZSet();
            return zSetOperations.score(key,value);
        }catch(Exception e){
            logger.error("redis zset zscore error e:{}",e);
        }
        return 0;
    }
    /****************************** ZSET end ************************/

    /******************************List start************************/
    public void aTest(){
//        try{
//            ListOperations listOperations = redisTemplate.opsForList();
//            listOperations.
//        }catch (Exception e){
//
//        }
    }
    /******************************List end************************/

}

package com.redis;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

/**
 * @author chengqiu
 *
 */
@Component
public class RedisDataCache {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired 
	RedisConnectionFactory redisConnectionFactory;
	
	RedisTemplate<String, Map<String,String>> redisTemplate;
	
	ValueOperations<String, Map<String,String>> cache;
	
	/**
	 * 
	 */
	
	public RedisTemplate<String, Map<String,String>> getRedisTemplate() {
		if (null == redisTemplate) {
			redisTemplate = new RedisTemplate<>();
			redisTemplate.setConnectionFactory(redisConnectionFactory);
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
			redisTemplate.afterPropertiesSet();
		}
		
		return redisTemplate;
	}
	
	public ValueOperations<String, Map<String,String>> getCache() {
		if (null == cache) {
			cache = getRedisTemplate().opsForValue();
		}
		
		return cache;
	}
	
	public String getCacheKey(String funcPath) {
		String cus = "dev";
		String key = "saas:dp:" + cus + ":" + funcPath;
		return key;
	}

	public Map<String,String> load(String funcPath) throws Exception {
		try {
			String key = getCacheKey(funcPath);
			ValueOperations<String, Map<String,String>> cache = getCache();
			Map<String,String> dpSalesData = cache.get(key);
			return dpSalesData;
		} catch (Exception ex) {
			delete(funcPath);
			throw ex;
		}
	}
	
	public void save(String funcPath, Map<String,String> dpSalesData) {
		String key = getCacheKey(funcPath);
		ValueOperations<String, Map<String,String>> cache = getCache();
		cache.set(key, dpSalesData, 30, TimeUnit.DAYS);
	}
	
	public void delete(String funcPath) {
		getRedisTemplate().delete(getCacheKey(funcPath));
	}
	public static void main(String args[]) {
//		Jedis jedis=new Jedis("192.168.0.20", 6379);
//		jedis.auth("master6379");
//		String a=jedis.get("a");
//		System.out.println(a);
		
		JedisConnectionFactory  redisConnectionFactory =new JedisConnectionFactory(); 
		redisConnectionFactory.setHostName("192.168.0.20");
		redisConnectionFactory.setPort(6379);
		redisConnectionFactory.setPassword("master6379");
		System.out.println(redisConnectionFactory.getShardInfo().createResource().ping());
	}
	
}

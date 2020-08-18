package com.xdx.common.utils.redis;

import com.xdx.common.utils.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
@Component
public class RedisManager implements CacheManager {
	private final Logger logger = LoggerFactory.getLogger(RedisManager.class);

    @Resource
	protected RedisTemplate<Serializable, Object> redisTemplate;

	@Override
	public void init() {
		@SuppressWarnings("unchecked")
        RedisTemplate<Serializable, Object> redisTemplate = (RedisTemplate<Serializable, Object>) SpringContextUtils
				.getBean("redisTemplate");
		if (redisTemplate != null && this.redisTemplate == null) {
			this.redisTemplate = redisTemplate;
		}
	}

	/**
	 * 批量删除对应的value
	 * 
	 * @param keys
	 */
	@Override
	public void remove(final String... keys) {
		for (String key : keys) {
			remove(key);
		}
	}

	/**
	 * 批量删除key(通配符)
	 * 
	 * @param pattern
	 */
	public void removePattern(final String pattern) {
		Set<Serializable> keys = redisTemplate.keys(pattern);
		if (keys.size() > 0){
			redisTemplate.delete(keys);
		}
	}

	/**
	 * 删除对应的value
	 * 
	 * @param key
	 */
	@Override
	public boolean remove(final String key) {
		if (exists(key)) {
			return redisTemplate.delete(key);
		}
		return true;
	}

	/**
	 * 判断缓存中是否有对应的value
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
	}

	/**
	 * 读取缓存
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public Object get(final String key) {
		if (!exists(key)) {
			return null;
		}
		Object result = null;
		ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
		result = operations.get(key);
		return result;
	}

	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public boolean set(final String key, Object value) {
		Integer expire = defaultExpirationTime.intValue() ;
		return set(key, value, expire);
	}

	/**
	 * 写入缓存(可以配置过期时间-单位分钟)
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public boolean set(final String key, Object value, long expireTime) {
		boolean result = false;
		try {
			if (expireTime <= 0) {
				logger.warn("设置缓存时间不能小于0,key:{},value:{}", key, value);
				return result;
			}
			ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			expire(key, expireTime);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("缓存设置失败,key:{},value:{}", key, value);
		}
		return result;
	}

	@Override
	public Boolean expire(String key, final long timeout) {
		return redisTemplate.expire(key, timeout, TimeUnit.MINUTES);
	}

	@Override
	public Boolean expire(String key, final long timeout, final TimeUnit unit) {
		return redisTemplate.expire(key, timeout, unit);
	}

	/**
	 * 获取key有效期
	 * 
	 * @param key
	 * @return
     * @author 小道仙
     * @date 2020年2月26日
	 * @version 1.0
	 */
	@Override
	public Long getExpire(String key) {
		return redisTemplate.getExpire(key) == null ? 0 : redisTemplate.getExpire(key);
	}

	/**
	 * 获取全部的key
	 *
	 * @param key
	 * @return
	 * @author 小道仙
	 * @date 2020年2月26日
	 * @version 1.0
	 */
	@Override
	public Set<Serializable> getKeys() {
        return redisTemplate.keys("*");
	}

	public void setRedisTemplate(RedisTemplate<Serializable, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public RedisTemplate<Serializable, Object> getRedisTemplate() {
		return redisTemplate;
	}
}

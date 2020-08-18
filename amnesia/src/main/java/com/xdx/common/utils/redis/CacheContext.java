package com.xdx.common.utils.redis;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存管理工具
 * @author 小道仙
 * @date 2020年2月26日
 */
public class CacheContext<T extends CacheManager> {

	
	static CacheManager cache;

	/**
	 * 初始化方法
	 *
	 * @author 小道仙
	 * @date 2020年2月26日
	 * @version 1.0
	 */
	@SuppressWarnings("unchecked")
	public void init() {
		Type type = this.getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			Class<T> entityClass;
			Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();
			entityClass = (Class<T>) parameterizedType[0];
			try {
				cache = entityClass.newInstance();
				cache.init();
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 批量删除对应的value
	 * 
	 * @param keys
	 */
	public static void remove(final String... keys) {
		cache.remove(keys);
	}

	/**
	 * 删除对应的value
	 * 
	 * @param key
	 */
	public static boolean remove(final String key) {
		return cache.remove(key);
	}

	/**
	 * 判断缓存中是否有对应的value
	 * 
	 * @param key
	 * @return
	 */
	public static boolean exists(final String key) {
		return cache.exists(key);
	}

	/**
	 * 读取缓存
	 * 
	 * @param key
	 * @return
	 */
	public static Object get(final String key) {
		return cache.get(key);
	}

	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean set(final String key, Object value) {
		return cache.set(key, value);
	}

	/**
	 * 写入缓存(可以配置过期时间-单位分钟)
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean set(final String key, Object value, long expireTime) {
		return cache.set(key, value, expireTime);
	}
	
	/**
	 * 设置缓存时间，默认单位分钟
	 * @param key
	 * @param timeout
	 * @return
	 * @author 小道仙
	 * @date 2020年2月26日
	 * @version 1.0
	 */
	public static  Boolean expire(String key, final long timeout) {
		return cache.expire(key, timeout);
	}
	
	/**
	 * 设置缓存时间
	 * @param key
	 * @param timeout
	 * @return
	 * @author 小道仙
	 * @date 2020年2月26日
	 * @version 1.0
	 */
	public static Boolean expire(String key, final long timeout, final TimeUnit unit) {
		return cache.expire(key, timeout,unit);
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
	public static Long getExpire(String key) {
		return cache.getExpire(key);
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
	public static Set<Serializable> getKeys() {
		return cache.getKeys();
	}
}

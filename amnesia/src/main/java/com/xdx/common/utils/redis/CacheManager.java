package com.xdx.common.utils.redis;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface CacheManager {
	/**
	 * 默认过期时间，单位分钟
	 */
	 Long defaultExpirationTime =  30L;

	/**
	 * 初始化方法
	 * 
	 * @author 小道仙
	 * @date 2020年2月26日
	 * @version 1.0
	 */
	 void init();

	/**
	 * 批量删除对应的value
	 * 
	 * @param keys
	 */
	 void remove(final String... keys);

	/**
	 * 删除对应的value
	 * 
	 * @param key
	 */
	 boolean remove(final String key);

	/**
	 * 判断缓存中是否有对应的value
	 * 
	 * @param key
	 * @return
	 */
	 boolean exists(final String key);

	/**
	 * 读取缓存
	 * 
	 * @param key
	 * @return
	 */
	 Object get(final String key);

	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	 boolean set(final String key, Object value);

	/**
	 * 写入缓存(可以配置过期时间-单位分钟)
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	 boolean set(final String key, Object value, long expireTime);

	/**
	 * 设置缓存时间，默认单位分钟
	 * 
	 * @param key
	 * @param timeout
	 * @return
     * @author 小道仙
     * @date 2020年2月26日
	 * @version 1.0
	 */
	 Boolean expire(String key, final long timeout);

	/**
	 * 设置缓存时间
	 * 
	 * @param key
	 * @param timeout
	 * @return
     * @author 小道仙
     * @date 2020年2月26日
	 * @version 1.0
	 */
	 Boolean expire(String key, final long timeout, final TimeUnit unit);

	/**
	 * 获取key有效期
	 * 
	 * @param key
	 * @return
     * @author 小道仙
     * @date 2020年2月26日
	 * @version 1.0
	 */
	 Long getExpire(String key);

	/**
	 * 获取全部的key
	 *
	 * @return
	 * @author 小道仙
	 * @date 2020年2月26日
	 * @version 1.0
	 */
    Set<Serializable> getKeys();

}

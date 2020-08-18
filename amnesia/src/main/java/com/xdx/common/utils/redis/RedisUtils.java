package com.xdx.common.utils.redis;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * RedisUtils
 *
 * @DependsOn(value = "springContextUtils")
 * 作用是在这之前加载springContextUtils bean
 *
 * @author 小道仙
 * @date 2020年2月26日
 */
@Component
@DependsOn(value = "springContextUtils")
public class RedisUtils extends CacheContext<RedisManager>{
	public RedisUtils() {
		super.init();
	}
}


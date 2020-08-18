package com.xdx.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.Method;
import java.time.Duration;

//继承CachingConfigurerSupport，为了自定义生成KEY的策略。可以不继承。
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Value("${spring.redis.jedis.pool.max-idle}")
    private Integer maxidle;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private Integer minidle;

    @Value("${spring.redis.jedis.pool.max-active}")
    private Integer maxActive;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private Integer maxWait;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private Integer port;

    @Value("${spring.redis.database}")
    private Integer database;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.timeout}")
    private Integer timeout;
	/**
	 * 缓存管理器.
	 * 
	 * @param redisTemplate
	 * @return
	 */
	@Bean
	public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		RedisCacheManager manager = RedisCacheManager.create(connectionFactory);
		return manager;
	}

	/**
	 * JedisPoolConfig 连接池
	 * 
	 * @return
	 */
	@Bean
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		Integer maxidle = this.maxidle;
		Integer minidle = this.minidle;
		Integer maxActive = this.maxActive;
		Integer maxWait = this.maxWait;

		// 最大空闲数
		jedisPoolConfig.setMaxIdle(maxidle);
		jedisPoolConfig.setMinIdle(minidle);
		// 连接池的最大数据库连接数
		jedisPoolConfig.setMaxTotal(maxActive);
		// 最大建立连接等待时间
		jedisPoolConfig.setMaxWaitMillis(maxWait);
		// Idle时进行连接扫描
		jedisPoolConfig.setTestWhileIdle(true);
		// 表示idle object evitor两次扫描之间要sleep的毫秒数
		jedisPoolConfig.setTimeBetweenEvictionRunsMillis(30000);
		// 表示idle object evitor每次扫描的最多的对象数
		jedisPoolConfig.setNumTestsPerEvictionRun(10);
		// 表示一个对象至少停留在idle状态的最短时间，然后才能被idle object
		// evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
		jedisPoolConfig.setMinEvictableIdleTimeMillis(60000);
		// 在borrow一个jedis实例时，是否提前进行alidate操作
		jedisPoolConfig.setTestOnBorrow(true);
		// 在return给pool时，是否提前进行validate操作
		jedisPoolConfig.setTestOnReturn(true);
		return jedisPoolConfig;
	}

	@Bean
	public JedisConnectionFactory connectionFactory(JedisPoolConfig jedisPoolConfig) {
		String host = this.host;
		Integer port = this.port;
		Integer database = this.database;
		String password = this.password;
		Integer timeout = this.timeout;
		RedisStandaloneConfiguration rf = new RedisStandaloneConfiguration();
		rf.setHostName(host);
		rf.setPort(port);
		rf.setDatabase(database);
		rf.setPassword(RedisPassword.of(password));
		JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration
				.builder();
		JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpb = jedisClientConfiguration
				.usePooling();
		jedisClientConfiguration.connectTimeout(Duration.ofMillis(timeout));// connection timeout
		// 连接池
		jpb.poolConfig(jedisPoolConfig);

		JedisConnectionFactory factory = new JedisConnectionFactory(rf,
				jedisClientConfiguration.build());
		return factory;
	}

	/**
	 * 注解@Cache key生成规则
	 */
	@Bean
	@Override
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuilder sb = new StringBuilder();
				sb.append(target.getClass().getName());
				sb.append(method.getName());
				for (Object obj : params) {
					sb.append(obj.toString());
				} 
				return sb.toString();
			}
		};
	}

	// 设置数据存入 redis 的序列化方式
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
		// 创建一个模板类
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		// 将redis连接工厂设置到模板类中
		template.setConnectionFactory(factory);
		// 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
		Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);

		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		serializer.setObjectMapper(mapper);
		// 开启事务
//		template.setEnableTransactionSupport(true);
		template.setValueSerializer(serializer);
		// 使用StringRedisSerializer来序列化和反序列化redis的key值
		template.setKeySerializer(new StringRedisSerializer());
		
		template.afterPropertiesSet(); 
		return template;
	}
}

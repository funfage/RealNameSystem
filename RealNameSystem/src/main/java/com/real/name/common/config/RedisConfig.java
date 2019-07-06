package com.real.name.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableAutoConfiguration
public class RedisConfig extends CachingConfigurerSupport {

    //设置最大连接数
    @Value("${redis.pool.maxTotal}")
    private Integer maxTotal;

    //设置最大能够保持闲置状态的对象数
    @Value("${redis.pool.maxIdle}")
    private Integer maxIdle;

    //设置最小能够保持闲置状态的对象数
    @Value("${redis.pool.minIdle}")
    private Integer minIdle;

    //当池内对象没有返回时，最大的等待时间
    @Value("${redis.pool.maxWaitMillis}")
    private Integer maxWaitMillis;

    //当调用borrow Object方法时，是否进行有效性检查
    @Value("${redis.pool.testOnBorrow}")
    private boolean testOnBorrow;

    //当调用return Object方法时，是否进行有效性检查
    @Value("${redis.pool.testOnReturn}")
    private boolean testOnReturn;

    //向调用者输出“链接”对象时，是否检测它的空闲超时；
    @Value("${redis.pool.testWhileIdle}")
    private boolean testWhileIdle;

    //对于“空闲链接”检测线程而言，每次检测的链接资源的个数。默认为3.
    @Value("${redis.pool.numTestsPerEvictionRun}")
    private Integer numTestsPerEvictionRun;

    //redis服务器的ip
    @Value("${redis.hostName}")
    private String hostName;

    //redis服务器的Port
    @Value("${redis.port}")
    private Integer port;

    @Value("${redis.database}")
    private Integer database;

    @Value("${redis.password}")
    private String password;


    @Bean
    public JedisPoolConfig getPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setTestWhileIdle(testWhileIdle);
        config.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        return config;
    }

    @Bean
    public RedisStandaloneConfiguration jedisConfig() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setDatabase(database);
        config.setHostName(hostName);
        config.setPort(port);
        if(!StringUtils.isEmpty(password)){
            config.setPassword(password);
        }
        return config;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(JedisPoolConfig jedisPool, RedisStandaloneConfiguration jedisConfig) {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(jedisConfig);
        connectionFactory.setPoolConfig(jedisPool);
        return connectionFactory;
    }

    /**
     * 配置RedisTemplate
     * 设置添加序列化器
     * key 使用string序列化器
     * value 使用Json序列化器
     * 还有一种简答的设置方式，改变defaultSerializer对象的实现。
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        //StringRedisTemplate的构造方法中默认设置了stringSerializer
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        //使用StringRedisSerializer来序列化和反序列化redis的key值
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(stringRedisSerializer);

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        //set value serializer
        template.setDefaultSerializer(jackson2JsonRedisSerializer);

        template.setConnectionFactory(redisConnectionFactory);
        template.afterPropertiesSet();
        return template;
    }

}

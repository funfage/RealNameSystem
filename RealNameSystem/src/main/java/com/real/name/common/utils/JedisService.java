package com.real.name.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class JedisService {

    Logger logger = LoggerFactory.getLogger(JedisService.class);

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public JedisKeys jedisKeys;

    public JedisStrings jedisStrings;


    @Component(value = "jedisKeys")
    public class JedisKeys{

        /**
         * 删除key对应的记录
         */
        public boolean del(String key){
            try {
                return redisTemplate.delete(key);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                return false;
            }
        }

        /**
         * 批量删除一key开头的键值
         */
        public void delByPrex(String key){
            Set<String> keys = redisTemplate.keys(key + "*");
            if (!CollectionUtils.isEmpty(keys)) {
                redisTemplate.delete(keys);
            }
        }

        /**
         * 查询是否存在某个键
         */
        public boolean hasKey(String key){
            try {
                return redisTemplate.hasKey(key);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                return false;
            }
        }

        /**
         * 设置key的过期时间
         */
        public boolean expire(String key, long timeOut, TimeUnit timeUnit){
            return redisTemplate.expire(key, timeOut, timeUnit);
        }

        /**
         * 修改key的名称如何存在
         */
        public boolean renameIfAbsent(String oldKey, String newKey){
            return redisTemplate.renameIfAbsent(oldKey, newKey);
        }

        /**
         * 模糊查询
         */
        public Set<String> keys(String pattern) {
            return redisTemplate.keys(pattern);
        }

    }

    @Component(value = "jedisStrings")
    public class JedisStrings{
        /**
         * 设置key的value
         */
        public void set(String key, Object value){
            ValueOperations<String,Object> vo = redisTemplate.opsForValue();
            vo.set(key, value);
        }

        /**
         * 设置key的value以及过期时间
         */
        public void set(String key, Object value, long timeOut, TimeUnit timeUnit){
            ValueOperations<String,Object> vo = redisTemplate.opsForValue();
            vo.set(key, value, timeOut, timeUnit);
        }

        /**
         * 设置key的value, 如果这个key不存在的话
         */
        public void setIfAbsent(String key, Object value){
            ValueOperations<String,Object> vo = redisTemplate.opsForValue();
            vo.setIfAbsent(key, value);
        }

        /**
         * 设置key的value以及过期时间, 如果这个key不存在的话
         */
        public void setIfAbsent(String key, Object value, long timeOut, TimeUnit timeUnit){
            ValueOperations<String,Object> vo = redisTemplate.opsForValue();
            vo.setIfAbsent(key, value, timeOut, timeUnit);
        }

        /**
         * 设置key的value, 如果这个key存在的话
         */
        public void setIfPresent(String key, Object value){
            ValueOperations<String,Object> vo = redisTemplate.opsForValue();
            vo.setIfPresent(key, value);
        }

        /**
         * 设置key的value以及过期时间, 如果这个key存在的话
         */
        public void setIfPresent(String key, Object value, long timeOut, TimeUnit timeUnit){
            ValueOperations<String,Object> vo = redisTemplate.opsForValue();
            vo.setIfPresent(key, value, timeOut, timeUnit);
        }

        /**
         * 获取key的值
         */
        public Object get(String key){
            ValueOperations<String,Object> vo = redisTemplate.opsForValue();
            Object value = vo.get(key);
            try {
                if(value != null){
                    return vo.get(key);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
            return null;
        }

        public List<Object> multiGet(Set<String> keys) {
            return redisTemplate.opsForValue().multiGet(keys);
        }

    }


}

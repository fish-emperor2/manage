package com.zime.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    @Autowired
     RedisTemplate<String,String> redisTemplate;
    public void setString(String key,String value){
        redisTemplate.opsForValue().set(key,value);
    }
    public void update(String key,String value){
        redisTemplate.opsForValue().set(key,value);
        System.out.println("Updated key: " + key + ", New value: " + value);
    }
    public long getStringlength(String key){
        return redisTemplate.opsForValue().size(key);
    }
    public String getvalue(String key){
        return redisTemplate.opsForValue().get(key);
    }
    public void delete(String key){
        redisTemplate.delete(key);
    }

}

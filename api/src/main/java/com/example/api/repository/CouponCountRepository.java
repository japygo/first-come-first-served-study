package com.example.api.repository;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CouponCountRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public CouponCountRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Long increment() {
        return redisTemplate
            .opsForValue()
            .increment("coupon_count");
    }

    public void flushAll() {
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        if (factory == null) {
            return;
        }
        RedisConnection connection = factory.getConnection();
        RedisServerCommands commands = connection.commands();
        commands.flushAll();
    }

}

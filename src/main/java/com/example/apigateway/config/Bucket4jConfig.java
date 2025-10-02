package com.example.apigateway.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Bucket4jConfig {

    @Value("${bucket4j-rate-limiter.capacity}")
    private int capacity;
    
    @Value("${bucket4j-rate-limiter.refill-tokens}")
    private int refillTokens;
    
    @Value("${bucket4j-rate-limiter.refill-duration-seconds}")
    private int refillDurationSeconds;
    
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String key) {
        return cache.computeIfAbsent(key, k -> createNewBucket());
    }

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth
                .builder()
                .capacity(capacity)
                .refillGreedy(refillTokens, Duration.ofSeconds(refillDurationSeconds))
                .build();
        return Bucket.builder().addLimit(limit).build();
    }
}
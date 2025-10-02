package com.example.apigateway.filter;

import com.example.apigateway.config.Bucket4jConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class Bucket4jGatewayFilter implements WebFilter {

    private static final Logger log = LoggerFactory.getLogger(Bucket4jGatewayFilter.class);
    private final Bucket4jConfig bucketConfig;

    public Bucket4jGatewayFilter(Bucket4jConfig bucketConfig) {
        this.bucketConfig = bucketConfig;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        System.out.println("Bucket4jGatewayFilter applied");
        String key = Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress();
        var bucket = bucketConfig.resolveBucket(key);

        if (bucket.tryConsume(1)) {
            return chain.filter(exchange);
        } else {
            log.error("Too many requests - Rate limit exceeded for IP: {}", key);
            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }
    }
}

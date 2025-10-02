package com.example.apigateway.config;

import com.example.apigateway.filter.Bucket4jGatewayFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GatewayConfig {


    @Value("${student-service.uri}")
    private String studentUri;

    @Value("${student-service.prefix}")
    private String studentPrefix;

    @Value("${student-service.path}")
    private String studentPath;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("student-service", r -> r.path(studentPath)
                        .filters(f -> f
                                .prefixPath(studentPrefix)
                        )
                        .uri(studentUri))
                .build();
    }
}

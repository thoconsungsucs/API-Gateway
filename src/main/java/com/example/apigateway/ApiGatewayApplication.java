package com.example.apigateway;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ApiGatewayApplication {

    private static SecretKey getSigningKey() {
        byte[] bytes = Decoders.BASE64.decode("O1nXf6OaRCln2AtB9nFZ4wZr1Nf7P7L7O8U7vZ/sa3g=123456");
        return Keys.hmacShaKeyFor(bytes);
    }

    public static void main(String[] args) {

        long currentTimeMillis = System.currentTimeMillis();

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", new String[]{"USER", "ADMIN"});

        String token = Jwts.builder()
                .setClaims(extraClaims)
                .setSubject("thanhnd") // username
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis + 3600 * 1000)) // 1h
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        System.out.println("Generated JWT: " + token);
        SpringApplication.run(ApiGatewayApplication.class, args);
        System.out.println("API Gateway is running...");
    }
 
}

package com.example.rentCar.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;

@Configuration
public class SecurityJwtConfiguration {
    public final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;
    @Value("${thanh.jwt.base64-secret}")
    private String SecretKeyJWT;

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(SecretKeyJWT).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, this.JWT_ALGORITHM.getName());
    }

    @Bean
    public JwtEncoder jwtEncode() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(this.getSecretKey()));
    }
}

package com.example.rentCar.utils.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtils {
    private final JwtEncoder jwtEncoder;
    public final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;
    @Value("${thanh.jwt.token-validity-in-seconds}")
    private long validityJwt;

    public SecurityUtils(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateJwt() {
        Instant now = Instant.now();
        Instant validity = now.plus(this.validityJwt, ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("iss")
                .issuedAt(now)
                .expiresAt(validity)
                .subject("auth.getName()")
                .claim("user", "abc")
                .build();
        JwsHeader header = JwsHeader.with(this.JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

}

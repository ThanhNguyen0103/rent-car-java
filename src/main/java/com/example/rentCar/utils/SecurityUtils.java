package com.example.rentCar.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import com.example.rentCar.domain.res.ResLogin;
import com.example.rentCar.utils.constant.TokenType;
import com.example.rentCar.utils.error.InvalidException;

@Service
public class SecurityUtils {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    public final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;
    @Value("${thanh.jwt.accessToken-validity-in-seconds}")
    private long expiresAccesToken;
    @Value("${thanh.jwt.refreshToken-validity-in-seconds}")
    private long expiresRefreshToken;
    @Value("${thanh.jwt.base64-secret}")
    private String secretKeyJwt;

    public SecurityUtils(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateJwt(ResLogin.ResUserLogin user, TokenType type) {
        Instant now = Instant.now();
        Instant validity = (type == TokenType.ACCESS) ? now.plus(this.expiresAccesToken, ChronoUnit.SECONDS)
                : now.plus(this.expiresRefreshToken, ChronoUnit.SECONDS);

        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(user.getEmail());
        if (type == TokenType.ACCESS) {
            claimsBuilder.claim("user", user);
        } else {
            claimsBuilder.claim("type", "refresh");
        }

        JwtClaimsSet claims = claimsBuilder.build();
        JwsHeader header = JwsHeader.with(this.JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {

        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    public String extractSubjectFromValidRefreshToken(String refreshToken) {
        try {
            Jwt jwt = this.jwtDecoder.decode(refreshToken);
            String type = jwt.getClaim("type");
            if (!"refresh".equals(type)) {
                throw new JwtException("Token is not a refresh token");
            }
            return jwt.getSubject();
        } catch (Exception e) {
            throw new InvalidException("refreshToken Error");
        }
    }

}

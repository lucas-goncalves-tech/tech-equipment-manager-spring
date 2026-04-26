package io.github.lucas_goncalves_tech.tech_equipment_manager.core.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.lucas_goncalves_tech.tech_equipment_manager.user.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtService {

    private final String issuer = "tech-manager";

    @Value("${jwt.access.secret}")
    private String accessSecret;

    @Value("${jwt.refresh.secret}")
    private String refreshSecret;

    @Value("${jwt.access.expires-in-minutes}")
    private int ACCESS_EXPIRES_IN_MINUTES;

    @Value("${jwt.refresh.expires-in-days}")
    private int REFRESH_EXPIRES_IN_DAYS;

    private Algorithm getAccessAlgorithm() {
        return Algorithm.HMAC256(accessSecret);
    }

    private Algorithm getRefreshAlgorithm() {
        return Algorithm.HMAC256(refreshSecret);
    }

    public String generateAccessToken(User user) {
        final Instant now = Instant.now();
        return JWT.create()
                .withSubject(user.getId().toString())
                .withIssuer(issuer)
                .withClaim("type", TokenType.ACCESS.name())
                .withClaim("role", user.getRole().getAuthority())
                .withIssuedAt(now)
                .withExpiresAt(now.plus(ACCESS_EXPIRES_IN_MINUTES, ChronoUnit.MINUTES))
                .sign(getAccessAlgorithm());
    }

    public String generateRefreshToken(User user) {
        final Instant now = Instant.now();
        return JWT.create()
                .withSubject(user.getId().toString())
                .withIssuer(issuer)
                .withClaim("type", TokenType.REFRESH.name())
                .withClaim("tokenVersion", user.getTokenVersion())
                .withIssuedAt(now)
                .withExpiresAt(now.plus(REFRESH_EXPIRES_IN_DAYS, ChronoUnit.DAYS))
                .sign(getRefreshAlgorithm());
    }

    public DecodedJWT validateAccessToken(String token) {
        return JWT.require(getAccessAlgorithm())
                .withIssuer(issuer)
                .withClaim("type", TokenType.ACCESS.name())
                .build()
                .verify(token);
    }
}

package io.github.lucas_goncalves_tech.tech_equipment_manager.core.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.github.lucas_goncalves_tech.tech_equipment_manager.user.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtService {

    private final String issuer = "tech-manager";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-expires-in-minutes}")
    private int EXPIRES_ACCESS_IN_MINUTES;
    
    @Value("${jwt.refresh-expires-in-days}")
    private int EXPIRES_REFRESH_IN_DAYS;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

    public String generateAccessToken(User user) {
        final Instant now = Instant.now();
        return JWT.create()
                .withSubject(user.getId().toString())
                .withIssuer(issuer)
                .withClaim("type", TokenType.ACCESS.name())
                .withClaim("role", user.getRole().name())
                .withIssuedAt(now)
                .withExpiresAt(now.plus(EXPIRES_ACCESS_IN_MINUTES, ChronoUnit.MINUTES))
                .sign(getAlgorithm());
    }

    public String generateRefreshToken(User user) {
        final Instant now = Instant.now();
        return JWT.create()
                .withSubject(user.getId().toString())
                .withIssuer(issuer)
                .withClaim("type", TokenType.REFRESH.name())
                .withClaim("tokenVersion", user.getTokenVersion())
                .withIssuedAt(now)
                .withExpiresAt(now.plus(EXPIRES_REFRESH_IN_DAYS, ChronoUnit.DAYS))
                .sign(getAlgorithm());
    }
}

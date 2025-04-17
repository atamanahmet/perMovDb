package com.permovdb.permovdb.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.permovdb.permovdb.domain.User;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET = "Secret"; // change
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours

    private final Algorithm algorithm = Algorithm.HMAC256(SECRET);

    public String generateToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .sign(algorithm);
    }

    public String extractUsername(String token) {
        return decodeToken(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        DecodedJWT jwt = decodeToken(token);
        return jwt.getExpiresAt().after(new Date());
    }

    private DecodedJWT decodeToken(String token) {
        return JWT.require(algorithm)
                .build()
                .verify(token);
    }
}

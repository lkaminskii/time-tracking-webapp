package dev.lucas.time_tracking.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(Authentication authentication, boolean rememberMe) {
        if (authentication == null) {
            log.error("Authentication object is null");
            throw new IllegalArgumentException("Authentication cannot be null");
        }

        Object principal = authentication.getPrincipal();
        if (principal == null) {
            log.error("Authentication principal is null");
            throw new IllegalStateException("Authentication principal cannot be null");
        }

        if (!(principal instanceof UserPrincipal)) {
            log.error("Unexpected principal type: {}", principal.getClass().getName());
            throw new IllegalStateException("Unexpected principal type");
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() +
                (rememberMe ? jwtExpiration * 7 : jwtExpiration)); // 7 days if remember me

        return Jwts.builder()
                .setSubject(userPrincipal.getCpf())
                .claim("name", userPrincipal.getName())
                .claim("role", userPrincipal.getRole())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getCpfFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJwt(token);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        }
        return false;
    }
}

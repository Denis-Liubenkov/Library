package com.example.authenticationms.security;

import com.example.authenticationms.security.domain.UserClaims;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateJwtToken(UserClaims userClaims) {
        return Jwts.builder()
                .subject(userClaims.getLogin())
                .claim("userId", userClaims.getUserId())
                .expiration(new Date(new Date().getTime() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).build().parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("Expired Jwt token: " + e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported Jwt token: " + e);
        } catch (IllegalArgumentException e) {
            log.info("Illegal arguments: " + e);
        }
        return false;
    }

    public String getTokenFromHttpRequest(HttpServletRequest request) {
        final String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String removeBearerPrefix(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }

    public UserClaims getUserClaimsFromJwt(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .build()
                    .parseSignedClaims(token)
                    .getBody();
            String login = claims.getSubject();
            Long userId = claims.get("userId", Long.class);
            return new UserClaims(login, userId);
        } catch (ExpiredJwtException e) {
            log.info("Token is not expired " + e.getMessage());
            return null;
        } catch (MalformedJwtException e) {
            log.info("Token format is not valid " + e.getMessage());
            return null;
        } catch (SignatureException e) {
            log.info("Can't take userId from jwt due to signature exception: " + e.getMessage());
            return null;
        } catch (Exception e) {
            log.info("Can't take userId from jwt: " + e.getMessage());
            return null;
        }
    }

    public String getLoginFromJwt(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            log.info("Can`t take login from jwt: " + e);
        }
        return null;
    }
}



package com.graduation.parrot.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JwtUtil {

    public String createToken(Long id, String login_id) {
        String token = JWT.create()
                .withSubject("parrot")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", id)
                .withClaim("login_id", login_id)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        return token;
    }

    public Optional<String> verify(String token) {
        try {
            DecodedJWT decode = JWT.require(Algorithm.HMAC512("cos")).build().verify(token);
            return Optional.ofNullable(decode.getClaim("login_id").asString());
        }catch (JWTVerificationException e) {
            log.warn(String.valueOf(e));
        }
        return Optional.empty();
    }

}

package com.graduation.parrot.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JwtTokenTest {

    static void printClaim(String key, Claim value){
        if(value.isNull()){
            System.out.printf("%s:%s\n", key, "none");
            return;
        }
        if(value.asString() != null){
            System.out.printf("%s:{str}%s\n", key, value.asString());
            return;
        }
        if(value.asLong() != null){
            System.out.printf("%s:{lng}%d\n", key, value.asLong());
            return;
        }
        if(value.asInt() != null ){
            System.out.printf("%s:{int}%d\n", key, value.asInt());
            return;
        }
        if(value.asBoolean() != null){
            System.out.printf("%s:{bol}%b\n", key, value.asBoolean());
            return;
        }
        if(value.asDate() != null){
            System.out.printf("%s:{dte}%s\n", key, value.asDate().toString());
            return;
        }
        if(value.asDouble() != null){
            System.out.printf("%s:{dbl}%f\n", key, value.asDouble());
            return;
        }
        String[] values = value.asArray(String.class);
        if(values != null){
            System.out.printf("%s:{arr}%s\n", key, Stream.of(values).collect(Collectors.joining(",")));
            return;
        }
        Map valueMap = value.asMap();
        if(valueMap != null) {
            System.out.printf("%s:{map}%s\n", key, valueMap);
            return;
        }
        System.out.println("====>> unknown type for :"+key);
    }

    @Test
    void TokenExpiredTest() throws InterruptedException {

        Algorithm algorithm = Algorithm.HMAC512("hello");
        String token = JWT.create()
                .withSubject("cos")
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000))
                .sign(algorithm);
        System.out.println("token = " + token);

        DecodedJWT decodedToken = JWT.require(algorithm).build().verify(token);
        printClaim("typ", decodedToken.getHeaderClaim("typ"));
        printClaim("alg", decodedToken.getHeaderClaim("alg"));
        System.out.println("======");
        decodedToken.getClaims().forEach(JwtTokenTest::printClaim);

        Thread.sleep(2000);
        assertThrows(TokenExpiredException.class, ()->{
            JWT.require(algorithm).build().verify(token);
        });

    }


}

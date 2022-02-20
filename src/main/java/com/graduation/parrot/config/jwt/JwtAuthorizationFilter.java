package com.graduation.parrot.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.graduation.parrot.config.auth.PrincipalDetails;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(JwtProperties.AUTHORIZATION);

        //브라우저 접근이면
        if(token == null){
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
                    Cookie d = cookies[i];
                    String cName = d.getName();
                    if (cName.equals("jwtToken")) {
                        token = "Bearer " + d.getValue();
                    }
                }
            }
        }

        if (token == null || !token.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        token = token.replace(JwtProperties.TOKEN_PREFIX, "");

        try {
            DecodedJWT cos = JWT.require(Algorithm.HMAC512("cos")).build().verify(token);
            String login_id = cos.getClaim("login_id").asString();

            if (login_id != null) {
                Optional<User> user = userRepository.findByLogin_id(login_id);

                PrincipalDetails principalDetails = new PrincipalDetails(user.get());
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        principalDetails, //나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
                        null, // 패스워드는 모르니까 null 처리, 어차피 지금 인증하는게 아니니까!!
                        principalDetails.getAuthorities());

                // 강제로 시큐리티의 세션에 접근하여 값 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (TokenExpiredException e) {
            //토큰 기한 만료
            logger.warn(e);
        } catch (SignatureVerificationException e) {
            //아이디 비번 잘못됨
            logger.warn(e);
            expireCookie(response, "jwtToken");
        } catch (JWTDecodeException e) {
            //토큰이 이상함 The token was expected to have 3 parts, but got 1
            //The string doesn't have a valid JSON format.
            logger.warn(e);
            expireCookie(response, "jwtToken");
        }

        chain.doFilter(request, response);
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}

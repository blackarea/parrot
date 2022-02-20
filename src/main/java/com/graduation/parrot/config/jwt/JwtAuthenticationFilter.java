package com.graduation.parrot.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduation.parrot.config.auth.PrincipalDetails;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.form.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // Authentication 객체 만들어서 리턴 => 의존 : AuthenticationManager
    // 인증 요청시에 실행되는 함수 => /login
    @SneakyThrows(IOException.class)
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        ObjectMapper om = new ObjectMapper();
        LoginRequestDto loginRequestDto = null;
        String login_id = null;
        String password = null;

        String parameterLogin_id = request.getParameter("login_id");
        String parameterPassword = request.getParameter("password");

        if (parameterLogin_id == null) {
            try {
                loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            login_id = loginRequestDto.getLogin_id();
            password = loginRequestDto.getPassword();
        } else {
            login_id = parameterLogin_id;
            password = parameterPassword;
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(login_id, password);

        //PrincipalDetailsService.loadUserByUsername 실행
        //authentication은 session에 저장됨

        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return authentication;
        }catch (BadCredentialsException e){
            logger.warn(e);
            response.sendRedirect("/userlogin");
        }

        return null;
    }

    // JWT Token 생성해서 response에 담아주기
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject("cos")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("login_id", principalDetails.getUser().getLogin_id())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        response.addHeader("Authorization", "Bearer " + jwtToken);
        Cookie cookie = new Cookie("jwtToken", jwtToken);
        response.addCookie(cookie);
        response.sendRedirect("/");
    }

}

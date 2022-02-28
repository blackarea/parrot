package com.graduation.parrot.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduation.parrot.config.auth.PrincipalDetails;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.LoginRequestDto;
import org.springframework.security.authentication.AuthenticationManager;
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

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private boolean isApp = false;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        ObjectMapper om = new ObjectMapper();
        LoginRequestDto loginRequestDto = null;
        String login_id;
        String password;

        String parameterLogin_id = request.getParameter("login_id");
        String parameterPassword = request.getParameter("password");
        if(parameterLogin_id == null){
            isApp = true;
        }

        if (isApp) {
            try {
                loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            login_id = loginRequestDto == null ? null : loginRequestDto.getLogin_id();
            password = loginRequestDto == null ? null : loginRequestDto.getPassword();
        } else {
            login_id = parameterLogin_id;
            password = parameterPassword;
        }
        System.out.println("login_id = " + login_id);
        System.out.println("password = " + password);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(login_id, password);

        //PrincipalDetailsService.loadUserByUsername 실행
        //authentication은 session에 저장됨
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        User user = principalDetails.getUser();

        String jwtToken = jwtUtil.createToken(user.getId(), user.getLogin_id());

        if(isApp){
            response.addHeader(JwtProperties.AUTHORIZATION, JwtProperties.TOKEN_PREFIX + jwtToken);
        }else {
            Cookie cookie = new Cookie("jwtToken", jwtToken);
            cookie.setMaxAge(60 * 30); // 60초 * 30
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            response.sendRedirect("/");
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        logger.warn(failed.getMessage());
        if (!isApp){
            response.sendRedirect("/userlogin");
        }
    }
}

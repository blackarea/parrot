package com.graduation.parrot.config.jwt;

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
    private final JwtUtil jwtUtil;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, JwtUtil jwtUtil) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(JwtProperties.AUTHORIZATION);

        //브라우저 접근이면
        if (token == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
                    Cookie jwtToken = cookies[i];
                    String cName = jwtToken.getName();
                    if (cName.equals("jwtToken")) {
                        token = "Bearer " + jwtToken.getValue();
                    }
                }
            }
        }

        if (token == null || !token.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        token = token.replace(JwtProperties.TOKEN_PREFIX, "");
        Optional<String> tokenOptional = jwtUtil.verify(token);

        if (tokenOptional.isPresent()) {
            Optional<User> user = userRepository.findByLogin_idOptional(tokenOptional.get());
            PrincipalDetails principalDetails = new PrincipalDetails(user.get());
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    principalDetails, //나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
                    null,
                    principalDetails.getAuthorities());

            // 강제로 시큐리티의 세션에 접근하여 값 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            expireCookie(response, "jwtToken");
        }

        chain.doFilter(request, response);
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}

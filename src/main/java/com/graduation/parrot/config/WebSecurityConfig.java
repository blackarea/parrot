package com.graduation.parrot.config;

import com.graduation.parrot.config.jwt.JwtAuthenticationFilter;
import com.graduation.parrot.filter.MyFilter1;
import com.graduation.parrot.filter.MyFilter3;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    //private final BoardUserDetailsService boardUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                //.addFilterBefore(new MyFilter3(), BasicAuthenticationFilter.class)
                .addFilter(corsFilter)
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))

                .authorizeRequests()
                .antMatchers("/", "/css/**", "/js/**", "/security/**").permitAll()
                .antMatchers("/api/v1/user/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();
                //.and()

                //.logout()
                //.logoutUrl("/security/logout").logoutSuccessUrl("/").invalidateHttpSession(true);
                //.and()

                //.userDetailsService(boardUserDetailsService);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

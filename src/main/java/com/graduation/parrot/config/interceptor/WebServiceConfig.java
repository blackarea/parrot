package com.graduation.parrot.config.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebServiceConfig implements WebMvcConfigurer {

    private final BoardAuthInterceptor boardAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(boardAuthInterceptor)
                .addPathPatterns("/board/update/**")
                .addPathPatterns("/board/delete/**");
    }
}

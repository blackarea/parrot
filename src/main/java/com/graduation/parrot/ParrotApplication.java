package com.graduation.parrot;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.thymeleaf.spring5.SpringTemplateEngine;

@EnableJpaAuditing
@SpringBootApplication
public class ParrotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParrotApplication.class, args);
    }

    
}

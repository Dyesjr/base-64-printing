package com.print.printing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public String targetFolder() {
        return "${pdf.file.path}";
    }
}

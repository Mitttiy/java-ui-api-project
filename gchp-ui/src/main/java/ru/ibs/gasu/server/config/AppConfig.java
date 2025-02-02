package ru.ibs.gasu.server.config;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public DozerBeanMapper dozer() {
        return new DozerBeanMapper();
    }
}

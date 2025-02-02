package ru.ibs.gasu.configuation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import ru.ibs.gasu.gchp.user.UserAudit;

@Configuration
@EnableJpaAuditing
public class DBConfig {
    @Bean
    AuditorAware<Long> auditorProvider() {
        return new UserAudit();
    }
}

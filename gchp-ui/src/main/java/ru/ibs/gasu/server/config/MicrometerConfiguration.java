package ru.ibs.gasu.server.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.spring.autoconfigure.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//For further configure the registry, such as applying common tags, before any meters are registered with the registry
@Configuration
public class MicrometerConfiguration {

    @Bean
    MeterRegistryCustomizer meterRegistryCustomizer(MeterRegistry meterRegistry){
        return meterRegistry1 -> {
            meterRegistry.config().commonTags("application", "micrometer-with-spring-boot-1x");
        };
    }

}

package ru.ibs.gasu.configuation;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@Profile("dev-old")
public class DBConfigOld {

    @Bean
    @ConfigurationProperties(prefix = "application.datasource.db1")
    public DataSourceProperties data1SourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "application.datasource.db2")
    public DataSourceProperties data2SourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource dataSource1() {
        return data1SourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    public DataSource dataSource2() {
        return data2SourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource1());
    }

    @Bean
    public JdbcTemplate jdbcTemplateSecSSO() {
        return new JdbcTemplate(dataSource2());
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource1());
    }
}

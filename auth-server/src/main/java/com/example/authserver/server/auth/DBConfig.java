package com.example.authserver.server.auth;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @Auther: 长安
 */
@Slf4j
@Configuration
public class DBConfig {

    /*
        datasource:
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbcUrl
     */
    @Value("${spring.datasource.url}")
    private String url;


    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource() {
        log.info("==============> {}", url);
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean("customJdbcTemplate")
    public JdbcTemplate jdbcTemplate(DataSource dataSource) throws SQLException {
        log.debug("[DEBUG] datasource -> {}", dataSource.getConnection().getClientInfo());
        return new JdbcTemplate(dataSource);
    }
}

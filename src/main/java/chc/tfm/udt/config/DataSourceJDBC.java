package chc.tfm.udt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

@PropertySource("classpath:application.properties")
@Configuration
public class DataSourceJDBC {

    @Value("${spring.datasource.url}") private String jdbcUrl;
    @Value("${spring.datasource.username}") private String username;
    @Value("${spring.datasource.password}") private String password;
    @Value("${spring.datasource.driver-class-name}") private String driver;
    @Bean(value = "DataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .username(username)
                .password(password)
                .url(jdbcUrl)
                .driverClassName(driver)
                .build();
    }

    @Bean(value = "JdbcTemplate")
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(this.dataSource());
    }
}

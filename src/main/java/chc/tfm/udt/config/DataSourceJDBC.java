package chc.tfm.udt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Clase de configuración que esta vinculada a 1 archivo de propiedades donde esta la información de la base de datos.
 */
@PropertySource("classpath:application.properties")
@Configuration
@Component("DataSourceJDBC")
public class DataSourceJDBC {

    @Value("${spring.datasource.url}") private String jdbcUrl;
    @Value("${spring.datasource.username}") private String username;
    @Value("${spring.datasource.password}") private String password;
    @Value("${spring.datasource.driver-class-name}") private String driver;

    /**
     * Metodo que devuelve 1 BEAN de la Interfaz DataSource , que hace posibloe crear la instancia de la conexión.
     * @return
     */
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

    /**
     * Bean de la interaz JdbcTemplate que utilizaremos para trabajar con la base de datos , a través de sus metodos.
     * @return
     */
    @Bean(value = "JdbcTemplate")
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(this.dataSource());
    }
}

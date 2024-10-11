package ru.selfvsself.home_texttotext_api.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "ru.selfvsself.home_texttotext_api.repository",
        entityManagerFactoryRef = "messageEntityManagerFactory",
        transactionManagerRef = "messageTransactionManager"
)
public class MessageDatabaseConfig {

    @Value("${spring.datasource.messagedb.table-scheme}")
    private String schema;

    @Bean(name = "messageDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.messagedb")
    public DataSource messageDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "messageEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean messageEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("messageDataSource") DataSource dataSource) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.default_schema", schema);

        return builder
                .dataSource(dataSource)
                .packages("ru.selfvsself.home_texttotext_api.model.database")
                .persistenceUnit("message")
                .properties(properties)
                .build();
    }

    @Bean(name = "messageTransactionManager")
    public PlatformTransactionManager messageTransactionManager(
            @Qualifier("messageEntityManagerFactory") EntityManagerFactory messageEntityManagerFactory) {
        return new JpaTransactionManager(messageEntityManagerFactory);
    }
}
//package com.github.moviereservationbe.config;
//
//import jakarta.persistence.EntityManagerFactory;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.JpaVendorAdapter;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@EntityScan
//@EnableConfigurationProperties(DataSourceProperties.class)
//@RequiredArgsConstructor
//@EnableJpaRepositories(
//        basePackages = {"com.github.moviereservationbe.repository.role",
//                "com.github.moviereservationbe.repository.userRole",
//                "com.github.moviereservationbe.repository.user"
//        },
//        entityManagerFactoryRef = "localContainerEntityManagerFactoryBean",
//        transactionManagerRef = "tm"
//)
//public class JpaConfig {
//    private final DataSourceProperties dataSourceProperties;
//
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
//        driverManagerDataSource.setUrl(dataSourceProperties.getUrl());
//        driverManagerDataSource.setUsername(dataSourceProperties.getUsername());
//        driverManagerDataSource.setPassword(dataSourceProperties.getPassword());
//        driverManagerDataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
//        return driverManagerDataSource;
//    }
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(DataSource datasource) {
//        LocalContainerEntityManagerFactoryBean lemfb = new LocalContainerEntityManagerFactoryBean();
//        lemfb.setDataSource(datasource);
//        lemfb.setPackagesToScan(
//                "com.github.moviereservationbe.repository.role",
//                "com.github.moviereservationbe.repository.userRole",
//                "com.github.moviereservationbe.repository.user"
//        );
//        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        lemfb.setJpaVendorAdapter(vendorAdapter);
//
//        Map<String, Object> properties = new HashMap<>();
//        properties.put("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect");
//        properties.put("hibernate.format_sql", "true");
//        properties.put("hibernate.use_sql_comment", "true");
//
//        lemfb.setJpaPropertyMap(properties);
//
//        return lemfb;
//    }
//
//    @Bean(name = "tm")
//    public PlatformTransactionManager platformTransactionManager(EntityManagerFactory entityManagerFactory) {
//        return new JpaTransactionManager(entityManagerFactory);
//    }
//}

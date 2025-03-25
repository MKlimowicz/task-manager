package pl.mk.taskmanager.common.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class JpaConfig {

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {

    LocalContainerEntityManagerFactoryBean emFactory = new LocalContainerEntityManagerFactoryBean();
    emFactory.setDataSource(dataSource);
    // TODO: tips: Remember to add each new entity package here when you create additional entities
    emFactory.setPackagesToScan("pl.mk.taskmanager.project.model");
    emFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

    Properties props = new Properties();
    props.setProperty("hibernate.hbm2ddl.auto", "validate");
    props.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

    emFactory.setJpaProperties(props);

    return emFactory;
  }
}

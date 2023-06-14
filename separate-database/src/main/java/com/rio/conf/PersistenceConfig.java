package com.rio.conf;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.rio")
@EnableConfigurationProperties(JpaProperties.class)
@RequiredArgsConstructor
public class PersistenceConfig {

  private final ConfigurableListableBeanFactory beanFactory;
  private final JpaProperties jpaProperties;

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
          MultiTenantConnectionProvider connectionProvider,
          CurrentTenantIdentifierResolver tenantResolver) {
    var emfBean = new LocalContainerEntityManagerFactoryBean();
    emfBean.setPackagesToScan("com.rio");
    emfBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

    var properties = new HashMap<String, Object>(this.jpaProperties.getProperties());
    properties.put(AvailableSettings.BEAN_CONTAINER, new SpringBeanContainer(this.beanFactory));
    properties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
    properties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
    emfBean.setJpaPropertyMap(properties);

    return emfBean;
  }

  @Bean
  public TransactionManager transactionManager(EntityManagerFactory emf) {
    var transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(emf);
    return transactionManager;
  }
}
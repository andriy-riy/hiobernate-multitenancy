package com.rio.conf;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Component
public class DynamicDataSourceBasedMultiTenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
  private static final String HIBERNATE_PROPERTIES_PATH = "/hibernate-%s.properties";
  private final Map<String, DataSource> tenantDataSources = new HashMap<>();

  @PostConstruct
  private void init() {
    tenantDataSources.put("java-club", createAndConfigureDataSource("java-club"));
    tenantDataSources.put("coffee-jug", createAndConfigureDataSource("coffee-jug"));
  }

  @Override
  protected DataSource selectAnyDataSource() {
    return tenantDataSources.values().iterator().next();
  }

  @Override
  protected DataSource selectDataSource(String tenantIdentifier) {
    return tenantDataSources.get(tenantIdentifier);
  }

  private DataSource createAndConfigureDataSource(String tenantId) {
    Properties properties = getHibernatePropertiesForTenantId(tenantId);
    return DataSourceBuilder.create()
            .url(properties.get(Environment.URL).toString())
            .username(properties.get(Environment.USER).toString())
            .password(properties.get(Environment.PASS).toString())
            .driverClassName(properties.get(Environment.DRIVER).toString())
            .build();
  }

  private Properties getHibernatePropertiesForTenantId(String tenantId) {
    String hibernateConfigPath = String.format(HIBERNATE_PROPERTIES_PATH, tenantId);
    try (InputStream configAsStream = getClass().getResourceAsStream(hibernateConfigPath)) {
      var properties = new Properties();
      properties.load(configAsStream);
      return properties;
    } catch (IOException e) {
      throw new RuntimeException(String.format("Cannot open hibernate properties: %s", HIBERNATE_PROPERTIES_PATH));
    }
  }
}
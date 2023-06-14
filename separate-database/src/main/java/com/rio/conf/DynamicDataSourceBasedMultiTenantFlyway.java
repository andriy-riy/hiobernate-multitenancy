package com.rio.conf;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.io.IOException;
import java.util.Properties;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DynamicDataSourceBasedMultiTenantFlyway implements InitializingBean {
  private static final String HIBERNATE_PROPERTIES_PATH = "/hibernate-%s.properties";

  @Override
  public void afterPropertiesSet() {
    migrateTenant("java-club");
    migrateTenant("coffee-jug");
  }

  private void migrateTenant(String tenantId) throws FlywayException {
    Properties properties = getHibernatePropertiesForTenantId(tenantId);

    Flyway.configure()
            .baselineOnMigrate(true)
            .dataSource(new SingleConnectionDataSource(
                    properties.get(Environment.URL).toString(),
                    properties.get(Environment.USER).toString(),
                    properties.get(Environment.PASS).toString(),
                    false))
            .load()
            .migrate();
  }

  private Properties getHibernatePropertiesForTenantId(String tenantId) {
    try {
      Properties properties = new Properties();
      properties.load(getClass().getResourceAsStream(String.format(HIBERNATE_PROPERTIES_PATH, tenantId)));
      return properties;
    } catch (IOException e) {
      throw new RuntimeException(String.format("Cannot open hibernate properties: %s", HIBERNATE_PROPERTIES_PATH));
    }
  }
}
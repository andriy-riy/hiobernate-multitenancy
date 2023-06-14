package com.rio.conf;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DynamicSchemaBasedMultiTenantFlyway implements InitializingBean {

  private final DataSource dataSource;

  @Override
  public void afterPropertiesSet() {
    migrateTenant("java-club");
    migrateTenant("coffee-jug");
  }

  private void migrateTenant(String tenantId) throws FlywayException {
    Flyway.configure()
            .baselineOnMigrate(true)
            .dataSource(dataSource)
            .defaultSchema(tenantId)
            .load()
            .migrate();
  }
}
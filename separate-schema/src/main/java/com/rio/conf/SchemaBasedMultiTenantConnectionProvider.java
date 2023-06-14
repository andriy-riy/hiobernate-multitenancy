package com.rio.conf;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchemaBasedMultiTenantConnectionProvider implements MultiTenantConnectionProvider {

  private final DataSource datasource;

  @Override
  public Connection getAnyConnection() throws SQLException {
    return datasource.getConnection();
  }

  @Override
  public void releaseAnyConnection(Connection connection) throws SQLException {
    connection.close();
  }

  @Override
  public Connection getConnection(String tenantIdentifier) throws SQLException {
    log.info("Get connection for tenant {}", tenantIdentifier);
    Connection connection = getAnyConnection();
    connection.setSchema(tenantIdentifier);
    connection.createStatement().execute(String.format("USE `%s`;", tenantIdentifier));
    return connection;
  }

  @Override
  public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
    log.info("Release connection for tenant {}", tenantIdentifier);
    connection.setSchema(null);
    releaseAnyConnection(connection);
  }

  @Override
  public boolean supportsAggressiveRelease() {
    return false;
  }

  @Override
  public boolean isUnwrappableAs(Class unwrapType) {
    return MultiTenantConnectionProvider.class.isAssignableFrom(unwrapType);
  }

  @Override
  public <T> T unwrap(Class<T> unwrapType) {
    if (MultiTenantConnectionProvider.class.isAssignableFrom(unwrapType)) {
      @SuppressWarnings("unchecked")
      T result = (T) this;
      return result;
    } else {
      throw new UnknownUnwrapTypeException(unwrapType);
    }
  }
}
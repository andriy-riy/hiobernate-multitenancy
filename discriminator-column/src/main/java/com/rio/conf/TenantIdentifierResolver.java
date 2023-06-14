package com.rio.conf;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

  @Override
  public String resolveCurrentTenantIdentifier() {
    String tenantId = TenantHolder.getCurrentTenant();
    // Allow bootstrapping the EntityManagerFactory, in which case no tenant is needed
    return Objects.requireNonNullElse(tenantId, "DEFAULT");
  }

  @Override
  public boolean validateExistingCurrentSessions() {
    return true;
  }
}

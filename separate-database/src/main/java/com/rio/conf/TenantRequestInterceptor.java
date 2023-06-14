package com.rio.conf;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TenantRequestInterceptor implements HandlerInterceptor {
  private static final String TENANT_HEADER = "Tenant";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    String currentTenant = request.getHeader(TENANT_HEADER);
    if (currentTenant != null) {
      TenantHolder.setCurrentTenant(currentTenant);
      return true;
    }
    return false;
  }
}
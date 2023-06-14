package com.rio.conf;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TenantHolder {

    private static final ThreadLocal<String> CURRENT_TENANT_HOLDER = new InheritableThreadLocal<>();

    public static String getCurrentTenant() {
        return CURRENT_TENANT_HOLDER.get();
    }

    public static void setCurrentTenant(String tenant) {
        CURRENT_TENANT_HOLDER.set(tenant);
    }
}

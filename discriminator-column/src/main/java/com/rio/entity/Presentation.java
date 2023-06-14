package com.rio.entity;

import com.rio.conf.TenantHolder;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.TenantId;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "presentation")
@AllArgsConstructor
@NoArgsConstructor
public class Presentation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;
  @Column(name = "name")
  private String name;
  @Column(name = "description")
  private String description;
  @TenantId
  @Column(name = "tenant_id")
  private String tenantId;
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @PreUpdate
  @PreRemove
  @PrePersist
  public void setTenant() {
    String currentTenant = TenantHolder.getCurrentTenant();
    if (StringUtils.isNotEmpty(currentTenant)) {
      this.tenantId = currentTenant;
    }
  }
}

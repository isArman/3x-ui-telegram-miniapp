package com.proxy.subscription.plan.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;

@Entity
@Table(name = "plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "data_allowance_gb", nullable = false)
    private Long dataAllowanceGb;

    @Column(name = "validity_duration_days", nullable = false)
    private Integer validityDurationDays;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal priceToman;

    public Plan() {}

    public Plan(String name, Long dataAllowanceGb, Integer validityDurationDays, BigDecimal priceToman) {
        this.name = name;
        this.dataAllowanceGb = dataAllowanceGb;
        this.validityDurationDays = validityDurationDays;
        this.priceToman = priceToman;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getDataAllowanceGb() { return dataAllowanceGb; }
    public void setDataAllowanceGb(Long dataAllowanceGb) { this.dataAllowanceGb = dataAllowanceGb; }

    public Integer getValidityDurationDays() { return validityDurationDays; }
    public void setValidityDurationDays(Integer validityDurationDays) { this.validityDurationDays = validityDurationDays; }

    public BigDecimal getPriceToman() { return priceToman; }
    public void setPriceToman(BigDecimal priceToman) { this.priceToman = priceToman; }
}

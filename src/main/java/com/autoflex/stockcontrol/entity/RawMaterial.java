package com.autoflex.stockcontrol.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Entity
@Table(name = "raw_material")
public class RawMaterial extends PanacheEntity {

    @NotBlank
    @Column(name = "name", nullable = false)
    public String name;

    @NotNull
    @PositiveOrZero
    @Column(name = "stock_quantity", nullable = false, precision = 10, scale = 2)
    public BigDecimal stockQuantity;
}

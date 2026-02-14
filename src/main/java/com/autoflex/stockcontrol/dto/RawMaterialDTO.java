package com.autoflex.stockcontrol.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class RawMaterialDTO {

    public Long id;

    @NotBlank
    public String name;

    @NotNull
    @PositiveOrZero
    public BigDecimal stockQuantity;
}

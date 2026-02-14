package com.autoflex.stockcontrol.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class ProductDTO {

    public Long id;

    @NotBlank
    public String name;

    @NotNull
    @Positive
    public BigDecimal value;
}

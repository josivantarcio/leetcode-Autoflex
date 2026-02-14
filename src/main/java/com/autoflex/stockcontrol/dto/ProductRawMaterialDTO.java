package com.autoflex.stockcontrol.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class ProductRawMaterialDTO {

    public Long id;

    @NotNull
    public Long productId;

    @NotNull
    public Long rawMaterialId;

    public String productName;

    public String rawMaterialName;

    @NotNull
    @Positive
    public BigDecimal quantity;
}

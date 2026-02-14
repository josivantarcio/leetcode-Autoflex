package com.autoflex.stockcontrol.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProductionSuggestionDTO {

    public List<SuggestedProduct> suggestedProducts;
    public BigDecimal totalValue;

    public static class SuggestedProduct {
        public Long productId;
        public String productName;
        public BigDecimal productValue;
        public int quantity;
        public BigDecimal subtotal;
    }
}

package com.autoflex.stockcontrol.service;

import com.autoflex.stockcontrol.dto.ProductionSuggestionDTO;
import com.autoflex.stockcontrol.dto.ProductionSuggestionDTO.SuggestedProduct;
import com.autoflex.stockcontrol.entity.Product;
import com.autoflex.stockcontrol.entity.ProductRawMaterial;
import com.autoflex.stockcontrol.entity.RawMaterial;
import com.autoflex.stockcontrol.repository.ProductRawMaterialRepository;
import com.autoflex.stockcontrol.repository.ProductRepository;
import com.autoflex.stockcontrol.repository.RawMaterialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ProductionSuggestionService {

    @Inject
    ProductRepository productRepository;

    @Inject
    RawMaterialRepository rawMaterialRepository;

    @Inject
    ProductRawMaterialRepository productRawMaterialRepository;

    public ProductionSuggestionDTO suggest() {
        // Load current stock into an in-memory map
        Map<Long, BigDecimal> availableStock = new HashMap<>();
        for (RawMaterial rm : rawMaterialRepository.listAll()) {
            availableStock.put(rm.id, rm.stockQuantity);
        }

        // Get all products sorted by value descending (greedy: most valuable first)
        List<Product> products = new ArrayList<>(productRepository.listAll());
        products.sort(Comparator.comparing((Product p) -> p.value).reversed());

        List<SuggestedProduct> suggestions = new ArrayList<>();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (Product product : products) {
            List<ProductRawMaterial> materials = productRawMaterialRepository.findByProductId(product.id);
            if (materials.isEmpty()) {
                continue;
            }

            // Calculate how many units can be produced
            int maxUnits = Integer.MAX_VALUE;
            for (ProductRawMaterial prm : materials) {
                BigDecimal available = availableStock.getOrDefault(prm.rawMaterial.id, BigDecimal.ZERO);
                int possibleUnits = available.divide(prm.quantity, 0, RoundingMode.FLOOR).intValue();
                maxUnits = Math.min(maxUnits, possibleUnits);
            }

            if (maxUnits >= 1) {
                // Deduct raw materials from available stock
                for (ProductRawMaterial prm : materials) {
                    BigDecimal used = prm.quantity.multiply(BigDecimal.valueOf(maxUnits));
                    BigDecimal remaining = availableStock.get(prm.rawMaterial.id).subtract(used);
                    availableStock.put(prm.rawMaterial.id, remaining);
                }

                SuggestedProduct sp = new SuggestedProduct();
                sp.productId = product.id;
                sp.productName = product.name;
                sp.productValue = product.value;
                sp.quantity = maxUnits;
                sp.subtotal = product.value.multiply(BigDecimal.valueOf(maxUnits));
                suggestions.add(sp);

                totalValue = totalValue.add(sp.subtotal);
            }
        }

        ProductionSuggestionDTO result = new ProductionSuggestionDTO();
        result.suggestedProducts = suggestions;
        result.totalValue = totalValue;
        return result;
    }
}

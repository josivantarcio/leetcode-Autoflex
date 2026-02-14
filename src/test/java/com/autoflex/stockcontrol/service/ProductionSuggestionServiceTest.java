package com.autoflex.stockcontrol.service;

import com.autoflex.stockcontrol.dto.ProductionSuggestionDTO;
import com.autoflex.stockcontrol.entity.Product;
import com.autoflex.stockcontrol.entity.ProductRawMaterial;
import com.autoflex.stockcontrol.entity.RawMaterial;
import com.autoflex.stockcontrol.repository.ProductRawMaterialRepository;
import com.autoflex.stockcontrol.repository.ProductRepository;
import com.autoflex.stockcontrol.repository.RawMaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductionSuggestionServiceTest {

    private ProductionSuggestionService service;
    private ProductRepository productRepository;
    private RawMaterialRepository rawMaterialRepository;
    private ProductRawMaterialRepository productRawMaterialRepository;

    @BeforeEach
    void setUp() {
        service = new ProductionSuggestionService();
        productRepository = mock(ProductRepository.class);
        rawMaterialRepository = mock(RawMaterialRepository.class);
        productRawMaterialRepository = mock(ProductRawMaterialRepository.class);

        service.productRepository = productRepository;
        service.rawMaterialRepository = rawMaterialRepository;
        service.productRawMaterialRepository = productRawMaterialRepository;
    }

    @Test
    void testSuggestWithNoProducts() {
        when(productRepository.listAll()).thenReturn(List.of());
        when(rawMaterialRepository.listAll()).thenReturn(List.of());

        ProductionSuggestionDTO result = service.suggest();

        assertTrue(result.suggestedProducts.isEmpty());
        assertEquals(BigDecimal.ZERO, result.totalValue);
    }

    @Test
    void testSuggestPrioritizesHigherValue() {
        // Raw materials
        RawMaterial wood = new RawMaterial();
        wood.id = 1L;
        wood.name = "Wood";
        wood.stockQuantity = new BigDecimal("100");

        // Products
        Product expensive = new Product();
        expensive.id = 1L;
        expensive.name = "Expensive Table";
        expensive.value = new BigDecimal("500.00");

        Product cheap = new Product();
        cheap.id = 2L;
        cheap.name = "Cheap Stool";
        cheap.value = new BigDecimal("50.00");

        // Associations
        ProductRawMaterial expensiveWood = new ProductRawMaterial();
        expensiveWood.product = expensive;
        expensiveWood.rawMaterial = wood;
        expensiveWood.quantity = new BigDecimal("30");

        ProductRawMaterial cheapWood = new ProductRawMaterial();
        cheapWood.product = cheap;
        cheapWood.rawMaterial = wood;
        cheapWood.quantity = new BigDecimal("10");

        when(rawMaterialRepository.listAll()).thenReturn(List.of(wood));
        when(productRepository.listAll()).thenReturn(List.of(cheap, expensive)); // unordered
        when(productRawMaterialRepository.findByProductId(1L)).thenReturn(List.of(expensiveWood));
        when(productRawMaterialRepository.findByProductId(2L)).thenReturn(List.of(cheapWood));

        ProductionSuggestionDTO result = service.suggest();

        assertEquals(2, result.suggestedProducts.size());
        // First suggestion should be the expensive product (greedy)
        assertEquals("Expensive Table", result.suggestedProducts.get(0).productName);
        assertEquals(3, result.suggestedProducts.get(0).quantity); // 100/30 = 3
        // Remaining wood: 100 - 90 = 10, cheap stool needs 10 -> 1 unit
        assertEquals("Cheap Stool", result.suggestedProducts.get(1).productName);
        assertEquals(1, result.suggestedProducts.get(1).quantity);
        // Total: 3*500 + 1*50 = 1550
        assertEquals(new BigDecimal("1550.00"), result.totalValue);
    }

    @Test
    void testSuggestWithInsufficientStock() {
        RawMaterial wood = new RawMaterial();
        wood.id = 1L;
        wood.name = "Wood";
        wood.stockQuantity = new BigDecimal("5");

        Product table = new Product();
        table.id = 1L;
        table.name = "Table";
        table.value = new BigDecimal("200.00");

        ProductRawMaterial tableWood = new ProductRawMaterial();
        tableWood.product = table;
        tableWood.rawMaterial = wood;
        tableWood.quantity = new BigDecimal("10");

        when(rawMaterialRepository.listAll()).thenReturn(List.of(wood));
        when(productRepository.listAll()).thenReturn(List.of(table));
        when(productRawMaterialRepository.findByProductId(1L)).thenReturn(List.of(tableWood));

        ProductionSuggestionDTO result = service.suggest();

        assertTrue(result.suggestedProducts.isEmpty());
        assertEquals(BigDecimal.ZERO, result.totalValue);
    }

    @Test
    void testSuggestWithMultipleRawMaterials() {
        RawMaterial wood = new RawMaterial();
        wood.id = 1L;
        wood.name = "Wood";
        wood.stockQuantity = new BigDecimal("100");

        RawMaterial nail = new RawMaterial();
        nail.id = 2L;
        nail.name = "Nail";
        nail.stockQuantity = new BigDecimal("50");

        Product chair = new Product();
        chair.id = 1L;
        chair.name = "Chair";
        chair.value = new BigDecimal("150.00");

        ProductRawMaterial chairWood = new ProductRawMaterial();
        chairWood.product = chair;
        chairWood.rawMaterial = wood;
        chairWood.quantity = new BigDecimal("10");

        ProductRawMaterial chairNail = new ProductRawMaterial();
        chairNail.product = chair;
        chairNail.rawMaterial = nail;
        chairNail.quantity = new BigDecimal("20");

        when(rawMaterialRepository.listAll()).thenReturn(List.of(wood, nail));
        when(productRepository.listAll()).thenReturn(List.of(chair));
        when(productRawMaterialRepository.findByProductId(1L)).thenReturn(List.of(chairWood, chairNail));

        ProductionSuggestionDTO result = service.suggest();

        assertEquals(1, result.suggestedProducts.size());
        // wood: 100/10=10, nail: 50/20=2 -> min is 2
        assertEquals(2, result.suggestedProducts.get(0).quantity);
        assertEquals(new BigDecimal("300.00"), result.totalValue);
    }
}

package com.autoflex.stockcontrol.repository;

import com.autoflex.stockcontrol.entity.ProductRawMaterial;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ProductRawMaterialRepository implements PanacheRepository<ProductRawMaterial> {

    public List<ProductRawMaterial> findByProductId(Long productId) {
        return list("product.id", productId);
    }
}

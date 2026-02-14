package com.autoflex.stockcontrol.service;

import com.autoflex.stockcontrol.dto.ProductRawMaterialDTO;
import com.autoflex.stockcontrol.entity.Product;
import com.autoflex.stockcontrol.entity.ProductRawMaterial;
import com.autoflex.stockcontrol.entity.RawMaterial;
import com.autoflex.stockcontrol.repository.ProductRawMaterialRepository;
import com.autoflex.stockcontrol.repository.ProductRepository;
import com.autoflex.stockcontrol.repository.RawMaterialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class ProductRawMaterialService {

    @Inject
    ProductRawMaterialRepository repository;

    @Inject
    ProductRepository productRepository;

    @Inject
    RawMaterialRepository rawMaterialRepository;

    public List<ProductRawMaterialDTO> listAll() {
        return repository.listAll().stream().map(this::toDTO).toList();
    }

    public List<ProductRawMaterialDTO> findByProductId(Long productId) {
        return repository.findByProductId(productId).stream().map(this::toDTO).toList();
    }

    @Transactional
    public ProductRawMaterialDTO create(ProductRawMaterialDTO dto) {
        Product product = productRepository.findById(dto.productId);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }

        RawMaterial rawMaterial = rawMaterialRepository.findById(dto.rawMaterialId);
        if (rawMaterial == null) {
            throw new NotFoundException("Raw material not found");
        }

        ProductRawMaterial entity = new ProductRawMaterial();
        entity.product = product;
        entity.rawMaterial = rawMaterial;
        entity.quantity = dto.quantity;
        repository.persist(entity);
        return toDTO(entity);
    }

    @Transactional
    public ProductRawMaterialDTO update(Long id, ProductRawMaterialDTO dto) {
        ProductRawMaterial entity = repository.findById(id);
        if (entity == null) {
            throw new NotFoundException("Product-RawMaterial association not found");
        }

        Product product = productRepository.findById(dto.productId);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }

        RawMaterial rawMaterial = rawMaterialRepository.findById(dto.rawMaterialId);
        if (rawMaterial == null) {
            throw new NotFoundException("Raw material not found");
        }

        entity.product = product;
        entity.rawMaterial = rawMaterial;
        entity.quantity = dto.quantity;
        return toDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        ProductRawMaterial entity = repository.findById(id);
        if (entity == null) {
            throw new NotFoundException("Product-RawMaterial association not found");
        }
        repository.delete(entity);
    }

    private ProductRawMaterialDTO toDTO(ProductRawMaterial entity) {
        ProductRawMaterialDTO dto = new ProductRawMaterialDTO();
        dto.id = entity.id;
        dto.productId = entity.product.id;
        dto.rawMaterialId = entity.rawMaterial.id;
        dto.productName = entity.product.name;
        dto.rawMaterialName = entity.rawMaterial.name;
        dto.quantity = entity.quantity;
        return dto;
    }
}

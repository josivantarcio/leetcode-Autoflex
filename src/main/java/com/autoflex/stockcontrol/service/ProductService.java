package com.autoflex.stockcontrol.service;

import com.autoflex.stockcontrol.dto.ProductDTO;
import com.autoflex.stockcontrol.entity.Product;
import com.autoflex.stockcontrol.repository.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository repository;

    public List<ProductDTO> listAll() {
        return repository.listAll().stream().map(this::toDTO).toList();
    }

    public ProductDTO findById(Long id) {
        Product product = repository.findById(id);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }
        return toDTO(product);
    }

    @Transactional
    public ProductDTO create(ProductDTO dto) {
        Product product = new Product();
        product.name = dto.name;
        product.value = dto.value;
        repository.persist(product);
        return toDTO(product);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        Product product = repository.findById(id);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }
        product.name = dto.name;
        product.value = dto.value;
        return toDTO(product);
    }

    @Transactional
    public void delete(Long id) {
        Product product = repository.findById(id);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }
        repository.delete(product);
    }

    private ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.id = product.id;
        dto.name = product.name;
        dto.value = product.value;
        return dto;
    }
}

package com.autoflex.stockcontrol.service;

import com.autoflex.stockcontrol.dto.RawMaterialDTO;
import com.autoflex.stockcontrol.entity.RawMaterial;
import com.autoflex.stockcontrol.repository.RawMaterialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class RawMaterialService {

    @Inject
    RawMaterialRepository repository;

    public List<RawMaterialDTO> listAll() {
        return repository.listAll().stream().map(this::toDTO).toList();
    }

    public RawMaterialDTO findById(Long id) {
        RawMaterial rawMaterial = repository.findById(id);
        if (rawMaterial == null) {
            throw new NotFoundException("Raw material not found");
        }
        return toDTO(rawMaterial);
    }

    @Transactional
    public RawMaterialDTO create(RawMaterialDTO dto) {
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.name = dto.name;
        rawMaterial.stockQuantity = dto.stockQuantity;
        repository.persist(rawMaterial);
        return toDTO(rawMaterial);
    }

    @Transactional
    public RawMaterialDTO update(Long id, RawMaterialDTO dto) {
        RawMaterial rawMaterial = repository.findById(id);
        if (rawMaterial == null) {
            throw new NotFoundException("Raw material not found");
        }
        rawMaterial.name = dto.name;
        rawMaterial.stockQuantity = dto.stockQuantity;
        return toDTO(rawMaterial);
    }

    @Transactional
    public void delete(Long id) {
        RawMaterial rawMaterial = repository.findById(id);
        if (rawMaterial == null) {
            throw new NotFoundException("Raw material not found");
        }
        repository.delete(rawMaterial);
    }

    private RawMaterialDTO toDTO(RawMaterial rawMaterial) {
        RawMaterialDTO dto = new RawMaterialDTO();
        dto.id = rawMaterial.id;
        dto.name = rawMaterial.name;
        dto.stockQuantity = rawMaterial.stockQuantity;
        return dto;
    }
}

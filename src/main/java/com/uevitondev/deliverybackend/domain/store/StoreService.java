package com.uevitondev.deliverybackend.domain.store;

import com.uevitondev.deliverybackend.domain.exception.DatabaseException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.product.Product;
import com.uevitondev.deliverybackend.domain.product.ProductService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final ProductService productService;

    public StoreService(StoreRepository storeRepository, ProductService productService) {
        this.storeRepository = storeRepository;
        this.productService = productService;
    }

    public List<Store> findAllStores() {
        return storeRepository.findAll();
    }

    public List<Store> findAllStoresWithFilters(String name) {
        return storeRepository.findStoresWithFilters(name);
    }

    public Store findById(UUID id) {
        return storeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("store not found"));
    }

    public Store findStoreByName(String name) {
        return storeRepository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("store not found for name " + name)
        );
    }

    @Transactional
    public Store insertNewStore(StoreDTO dto) {
        var store = new Store();
        store.setLogoUrl(dto.logoUrl());
        store.setName(dto.name());
        store.setPhoneNumber(dto.phoneNumber());
        store.setType(StoreType.valueOf(dto.type()).toString());
        store = storeRepository.save(store);
        return storeRepository.save(store);
    }

    @Transactional
    public Store updateStore(StoreDTO dto) {
        var store = findById(dto.id());
        store.setLogoUrl(dto.logoUrl());
        store.setName(dto.name());
        store.setPhoneNumber(dto.phoneNumber());
        store.setType(StoreType.valueOf(dto.type()).toString());
        store.setUpdatedAt(LocalDateTime.now());
        return storeRepository.save(store);
    }

    @Transactional
    public void deleteStoreById(UUID storeId) {
        try {
            storeRepository.deleteById(findById(storeId).getId());
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity constraint violation");
        }
    }

    // products
    public Page<Product> findAllProductsByStoreId(UUID id, String name, String category, Pageable pageable) {
        return productService.findAllProductsByStoreId(id, name, category, pageable);
    }


}

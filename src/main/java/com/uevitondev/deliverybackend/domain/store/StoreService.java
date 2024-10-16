package com.uevitondev.deliverybackend.domain.store;

import com.uevitondev.deliverybackend.domain.exception.DatabaseException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public Store findById(UUID id) {
        return storeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("store not found"));
    }

    public List<StoreDTO> findAllStores() {
        return storeRepository.findAll().stream().map(StoreDTO::new).toList();
    }

    public StoreDTO findStoreById(UUID storeId) {
        return new StoreDTO(findById(storeId));
    }

    @Transactional
    public StoreDTO insertNewStore(StoreDTO dto) {
        var store = new Store();
        store.setLogoUrl(dto.logoUrl());
        store.setName(dto.name());
        store.setPhoneNumber(dto.phoneNumber());
        store.setType(StoreType.valueOf(dto.type()).toString());
        store = storeRepository.save(store);
        return new StoreDTO(storeRepository.save(store));
    }

    @Transactional
    public StoreDTO updateStore(StoreDTO dto) {
        var store = findById(dto.id());
        store.setLogoUrl(dto.logoUrl());
        store.setName(dto.name());
        store.setPhoneNumber(dto.phoneNumber());
        store.setType(StoreType.valueOf(dto.type()).toString());
        store.setUpdatedAt(LocalDateTime.now());
        return new StoreDTO(storeRepository.save(store));
    }

    @Transactional
    public void deleteStoreById(UUID storeId) {
        try {
            storeRepository.deleteById(findById(storeId).getId());
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity constraint violation");
        }
    }


}

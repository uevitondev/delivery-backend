package com.uevitondev.deliverybackend.domain.service;

import com.uevitondev.deliverybackend.domain.dto.StoreDTO;
import com.uevitondev.deliverybackend.domain.exception.DatabaseException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.model.Store;
import com.uevitondev.deliverybackend.domain.repository.StoreRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

    @Transactional(readOnly = true)
    public List<StoreDTO> findAllStores() {
        return storeRepository.findAll().stream().map(StoreDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public StoreDTO findStoreById(UUID id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("store not found for storeId: " + id));
        return new StoreDTO(store);
    }

    @Transactional
    public StoreDTO insertNewStore(StoreDTO dto) {
        Store store = new Store();
        store.setName(dto.getName());
        store = storeRepository.save(store);

        return new StoreDTO(store);
    }

    @Transactional
    public StoreDTO updateStoreById(UUID id, StoreDTO dto) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("store not found for storeId: " + id));
        store.setName(dto.getName());
        store.setUpdateAt(LocalDateTime.now());
        store = storeRepository.save(store);
        return new StoreDTO(store);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteStoreById(UUID id) {
        if (!storeRepository.existsById(id)) {
            throw new ResourceNotFoundException("store not found for storeId: " + id);
        }
        try {
            storeRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity constraint violation");
        }
    }
}

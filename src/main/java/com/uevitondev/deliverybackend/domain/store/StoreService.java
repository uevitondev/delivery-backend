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
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public List<StoreDTO> findAllStores() {
        return storeRepository.findAll().stream().map(StoreDTO::new).toList();
    }

    public StoreDTO findStoreById(UUID id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("store not found for storeId: " + id));
        return new StoreDTO(store);
    }

    public StoreDTO insertNewStore(StoreDTO dto) {
        Store store = new Store();
        store.setName(dto.name());
        store = storeRepository.save(store);

        return new StoreDTO(store);
    }

    public StoreDTO updateStoreById(UUID id, StoreDTO dto) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("store not found for storeId: " + id));
        store.setName(dto.name());
        store.setUpdatedAt(LocalDateTime.now());
        store = storeRepository.save(store);
        return new StoreDTO(store);
    }

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

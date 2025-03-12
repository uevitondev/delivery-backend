package com.uevitondev.deliverybackend.domain.store;

import com.uevitondev.deliverybackend.config.aws.AwsS3Service;
import com.uevitondev.deliverybackend.domain.exception.DatabaseException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.product.Product;
import com.uevitondev.deliverybackend.domain.product.ProductService;
import com.uevitondev.deliverybackend.domain.seller.Seller;
import com.uevitondev.deliverybackend.domain.user.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final ProductService productService;
    private final UserService userService;
    private final AwsS3Service awsS3Service;

    public StoreService(StoreRepository storeRepository, ProductService productService, UserService userService, AwsS3Service awsS3Service) {
        this.storeRepository = storeRepository;
        this.productService = productService;
        this.userService = userService;
        this.awsS3Service = awsS3Service;
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


    public List<Store> findStoresBySeller() {
        var seller = (Seller) userService.getUserAuthenticated();
        return storeRepository.findBySeller(seller);
    }

    @Transactional
    public Store insertNewStore(MultipartFile logoFile, NewStoreDTO dto) {
        try {
            var seller = (Seller) userService.getUserAuthenticated();
            var store = new Store();
            store.setCreatedAt(LocalDateTime.now());
            store.setUpdatedAt(LocalDateTime.now());
            store.setLogoUrl(awsS3Service.uploadS3FileAndReturnUrl(logoFile));
            store.setName(dto.name());
            store.setPhoneNumber(dto.phoneNumber());
            store.setType(StoreType.valueOf(dto.type()).toString());
            store.setSeller(seller);
            return storeRepository.save(store);
        } catch (RuntimeException | IOException e) {
            throw new RuntimeException(e);
        }


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

package com.uevitondev.deliverybackend.domain.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("SELECT p FROM Product p")
    Page<Product> findAllProductsPaged(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.store.id = :storeId AND (:categoryName = '' OR p.category.name = :categoryName)")
    Page<Product> findAllByStoreAndCategory(
            @Param("storeId") UUID storeId,
            @Param("categoryName") String categoryName,
            Pageable pageable
    );
}
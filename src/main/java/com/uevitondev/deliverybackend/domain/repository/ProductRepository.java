package com.uevitondev.deliverybackend.domain.repository;

import com.uevitondev.deliverybackend.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("SELECT p FROM Product p JOIN FETCH p.category  JOIN FETCH p.store")
    Page<Product> findAllProductsPaged(Pageable pageable);

    @Query("SELECT p FROM Product p JOIN FETCH p.category  JOIN FETCH p.store where p.store.id = :id")
    Page<Product> findAllProductsPagedByStoreId(UUID id, Pageable pageable);

}
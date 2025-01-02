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
    Page<Product> findAllProducts(Pageable pageable);


    @Query("""
            SELECT p 
            FROM Product p 
            WHERE p.store.id = :id 
            AND (
                 (
                     :name = '' 
                     OR LOWER(p.name) LIKE CONCAT('%', LOWER(:name), '%')
                     OR NOT EXISTS (
                         SELECT 1 
                         FROM Product p2 
                         WHERE p2.store.id = :id 
                         AND LOWER(p2.name) LIKE CONCAT('%', LOWER(:name), '%')
                         AND LOWER(p2.category.name) LIKE CONCAT('%', LOWER(:category), '%')
                     )
                 )
                 AND (
                     :category = '' 
                     OR LOWER(p.category.name) LIKE CONCAT('%', LOWER(:category), '%')
                     OR NOT EXISTS (
                         SELECT 1 
                         FROM Product p3 
                         WHERE p3.store.id = :id 
                         AND LOWER(p3.category.name) LIKE CONCAT('%', LOWER(:category), '%')
                     )
                 )
            )
            """)
    Page<Product> findProductsByStoreWithFilters(
            @Param("id") UUID id,
            @Param("name") String name,
            @Param("category") String category,
            Pageable pageable
    );
}
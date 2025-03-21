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

    //@Query("SELECT p FROM Product p")
    //Page<Product> findAllProducts(Pageable pageable);

    Page<Product> findAll(Pageable pageable);



    @Query(value = """
    SELECT p.* 
    FROM tb_product p 
    JOIN tb_category c ON p.category_id = c.id
    WHERE p.store_id = :store_id 
    AND (:product_name IS NULL OR LOWER(p.name) LIKE CONCAT('%', LOWER(:product_name), '%'))
    AND (:category_name IS NULL OR LOWER(c.name) LIKE CONCAT('%', LOWER(:category_name), '%'))
    """, nativeQuery = true)

    Page<Product> findByStore(
            @Param("store_id") UUID storeId,
            @Param("product_name") String productName,
            @Param("category_name") String categoryName,
            Pageable pageable
    );


}
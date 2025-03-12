package com.uevitondev.deliverybackend.domain.store;

import com.uevitondev.deliverybackend.domain.seller.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {

    // List<Store> findByNameContaining(String name);

    @Query("SELECT s FROM Store s WHERE LOWER(s.name) LIKE %:name% ")
    List<Store> findAllByName(@Param("name") String name);


    @Query("SELECT s FROM Store s WHERE LOWER(s.name) =  LOWER(:name)")
    Optional<Store> findByName(@Param("name") String name);

    @Query("SELECT s FROM Store s WHERE s.seller = :seller")
    List<Store> findBySeller(@Param("seller") Seller seller);

    @Query("""
            SELECT s
            FROM Store s 
            WHERE 
            :store_name IS NULL
            OR  
            LOWER(s.name) LIKE CONCAT('%', LOWER(:store_name), '%')
            """)
    List<Store> findStoresWithFilters(@Param("store_name") String storeName);


}

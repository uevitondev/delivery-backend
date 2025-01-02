package com.uevitondev.deliverybackend.domain.store;

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

    @Query("""
            SELECT s
            FROM Store s 
            WHERE (
                      :name = '' OR 
                      LOWER(s.name) LIKE CONCAT('%', LOWER(:name), '%') OR 
                      NOT EXISTS (
                          SELECT 1 FROM Store s2 
                          WHERE s2.name = :name AND LOWER(s2.name) LIKE CONCAT('%', LOWER(:name), '%')
                      )
                  ) 
            
            """)
    List<Store> findStoresWithFilters(@Param("name") String name);


}

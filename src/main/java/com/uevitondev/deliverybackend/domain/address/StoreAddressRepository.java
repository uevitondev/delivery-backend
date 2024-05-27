package com.uevitondev.deliverybackend.domain.address;

import com.uevitondev.deliverybackend.domain.store.Store;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface StoreAddressRepository extends JpaRepository<StoreAddress, UUID> {
    @Query("SELECT sa FROM StoreAddress sa WHERE sa.store = :store")
    List<StoreAddress> findAllByStore(@Param("store") Store store);
}

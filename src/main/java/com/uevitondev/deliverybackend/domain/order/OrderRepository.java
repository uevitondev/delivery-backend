package com.uevitondev.deliverybackend.domain.order;

import com.uevitondev.deliverybackend.domain.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {


    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.customer = :customer")
    List<Order> findByCustomer(@Param("customer") Customer customer);

    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.store.id = :storeId")
    List<Order> findByStore(@Param("storeId") UUID storeId);

    @Query("SELECT o FROM Order o  LEFT JOIN FETCH o.store LEFT JOIN FETCH o.orderItems WHERE o.id = :id")
    Optional<Order> findByIdWithStoreAndOrderItemsDetails(@Param("id") UUID id);


}

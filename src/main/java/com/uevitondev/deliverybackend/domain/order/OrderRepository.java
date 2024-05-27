package com.uevitondev.deliverybackend.domain.order;

import com.uevitondev.deliverybackend.domain.customer.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.id = :id")
    Optional<Order> findByIdWithItems(@Param("id") Long id);

    @Query(value = " SELECT o.id, o.created_at, o.updated_at, o.status, o.total, o.address_id, o.customer_id, o.store_id FROM tb_order o  WHERE o.id = :id", nativeQuery = true)
    Optional<Order> findByIdTest(@Param("id") UUID id);

    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems oi JOIN FETCH oi.product WHERE o.customer = :customer")
    List<Order> findOrdersByCustomer(@Param("customer") Customer customer);


}

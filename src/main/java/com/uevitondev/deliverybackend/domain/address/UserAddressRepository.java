package com.uevitondev.deliverybackend.domain.address;

import com.uevitondev.deliverybackend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {

    @Query("SELECT ua FROM UserAddress ua WHERE ua.user = :user")
    List<UserAddress> findAllByUser(@Param("user") User user);

}

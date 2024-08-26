package com.uevitondev.deliverybackend.domain.tokenverification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenVerificationRepository extends JpaRepository<TokenVerification, UUID> {

    Optional<TokenVerification> findByToken(String token);
}
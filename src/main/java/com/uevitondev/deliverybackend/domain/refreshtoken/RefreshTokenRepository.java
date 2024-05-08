package com.uevitondev.deliverybackend.domain.refreshtoken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String refreshToken);

    @Query(value = "SELECT tb_rt.* FROM TB_REFRESH_TOKEN tb_rt WHERE tb_rt.user_id = :userId", nativeQuery = true)
    Optional<RefreshToken> findRefreshTokenByUserId(UUID userId);

}
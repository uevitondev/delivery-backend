package com.uevitondev.deliverybackend.domain.refreshtoken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String refreshToken);

    @Query(value = "SELECT tr.* FROM TB_REFRESH_TOKEN tb_rt " +
            "INNER JOIN USER_DETAILS ud ON tr.user_id = ud.id " +
            "WHERE ud.username = :username and tr.revoked = false ", nativeQuery = true)
    List<RefreshToken> findAllRefreshTokenByUserName(String username);

}
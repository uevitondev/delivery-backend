package com.uevitondev.deliverybackend.domain.passwordresettoken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

    Optional<PasswordResetToken> findByToken(String token);

    @Query(
            nativeQuery = true,
            value = "SELECT tb_prt.* FROM TB_PASSWORD_RESET_TOKEN tb_prt WHERE tb_prt.user_id = :userId"
    )
    Optional<PasswordResetToken> findByUserId(UUID userId);

}
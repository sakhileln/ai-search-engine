package za.auth.repository;

import za.auth.entity.PasswordResetToken;
import za.auth.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

  Optional<PasswordResetToken> findByToken(String token);

  Optional<PasswordResetToken> findByUser(User user);

  @Modifying
  @Query("DELETE FROM PasswordResetToken p WHERE p.user = :user")
  void deleteByUser(@Param("user") User user);

  @Modifying
  @Query("DELETE FROM PasswordResetToken p WHERE p.expiryDate < :now OR p.used = true")
  void deleteAllExpiredAndUsedTokens(@Param("now") LocalDateTime now);
}

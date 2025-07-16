package za.auth.repository;

import za.auth.entity.EmailVerificationToken;
import za.auth.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

  Optional<EmailVerificationToken> findByToken(String token);

  Optional<EmailVerificationToken> findByUser(User user);

  @Modifying
  @Query("DELETE FROM EmailVerificationToken e WHERE e.user = :user")
  void deleteByUser(@Param("user") User user);

  @Modifying
  @Query("DELETE FROM EmailVerificationToken e WHERE e.expiryDate < :now OR e.used = true")
  void deleteAllExpiredAndUsedTokens(@Param("now") LocalDateTime now);
}

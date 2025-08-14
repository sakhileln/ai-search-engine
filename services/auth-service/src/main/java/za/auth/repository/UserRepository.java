package za.auth.repository;

import za.auth.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Boolean existsByEmail(String email);

  @Query("SELECT u FROM User u WHERE u.email = :email AND u.enabled = true")
  Optional<User> findByEmailAndEnabled(@Param("email") String email);

  @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
  long countByRole(@Param("role") User.Role role);
}

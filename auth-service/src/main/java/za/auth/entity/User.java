package za.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users",
      uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
      })
public class User implements UserDetails {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;

  @NotBlank
  @Size(max = 50)
  private String firstName;

  @NotBlank
  @Size(size = 50)
  private String lastName;

  @Enumerated(EnumType.STRING)
  private Role role = Role.USER;

  private boolean enabled = false;
  private boolean accountNonExpired = true;
  private boolean accountNonLocked = true;
  private boolean credentialsNonExpired = true;
  private boolean mfaEnabled = false;
  private String mfaSecret;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  public enum Role {
    USER, ADMIN, MODERATOR
  }

  // constructors
  public User() {}

  public User(String email, String password, String firstName, String lastName) {
    this.email = email;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  // UserDetails implementation
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  // getters and setters
  //
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }

  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }

  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }

  public Role getRole() { return role; }
  public void setRole(Role role) { this.role = role; }

  public void setEnabled(boolean enabled) { this.enabled = enabled; }
  public void setAccountNonExpired(boolean accountNonExpired) { this.accountNonExpired = accountNonExpired; }
  public void setAccountNonLocked(boolean accountNonLocked) { this.accountNonLocked = accountNonLocked; }
  public void setCredentialsNonExpired(boolean credentialsNonExpired) { this.credentialsNonExpired = credentialsNonExpired; }
  
  public boolean isMfaEnabled() { return mfaEnabled; }
  public void setMfaEnabled(boolean mfaEnabled) { this.mfaEnabled = mfaEnabled; }

  public String getMfaSecret() { return mfaSecret; }
  public void setMfaSecret(String mfaSecret) { this.mfaSecret = mfaSecret; }

  public LocalDateTime getCreatedAt() { return createdAt; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }

}

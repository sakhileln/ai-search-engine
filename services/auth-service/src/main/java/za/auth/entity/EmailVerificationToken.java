package za.auth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_verification_tokens")
public class EmailVerificationToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private boolean used = false;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructors
    public EmailVerificationToken() {}

    public EmailVerificationToken(String token, User user, int expirationTimeInMinutes) {
        this.token = token;
        this.user = user;
        this.expiryDate = LocalDateTime.now().plusMinutes(expirationTimeInMinutes);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}

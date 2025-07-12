package za.auth.dto;

import za.auth.entity.User;

public class AuthResponse {

  private String accessToken;
  private String refreshToken;
  private String tokenType = "Bearer";
  private Long userId;
  private String email;
  private String firstName;
  private String lastName;
  private String role;
  private boolean mfaEnabled;
  private boolean requiresMfa;

  // constructors
  //
  public AuthResponse() {}

  public AuthResponse(String accessToken, String refreshToken, User user) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.userId = user.getId();
    this.email = user.getEmail();
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.role = user.getRole().name();
    this.mfaEnabled = user.isMfaEnabled();
  }

  // getters and setters
  public String getAccessToken() { return accessToken; }
  public void setAccessToken(String refreshToken) { this.accessToken = accessToken; }

  public String getRefreshToken() { return refreshToken; }
  public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

  public String getTokenType() { return tokenType; }
  public void setTokenType(String tokenType) { this.tokenType = tokenType; }

  public Long getUserId() { return userId; }
  public void setUserId(Long userId) { this.userId = userId; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }

  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }

  public String getRole() { return role; }
  public void setRole(String role) { this.role = role; }

  public boolean isMfaEnabled() { return mfaEnabled; }
  public void setMfaEnabled(boolean mfaEnabled) { this.mfaEnabled = mfaEnabled; }

  public boolean isRequiredMfa() { return requiresMfa; }
  public void setRequiresMfa(boolean requiresMfa) { this.requiresMfa = requiresMfa; }
}

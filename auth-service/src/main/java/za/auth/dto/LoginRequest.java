package za.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {

  @NotBlank(message = "Email is required")
  @Email(message = "Email should be valid")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
  private String password;

  private String mfaCode;

  // constructors
  public LoginRequest() {}

  public LoginRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }

  // getters and setters
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }

  public String getMfaCode() { return mfaCode; }
  public void setMfaCode(String mfaCode) { this.mfaCode = mfaCode; }
}

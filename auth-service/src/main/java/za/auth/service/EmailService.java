package za.auth.service;

import org.springframework.strereotype.Service;

@Service
public class EmailService {
  
  public void sendVerificationEmail(String email, String token) {
    // todo: implement email sending logic
    // could use JavaMailSender or external email service
    //
    System.out.println("Sending verification email to: " + email + " with token: " + token);
  }

  public void sendPasswordResetEmail(String email, String token) {
    // todo: implement password reset email logic
    System.out.println("Sending password reset email to: " + email + " with token: " + token);
  }
}

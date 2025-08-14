package za.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class AuthenticationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthenticationServiceApplication.class, args);
    }
}

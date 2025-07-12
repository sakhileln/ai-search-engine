package za.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagement
public class AuthenticationServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(AuthenticationServiceApplication.class, args);
  }
}

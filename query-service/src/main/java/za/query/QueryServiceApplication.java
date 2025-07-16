package za.co.search.engine.query;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class QueryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(QueryServiceApplication.class, args);
    }
}

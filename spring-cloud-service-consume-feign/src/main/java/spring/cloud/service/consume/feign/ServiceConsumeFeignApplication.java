package spring.cloud.service.consume.feign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ServiceConsumeFeignApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceConsumeFeignApplication.class, args);
    }
}

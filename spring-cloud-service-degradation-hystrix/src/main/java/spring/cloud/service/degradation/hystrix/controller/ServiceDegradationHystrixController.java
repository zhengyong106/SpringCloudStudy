package spring.cloud.service.degradation.hystrix.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ServiceDegradationHystrixController {
    @Autowired
    //注入restTemplate
    private RestTemplate restTemplate;

    @GetMapping("/getService")
    @HystrixCommand(fallbackMethod = "hystrixFallback")
    public String getService(){
        // 当启用Ribbon后直接访问服务提供者接口会报错
        // return restTemplate.getForEntity("http://localhost:8011/serviceDiscovery",String.class).getBody();

        // 使用restTemplate调用微服务接口
        return restTemplate.getForEntity("http://spring-cloud-service-register/serviceDiscovery", String.class).getBody();
    }

    public String hystrixFallback(){
        return "error";
    }
}

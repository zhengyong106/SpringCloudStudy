package spring.cloud.service.consume.ribbon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ServiceConsumeRibbonController {
    @Autowired
    //注入restTemplate
    private RestTemplate restTemplate;

    @GetMapping("/getService")
    public String getService(){
        // 这里注释掉，因为之前想当然使用了直链访问服务提供者的接口，这样是不会返回结果的，而且会报错
        // return restTemplate.getForEntity("http://localhost:8011/serviceDiscovery",String.class).getBody();
        // 使用restTemplate调用微服务接口

        return restTemplate.getForEntity("http://spring-cloud-service-register/serviceDiscovery", String.class).getBody();
    }

    @GetMapping("/getService/{serviceId}")
    public String getServiceById(@PathVariable("serviceId") String serviceId){
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("serviceId", serviceId);
        // 使用restTemplate调用微服务接口
        return restTemplate.getForEntity("http://spring-cloud-service-register/serviceDiscovery?serviceId={serviceId}", String.class, uriVariables).getBody();
    }
}

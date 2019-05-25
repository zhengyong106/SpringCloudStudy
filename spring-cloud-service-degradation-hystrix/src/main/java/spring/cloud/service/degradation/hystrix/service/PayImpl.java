package spring.cloud.service.degradation.hystrix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import spring.cloud.service.degradation.hystrix.entity.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PayImpl implements PayService{
    @Autowired
    //注入restTemplate
    private RestTemplate restTemplate;

    @Override
    public List<User> getUsers() {
        // 当启用Ribbon后直接访问服务提供者接口会报错
        // ResponseEntity<User[]> responseEntity= restTemplate.getForEntity("http://localhost:8011/serviceDiscovery", User[].class);

        // 使用restTemplate调用微服务接口
        ResponseEntity<User[]> responseEntity= restTemplate.getForEntity("http://spring-cloud-service-register/getUsers", User[].class);
        return Arrays.asList(responseEntity.getBody());
    }

    @Override
    public User getUser(String userId) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("userId", userId);
        // 使用restTemplate调用微服务接口
        ResponseEntity<User> responseEntity= restTemplate.getForEntity("http://spring-cloud-service-register/getUser?userId={userId}", User.class, uriVariables);
        return responseEntity.getBody();
    }
}

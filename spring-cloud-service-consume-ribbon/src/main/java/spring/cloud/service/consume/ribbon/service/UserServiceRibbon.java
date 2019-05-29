package spring.cloud.service.consume.ribbon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import spring.cloud.service.consume.ribbon.entity.User;

import java.util.*;

@Service
public class UserServiceRibbon implements UserService{
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<User> getUsers() {
        // 使用restTemplate调用微服务接口
        ResponseEntity<User[]> responseEntity= restTemplate.getForEntity("http://spring-cloud-service-register/getUsers", User[].class);
        return Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
    }

    @Override
    public User getUser(String userId) {
        // 使用restTemplate调用微服务接口
        ResponseEntity<User> responseEntity= restTemplate.getForEntity("http://spring-cloud-service-register/getUser/{userId}", User.class, userId);
        return responseEntity.getBody();
    }
}

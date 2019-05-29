package spring.cloud.service.consume.feign.service;

import org.springframework.stereotype.Component;
import spring.cloud.service.consume.feign.entity.User;

import java.util.List;

// Feign客户端开启hystrix后服务降级方法
@Component
public class UserControllerFeignFallback implements UserControllerFeign {
    @Override
    public List<User> getUsers() {
        System.out.println("System error");
        return null;
    }

    @Override
    public User getUser(String userId) {
        System.out.println("System error");
        return null;
    }

    @Override
    public String retry() {
        System.out.println("System error");
        return null;
    }
}

package spring.cloud.service.register.controller.fallback;

import org.springframework.stereotype.Component;
import spring.cloud.service.register.controller.UserFeign;
import spring.cloud.service.register.entity.User;

import java.util.List;

// Feign客户端开启hystrix后服务降级方法
@Component
public class UserFeignFallback implements UserFeign {
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

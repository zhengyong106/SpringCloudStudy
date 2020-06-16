package spring.cloud.service.register.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import spring.cloud.service.register.controller.fallback.UserFeignFallback;
import spring.cloud.service.register.entity.User;

import java.util.List;

@FeignClient(value = "spring-cloud-service-register", fallback = UserFeignFallback.class)
public interface UserFeign {
    @GetMapping("/getUsers")
    List<User> getUsers();

    @GetMapping("/getUser/{userId}")
    User getUser(@PathVariable("userId") String userId);

    @GetMapping("/retry")
    String retry();
}

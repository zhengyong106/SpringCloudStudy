package spring.cloud.service.consume.feign.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import spring.cloud.service.consume.feign.entity.User;

import java.util.List;

public interface UserController {
    @GetMapping("/getUsers")
    List<User> getUsers();

    @GetMapping("/getUser/{userId}")
    User getUser(@PathVariable("userId") String userId);

    @GetMapping("/retry")
    String retry();
}

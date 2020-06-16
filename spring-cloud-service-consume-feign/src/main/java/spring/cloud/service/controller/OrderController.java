package spring.cloud.service.consume.feign.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import spring.cloud.service.register.controller.UserFeign;
import spring.cloud.service.register.entity.User;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    UserFeign userFeign;

    @GetMapping("/getUsers")
    public List<User> getUsers(){
        return userFeign.getUsers();
    }

    @GetMapping("/getUser/{userId}")
    public User getUser(@PathVariable("userId") String userId){
        return userFeign.getUser(userId);
    }

    @GetMapping("/retry")
    public String feignRetry(){
        System.out.println(1);
        return userFeign.retry();
    }
}

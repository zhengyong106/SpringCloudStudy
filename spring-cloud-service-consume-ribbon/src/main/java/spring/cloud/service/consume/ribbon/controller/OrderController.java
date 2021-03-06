package spring.cloud.service.consume.ribbon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import spring.cloud.service.consume.ribbon.entity.User;
import spring.cloud.service.consume.ribbon.service.UserService;

import java.util.List;

@RestController
public class OrderController {
    @Autowired
    UserService userService;

    @GetMapping("/getUsers")
    public List<User> getService(){
        return userService.getUsers();
    }

    @GetMapping("/getUser/{userId}")
    public User getServiceById(@PathVariable("userId") String userId){
        return userService.getUser(userId);
    }
}

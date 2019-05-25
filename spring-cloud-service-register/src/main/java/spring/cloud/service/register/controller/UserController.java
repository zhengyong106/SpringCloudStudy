package spring.cloud.service.register.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.cloud.service.register.entity.User;
import spring.cloud.service.register.service.UserService;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService; //注入发现客户端

    @GetMapping("/getUsers")
    public List<User> serviceDiscovery(){
        return userService.getUsers();
    }

    @GetMapping("/getUser")
    public User serviceDiscovery(String userId){
        return userService.getUser(userId);
    }
}

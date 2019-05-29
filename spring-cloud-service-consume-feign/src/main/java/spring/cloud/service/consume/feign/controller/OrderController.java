package spring.cloud.service.consume.feign.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import spring.cloud.service.consume.feign.entity.User;
import spring.cloud.service.consume.feign.service.UserControllerFeign;

import java.util.List;

@RestController
public class OrderController {
    @Autowired
    UserControllerFeign userControllerFeign;

    @GetMapping("/getUsers")
    public List<User> getUsers(){
        return userControllerFeign.getUsers();
    }

    @GetMapping("/getUser/{userId}")
    public User getUser(@PathVariable("userId") String userId){
        return userControllerFeign.getUser(userId);
    }
}
